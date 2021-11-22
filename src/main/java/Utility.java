import java.util.ArrayList;
import java.util.Collections;

public class Utility 
{	
	static int tolerance = 1; //TOLERANCE IS HOW MANY CHANGES NEED TO BE MADE FOR A GUESS TO BE CORRECT.  
	
	//CHECKS GUESS AND COMPARES WITH KEY. 
	public static int checkGuess(String guess, String key)
	{
		//CASE 0: THE KEYS ARE EQUAL. RETURN 1
		if(guess.toUpperCase().equals(key.toUpperCase()) )
		{
			return 1; 
		}
		
		//CASE 2: THEY ARE CLOSE
		if(distance(guess, key) <= tolerance)
		{
			return 0; 
		}
		//CASE 3: THEY ARE NOT CLOSE OR THE SAME
		else 
		{
			return -1; 
		}
		
	}
	
	//FINDS THE NUMBER OF CHANGES NEEDED TO GET FROM GUESS TO KEY
	private static int distance (String guess, String key)
	{
		guess = guess.toUpperCase(); 
		key = key.toUpperCase(); 
		//Levenshtein distance 
	    int[][] opt = new int[guess.length() + 1][key.length() + 1];

	    for (int i = 0; i <= guess.length(); i++) 
	    {
	        for (int j = 0; j <= key.length(); j++) 
	        {
	            if (i == 0) //BASE CASE
	            {
	                opt[i][j] = j;
	            }
	            else if (j == 0) //BASE CASE
	            {
	                opt[i][j] = i;
	            }
	            else 
	            {
	            	ArrayList<Integer> temp = new ArrayList<Integer>(); 
	            	temp.add(opt[i - 1][j - 1] + costOfSubstitution(guess.charAt(i - 1), key.charAt(j - 1))); 
	            	temp.add(opt[i - 1][j] + 1); 
	            	temp.add(opt[i][j - 1] + 1); 
	            	opt[i][j] = Collections.min(temp); 
	            }
	        }
	    }
	    return opt[guess.length()][key.length()];
	}
	
	private static int costOfSubstitution(char a, char b)
	{
		if(a==b) //NO CHANGE NEEDED
		{
			return 0; 
		}
		else
		{
			return 1; //ONE CHANGE NEEDED
		}
	}
}
