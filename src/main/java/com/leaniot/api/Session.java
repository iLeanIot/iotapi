package com.leaniot.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.leaniot.exception.StatusException;
import com.leaniot.exception.ValueException;

@Component
public abstract class Session {
	private static final String WS_IOT = "/ws_iot";
	private static final String IOTP = "iotp";
	private static final String IOTPS = "iotps";
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final String WS = "ws";
	private static final String WSS = "wss";
	private static final String regex = "^(" + IOTP + "|" + IOTPS + ")://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	private String sessionId;
	private String uri;
	protected boolean logined;
		
	protected RestTemplate restTemplate;
	
	public Session() {
		this.restTemplate = new RestTemplate();
		this.logined = false;
	}

	public void start(String username, String password, String uri) {
		this.uri = uri;

		if(!this.uri.matches(regex))
			throw new ValueException(uri);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", username);
		map.add("password", password);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(getRestUri() + "/login", request, String.class);
		String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		String[] session = cookie.split(";");
		this.sessionId = session[0].substring(11);
		this.logined = true;
	}

	public void stop() {
		if (logined) {
			HttpHeaders header = getSessionHeader();
			HttpEntity<String> request = new HttpEntity<String>(header);
			restTemplate.getForObject(getRestUri() + "/logout", String.class, request);
		}
	}

	public HttpHeaders getSessionHeader() {
		assert logined : "login first";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", "JSESSIONID=" + this.sessionId);
		return requestHeaders;
	}

	public String getRestUri() {
		if(this.uri.startsWith(IOTP)) 
			return this.uri.replaceFirst(IOTP, HTTP);
		else if(this.uri.startsWith(IOTPS))
			return this.uri.replaceFirst(IOTPS, HTTPS);
		else
			throw new ValueException(uri);
	}
	
	public String getWSUri() {
		assert logined : "login first";
		if(this.uri.startsWith(IOTP)) 
			return this.uri.replaceFirst(IOTP, WS) + WS_IOT;
		else if(this.uri.startsWith(IOTPS))
			return this.uri.replaceFirst(IOTPS, WSS) + WS_IOT;
		else
			throw new ValueException(uri);
	}
	
	protected <T> T getEntity(String getUri, Class<T> responseType) {
		assert logined : "login first";
		HttpHeaders header = getSessionHeader();
		HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(null, header);
		try {
			ResponseEntity<T> rssResponse = restTemplate.exchange(getRestUri() + getUri, HttpMethod.GET, requestEntity, responseType);
			return rssResponse.getBody();
		} catch(ResourceAccessException e) {
			throw new StatusException(e.getMessage());
		} catch(HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
			throw new StatusException(e.getResponseBodyAsString());
		}
		
	}
	
	protected <T> T postEntity(String postUri, Object request, Class<T> responseType) {
		assert logined : "login first";
		HttpHeaders header = getSessionHeader();
		HttpEntity<?> requestEntity = new HttpEntity<>(request, header);
		try{
			ResponseEntity<T> rssResponse = restTemplate.exchange(getRestUri() + postUri, HttpMethod.POST, requestEntity, responseType);
			return rssResponse.getBody();
		} catch(ResourceAccessException e) {
			throw new StatusException(e.getMessage());
		} catch(HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
			throw new StatusException(e.getResponseBodyAsString());
		}
		
		
	}
}
