
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/endpoint1")

public class JoinEndpoint {
	private Room room = Room.getRoom();
	@OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	@OnMessage
	public void onMessage(String message, Session session) {
		room.join(message,session);
	}
	@OnMessage
	public void onImage(byte[] data,boolean last, Session session) {
		
	}
	@OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
}
