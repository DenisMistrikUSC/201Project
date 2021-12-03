import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.websocket.Session;

//This is the main backend function for the game, handles all requests from the frontend and sends responses accordingly 
//Also includes all of the data structures for the game

public class Room {
    private static Room instance = null;  
    final int playerCount = 2; //how many registered users are allowed in before the game starts
    public List<Session> sessions = new ArrayList<Session>(); //this is the primary list of all connected users
    public List<Session> registered = new ArrayList<Session>(); //this is the list of all registered sessions and is used to determine the drawer
    public Map<Session,Player> players = new HashMap<Session,Player>(); //this hashmap is used to store a particular session's player information
    public Session currDrawer; //the current drawer
    public int round; //the current round of the game, determined by the frontend
    static List<String> words = Arrays.asList("Coliseum", "Traveller", "Tommy", "Marching Band" ,"Trojan Check", "Village", 
			"California", "Target", "Fountain", "Bricks"); //list of all possible prompts for the drawer, gets shuffled randomly 
    //main function for receiving  all frontend requests
    public synchronized void signalRoom(String mess, Session session) {
    	//edge case
    	if(mess.isBlank()) {
    	}
    	// A ! signifies that the start game button was pressed by a client so they should be added to the game
    	else if(mess.charAt(0) == '!') {
    		sessions.add(session); //adds the player to the session array
    		Player p = null;
    		if(mess.substring(1).equals("g-")) { //the players username will be g- if they are a guest
    			p = new Player("Guest "  + session.getId(), true);
    		} else { //else add a regular player
    			p = new Player(mess.substring(1) + " ", false);
    			registered.add(session);
    		}		
    		players.put(session,p); //add the session, player class pair into the map
    		sendMessage(p.getName() + " joined"); //signal the server that this player has in fact joined
    		if(registered.size() == playerCount) { //if enough registered users join, start the game
    			startGame();
    		}
    	//An @ sent from the front end signifies that the round has ended so select a new drawer
    	} else if(mess.charAt(0) == '@') { 
    		round = Integer.parseInt(mess.substring(1)); //get the current round
    		players.get(registered.get(round-1)).becomeDrawer(); //select the next person in line to draw from the list of registered users
    		currDrawer = registered.get(round-1);
    		try {
    			//send the * as the first character to assign the new drawer on the front end and send him the next word
    			registered.get(round-1).getBasicRemote().sendText("*Your word is " + words.get(round)); 
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		System.out.println("Word is " + words.get(round));
    	} 
    	//A $ means the game has concluded on the front end and a winner should be selected
    	else if(mess.equals("$")) {
    		List<Player> ps = new ArrayList<Player>(players.values());
    		Player[] playerArray = ps.toArray(new Player[ps.size()]);
    		MergeSort.threadedSort(playerArray);
    		sendMessage(playerArray[playerArray.length-1].getName() + " won with a score of " + playerArray[playerArray.length-1].getPoints());
    	} 
    	//A & is used to send the coordinates for the objects drawn by the drawer to the other clients so that they can see his drawing in real time
    	else if(mess.charAt(0) == '&') {
    		sendMessage(mess);
    	} 
    	//A * signals that the previous drawer should have their isDrawing set back to false
    	else if(mess.equals("*")) {
    		players.get(session).stopDrawing();
    	}
    	//If no unique character is at the front of the message this means that server should handle an ordinary chat message
    	else {
    		//only lets messages by the guessers get displayed
    		if(players.get(session).isDrawing() == false) {
    			handleGuess(mess,session);
    		}
    	}
    }
    //this function gets called once the enough registered users join and prepares all of the necessary data structures for the game and signals the frontend
    public synchronized void startGame() {
    	sendMessage("#"); //signal the front end to begin the game on the client side
		Collections.shuffle(words); //shuffle the word list
		Collections.shuffle(registered); //shuffle the order of drawers
		players.get(registered.get(0)).becomeDrawer(); //get the first drawer
		currDrawer = registered.get(0); 
		try {
			sessions.get(0).getBasicRemote().sendText("*Your word is " + words.get(round));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Word is "  + words.get(round));
		
    }
    public synchronized void leave(Session session) { sessions.remove(session); }
    //function used to broadcast a message to every client by looping through the list of active sessions
    public synchronized void sendMessage(String message) {
        for (Session session: sessions) {
            if (session.isOpen()) {
                try { session.getBasicRemote().sendText(message); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
    //creates a new room if there isnt one already
    public synchronized static Room getRoom() {
        if (instance == null) { instance = new Room(); }
        return instance;
    }
    //main function to handle guesses from the players, the last two characters are the timestamp in seconds and are used to determine the score
    public synchronized void handleGuess(String message, Session session) {
    	String guess = message.substring(0, message.length()-2); //get the guess itself
    	int time = Integer.parseInt(message.substring(message.length()-2)); //get the timestamp in seconds
    	int result = Utility.checkGuess(guess,words.get(round)); //uses a utility function to check if the guess is correct or close
    	//result == 1 means the player guessed correctly and should be assigned points based on the formula below, the drawer also gets points
    	if(result == 1) { 
    		sendMessage("Player " + session.getId() + " Guessed Correctly!");
    		int score = (800 - 6*(81-time));
    		players.get(session).addPoints(score);    		
    		//the drawer gets points for each player that guesses their drawing, and is capped at 800 per round if everyone guesses correctly
    		players.get(currDrawer).addPoints(800/(playerCount-1)); 
    	} 
    	//if the player's guess was off by a single character signal to him that they were close
    	else if (result == 0) {
    		sendMessage("Player " + session.getId() + " Is Close!");
    	} 
    	//if the guess is wrong or not intended to be a guess, broadcast the player's message to everyone 
    	else {
    		sendMessage("[" + players.get(session).getName() + "]: " + guess);
    	}
    }
}