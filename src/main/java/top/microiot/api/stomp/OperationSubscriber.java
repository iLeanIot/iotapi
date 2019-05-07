package top.microiot.api.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.api.dto.Request;
import top.microiot.api.dto.Response;
import top.microiot.domain.Device;

public abstract class OperationSubscriber extends AbstractEventSubscriber {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Request request;
	private Device device;
	private WebsocketDeviceSession websocketDeviceSession;
	
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public WebsocketDeviceSession getWebsocketDeviceSession() {
		return websocketDeviceSession;
	}
	public void setWebsocketDeviceSession(WebsocketDeviceSession websocketDeviceSession) {
		this.websocketDeviceSession = websocketDeviceSession;
	}
	public abstract Response getResponse();
	@Override
	public void onEvent(Object event) {
		request = (Request)event;
		logger.debug("request: " + request);
	}

}