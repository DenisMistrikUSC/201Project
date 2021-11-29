import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.websocket.Session;

public class Room {
    private static Room instance = null;  
    final int playerCount = 2;
    public List<Session> sessions = new ArrayList<Session>();
    public Map<Integer,Integer> scores = new HashMap<Integer,Integer>();
    public Map<Session,Player> players = new HashMap<Session,Player>();
    public Session currDrawer;
    static boolean roundRunning = false;
    public int round;
    static List<String> words = Arrays.asList("Coliseum", "Traveller", "Tommy", "Marching Band" ,"Trojan Check", "Village", 
			"California", "Target", "Fountain", "Bricks");
    public synchronized void join(String mess, Session session) { 
    	if(mess.isBlank()) {
    		sessions.add(session);
    		scores.put(Integer.parseInt(session.getId()),0);
    		Player p = new Player("Player " + session.getId(), false,session);
    		players.put(session,p);
    		sendMessage("Player " + session.getId() + " joined");
    		if(sessions.size() == playerCount) {
    			startGame();
    		}
    	} else if(mess.charAt(0) == '@') {
    		round = Integer.parseInt(mess.substring(1));
    		players.get(sessions.get(round-1)).becomeDrawer();
    		currDrawer = sessions.get(round-1);
    		try {
    			sessions.get(round-1).getBasicRemote().sendText("*Your word is " + words.get(round));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		System.out.println("Word is " + words.get(round));
    	} else if(mess.equals("$")) {
    		List<Player> ps = new ArrayList<Player>(players.values());
    		Collections.sort(ps);
    		int winner = getWinner(scores);
    		sendMessage(ps.get(0).getName() + " won with a score of " + ps.get(0).getPoints());
    	} else if(mess.charAt(0) == '&') {
    		sendMessage(mess);
    	} else if(mess.equals("*")) {
    		players.get(session).stopDrawing();
    	}
    	else {
    		if(players.get(session).isDrawing() == false) {
    			handleGuess(mess,session);
    		}
    	}
    }
    public synchronized void startGame() {
    	sendMessage("#");
		Collections.shuffle(words);
		Collections.shuffle(sessions);
		players.get(sessions.get(0)).becomeDrawer();
		currDrawer = sessions.get(0);
		try {
			sessions.get(0).getBasicRemote().sendText("*Your word is " + words.get(round));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Word is "  + words.get(round));
		
    }
    public synchronized void leave(Session session) { sessions.remove(session); }
    public synchronized void sendMessage(String message) {
        for (Session session: sessions) {
            if (session.isOpen()) {
                try { session.getBasicRemote().sendText(message); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    public synchronized static Room getRoom() {
        if (instance == null) { instance = new Room(); }
        return instance;
    }
    public synchronized void handleGuess(String message, Session session) {
    	String guess = message.substring(0, message.length()-2);
    	int time = Integer.parseInt(message.substring(message.length()-2));
    	int result = Utility.checkGuess(guess,words.get(round));
    	if(result == 1) {
    		sendMessage("Player " + session.getId() + " Guessed Correctly!");
    		int sc = scores.get(Integer.parseInt(session.getId()));
    		sc += (600 - 6*(81-time));
    		scores.replace(Integer.parseInt(session.getId()), sc);
    		players.get(session).addPoints(sc);
    		players.get(currDrawer).addPoints(800/(playerCount-1));
    	} else if (result == 0) {
    		sendMessage("Player " + session.getId() + " Is Close!");
    	} else {
    		sendMessage("Player " + session.getId() + ": " + guess);
    	}
    }
    public Integer getWinner(Map<Integer,Integer> s) {
    	int winner = 0;
    	for(Integer i: s.keySet()) {
    		if(s.get(winner) < s.get(i)) {
    			winner = i;
    		}
    	}
    	return winner;
    }
}