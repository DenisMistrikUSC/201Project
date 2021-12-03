import javax.websocket.Session;

public class Player implements Comparable<Player> {
	String username;
	Integer currPoints;
	Integer iconID;
	boolean isGuest;
	boolean isDrawing;
	Session session;
	
	public Player(String name, boolean guest,Session s) {
		currPoints = 0;
		this.username = name;
		this.isGuest = guest;
		//this.iconID = iconID;
		isDrawing = false;
		this.session = s;
	}
	public void becomeDrawer() {
		isDrawing = true;
	}
	public void stopDrawing() {
		isDrawing = false;
	}
	public void addPoints(Integer points) {
		currPoints += points;
	}
	public String getName() {
		return username;
	}
	public Integer getIcon() {
		return iconID;
	}
	public Integer getPoints() {
		return currPoints;
	}
	public Session getSession() {
		return session;
	}
	public boolean isDrawing() {
		return isDrawing;
	}
	public int compareTo(Player comp) {
		/* For Descending order*/
		return comp.currPoints - currPoints;
	}
}
