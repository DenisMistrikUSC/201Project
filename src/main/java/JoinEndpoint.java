
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

//This class serves as the websocket itself and facilitates all data transfer between the frontend and backend, for a particular session

public class JoinEndpoint {
	private Room room = Room.getRoom(); //create the Room class
	//connect the client
	@OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
            //session.getId()
        }
    }
	//this function facilitates all of the requests to the backend 
	@OnMessage
	public void onMessage(String message, Session session) {
		room.signalRoom(message,session);
	}
	@OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
}
