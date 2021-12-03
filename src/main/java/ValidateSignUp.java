import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateSignUp 
{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
		response.setContentType("text/html;charset=UTF-8");
		String name = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		PrintWriter out = null; 
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		if(validateSignUp(name, pass))
			out.println("1"); 
		else
			out.println("0");
	}
	public static boolean validateSignUp(String uname, String pword)
	{
		String db = "jdbc:mysql://localhost/Logins";
		String user = "root"; 
		String pwd = "root"; 
	    String sql = "INSERT INTO login (username, passwords) VALUES (?, ?)";
	    boolean exists = false; 
	    String sql1 = "SELECT * FROM login WHERE username=?";
	    try (Connection conn = DriverManager.getConnection(db, user, pwd);
	    PreparedStatement ps = conn.prepareStatement(sql1);) 
	    {
	    	ps.setString(1, uname);
	    	ResultSet rs = ps.executeQuery();
	    	while (rs.next())
	    	{
	    		exists = true;
	    		break; 
	    	}
	    } 
	    catch (SQLException ex) 
	    {
	    	System.out.println ("SQLException: " + ex.getMessage());
	    }
		//CHECK IF USERNAME IS IN DATABASE
		if(exists)
		{
			return false; 
		}
		//IF NOT 
		else
		{
			try (Connection conn = DriverManager.getConnection(db, user, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);) 
			{
				ps.setString(1, uname);
				ps.setString(2, pword);
				int row = ps.executeUpdate();
				System.out.println(
									String.format("Number of rows affected %d", row));
			} 
			catch (SQLException ex) 
			{
				System.out.println ("SQLException: " + ex.getMessage());
			}
			return true; 
		}
	}
}
