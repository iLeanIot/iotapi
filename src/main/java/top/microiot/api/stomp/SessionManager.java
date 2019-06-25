package top.microiot.api.stomp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.stomp.WebSocketStompSessionManager;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import top.microiot.api.HttpSession;

public class SessionManager extends WebSocketStompSessionManager {
    private HttpSession session;
    protected List<StompSessionHandler> handlers = new ArrayList<StompSessionHandler>();
    
    public SessionManager(HttpSession session, WebSocketStompClient webSocketStompClient, String url) {
        super(webSocketStompClient, url);
        this.session = session;
		this.setAutoReceipt(true);
		this.setAutoStartup(true);
    }

    @Override
    protected ListenableFuture<StompSession> doConnect(StompSessionHandler handler) {
        restart();
        setHandshakeHeaders(new WebSocketHttpHeaders(session.getSessionHeader()));
        return super.doConnect(handler);
    }

    public void restart(){
        session.stop();
        session.start();
    }

    public void stop() {
    	for(StompSessionHandler handler : handlers) {
    		this.disconnect(handler);
    	}
        destroy();
        session.stop();
    }
}
