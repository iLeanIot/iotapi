package com.leaniot.api.device;

import org.springframework.integration.stomp.WebSocketStompSessionManager;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.leaniot.api.device.stomp.ActionSubscriber;
import com.leaniot.api.device.stomp.GetSubscriber;
import com.leaniot.api.device.stomp.SetSubscriber;
import com.leaniot.api.device.stomp.SubscribeAction;
import com.leaniot.api.device.stomp.SubscribeGet;
import com.leaniot.api.device.stomp.SubscribeSet;
import com.leaniot.domain.Device;

/**
 * 设备端与物联网平台的websocket会话
 *
 * @author 曹新宇
 */
public class WebsocketDeviceSession extends WebSocketStompSessionManager {
	private HttpDeviceSession session;
	
	public HttpDeviceSession getSession() {
		return session;
	}

	/**
	 * 设备端与物联网平台websocket会话构造函数。
	 * @param session 设备端http会话。
	 * @param webSocketStompClient 设备端与物联网平台websocket底层连接。
	 */
	public WebsocketDeviceSession(HttpDeviceSession session, WebSocketStompClient webSocketStompClient) {
		super(webSocketStompClient, session.getWSUri());
		this.session = session;
	}
	
	/**
	 * 设备端设置收到get后的处理操作。
	 * @param subscriber 收到get后的处理。
	 * @see com.leaniot.api.device.stomp.GetSubscriber
	 * @return 返回get处理。
	 */
	public SubscribeGet subscribe(GetSubscriber subscriber) {
		SubscribeGet sessionHandler = new SubscribeGet(this, subscriber);
        connect(sessionHandler);
        return sessionHandler;
	}
	
	/**
	 * 设备端设置收到set后的处理操作。
	 * @param subscriber 收到set后的处理。
	 * @see com.leaniot.api.device.stomp.SetSubscriber
	 * @return 返回set处理。
	 */
	public SubscribeSet subscribe(SetSubscriber subscriber) {
		SubscribeSet sessionHandler = new SubscribeSet(this, subscriber);
        connect(sessionHandler);
        return sessionHandler;
	}
	
	/**
	 * 设备端设置收到action后的处理操作。
	 * @param subscriber 收到action后的处理。
	 * @see com.leaniot.api.device.stomp.ActionSubscriber
	 * @return 返回action处理。
	 */
	public SubscribeAction subscribe(ActionSubscriber subscriber) {
		SubscribeAction sessionHandler = new SubscribeAction(this, subscriber);
        connect(sessionHandler);
        return sessionHandler;
	}
	
	/**
	 * 获取设备本身的信息。
	 * @return 返回设备信息。
	 */
	public Device getDevice() {
		return session.getDevice();
	}
	
	public void stop() {
		destroy();
		session.stop();
	}

	@Override
	protected ListenableFuture<StompSession> doConnect(StompSessionHandler handler) {
		session.stop();
		session.start();
		setHandshakeHeaders(new WebSocketHttpHeaders(session.getSessionHeader()));
		
		return super.doConnect(handler);
	}
	
	
}
