import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

public class Room {
    private static Room instance = null;
    public List<Session> sessions = new ArrayList<Session>();
    public Map<Integer,Integer> scores = new HashMap<Integer,Integer>();
    static boolean roundRunning = false;
    public int round;
    static List<String> words = Arrays.asList("Coliseum", "Traveller", "Tommy", "Marching Band" ,"Trojan Check", "Village", 
			"California", "Target", "Fountain", "Bricks");
    public synchronized void join(String mess, Session session) { 
    	if(mess.isBlank()) {
    		sessions.add(session);
    		scores.put(Integer.parseInt(session.getId()),0);
    		sendMessage("Player " + session.getId() + " joined");
    		if(sessions.size() == 2) {
    			//startGame();
    			sendMessage("#");
    			Collections.shuffle(words);
    		}
    	} else if(mess.equals("@")) {
    		round++;
    	} else if(mess.equals("$")) {
    		int winner = getWinner(scores);
    		sendMessage("Player " + winner + " won with a score of " + scores.get(winner) + "!");
    	}
    	else {
    		handleGuess(mess,session);
    	}
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