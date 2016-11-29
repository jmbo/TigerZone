import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class TCPServer 
{

	/**
	 * A TCP server that runs on port 9090.  When a client connects, it
	 * sends the client the current date and time, then closes the
	 * connection with that client.  Arguably just about the simplest
	 * server you can write.
	 */

	 /**
	 * Runs the server 
	 */
	
	    public static void main(String[] args) throws IOException, InterruptedException 
	    {
	        ServerSocket listener = new ServerSocket(9090);
	        try {
	            while (true) 
	            {
	                Socket socket = listener.accept();
	                
	                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	                
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                    
	                String response;
	                    
	                out.println("THIS IS SPARTA!");
	                
	                String username = null;
	                
	                while(true)
	                {
	                	// Poll the input string until something "pops" up
	                	response = in.readLine();
	                	         	
	                	// Authentication Protocol

	                	if (response.startsWith("JOIN")) 
	                	{
	    	           
	                		// display message on terminal screen
	                		System.out.println(response);
	                	
	                		
	                		out.println("HELLO!");
	                	  
	                	} 
	                
	                	else if (response.startsWith("I AM ")) 
	                	{	
	    		          
	                		System.out.println(response);
	                	
	                		String delims = "[ ]";
	                    	String[] tokens = response.split(delims);
	                    	
	                    	username = tokens[2];            		
	                		
	                		  
	                		out.printf("WELCOME %s PLEASE WAIT FOR THE NEXT CHALLENGE\n", username);
	                		
	                		Thread.sleep(2000);
	                			                			                		               		
	                		out.println("NEW CHALLENGE 1 YOU WILL PLAY 2 MATCHES");
	                		
	                		Thread.sleep(2000);
                            
                            
                            //out.println("BEGIN ROUND 1 of 2"); use this to expand to 2 rounds
	                		out.println("BEGIN ROUND 1 of 1");
	                		
	                		Thread.sleep(2000);
	                		
	                		out.println("YOUR OPPONENT IS PLAYER Blue");
	                		
	                		out.println("STARTING TILE IS TLTJ- AT 0 0 0");
	                		
	                		//out.println("THE REMAINING 23 tiles are [ TLLT- JJJJX TLLT- LLJJ- JJJJX LLLL- JLJL- TJTT- JLLL- LLJJ- LJLJ- TLJT- TLJTP JLTTB TLTJD TTLTT- LLLL- TLTJ- LJJJ- LJTJD JJJJX JLLL- JJJJX ]");
	                		
                            out.println("THE REMAINING 6 tiles are [ TLTTP LJTJ- JLJL- JJTJX JLTTB TLLT- ]");
                            
	                		out.println("MATCH BEGINS IN 15 SECONDS");
	                		
	                		Thread.sleep(2000);
	                		
	                		out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 1 PLACE TLLT-");
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 1 PLAYER Blue PLACED TLTTP AT 0 1 90 TIGER 8");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 2 PLACE LJTJ-");
                            
                            Thread.sleep(2000);
                            
                            out.println("MAME A MOVE 2 PLAYER Blue PLACED LJTJ- AT 0 2 180 TIGER 8");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 3 PLACE JLJL-");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 3 PLAYER Blue PLACED JLJL- AT 1 0 0 TIGER 4");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 4 PLACE JJTJX");
                            
                            Thread.sleep(2000); //ACTUAL BREAK - 15++ SECONDS
                            
                            out.println("GAME A MOVE 4 PLAYER Blue PLACED JJTJX AT 1 1 270 TIGER 5");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 5 PLACE JLTTB");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 5 PLAYER Blue PLACED JLTTB AT 2 0 180 TIGER 1");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 6 PLACE TLLT");
                            
                            Thread.sleep(2000);
                            
                            out.println("GAME A MOVE 6 PLAYER Blue PLACED TLLT- AT 0 -1 180 CROCODILE");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME A OVER PLAYER Red <red_score> PLAYER Blue <blue_score>");
                            
                            out.println("GAME B OVER PLAYER Red <red_score> PLAYER Blue <red_score>");
                            
                            //////////////////////////////////////////////////////////
                            out.println("END OF ROUND 1 OF 1");
                            
                            out.println("END OF CHALLENGES");
                            
                            out.println("THANK YOU FOR PLAYING! GOODBYE");
                            //////////////////////////////////////////////////////////
                            
                            
                            out.println("END OF ROUND 1 OF 2 PLEASE WAIT FOR THE NEXT MATCH");
                            
                            out.println("PLEASE WAIT FOR THE NEXT CHALLENGE TO BEGIN ");
                          
                            out.println("BEGIN ROUND 2 of 2");
                            
                            Thread.sleep(2000);
                            
                            out.println("YOUR OPPONENT IS PLAYER Red");
                            
                            out.println("STARTING TILE IS TLTJ- AT 0 0 0");
                            
                            //out.println("THE REMAINING 23 tiles are [ TLLT- JJJJX TLLT- LLJJ- JJJJX LLLL- JLJL- TJTT- JLLL- LLJJ- LJLJ- TLJT- TLJTP JLTTB TLTJD TTLTT- LLLL- TLTJ- LJJJ- LJTJD JJJJX JLLL- JJJJX ]");
                            
                            out.println("THE REMAINING 12 tiles are [ LLLL- JJJJX TLLT- LLJJ- JJJJX LLLL- JLJL- TJTT- JLLL- LLJJ- LJLJ- TLJT- ]");
                            
                            out.println("MATCH BEGINS IN 15 SECONDS");
                            
                            Thread.sleep(2000);
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 1 PLACE LLLL-");
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 1 PLAYER Red PLACED LLLL- AT 1 0 0 NONE");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 2 PLACE JJJJX");
                            
                            Thread.sleep(2000);
                            
                            out.println("MAME A MOVE 2 PLAYER Red PLACED JJJJX AT -1 0 0 NONE");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 3 PLACE TLLT-");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 3 PLAYER Red PLACED TLLT- AT 0 -1 0 NONE");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 4 PLACE LLJJ-");
                            
                            Thread.sleep(2000); //ACTUAL BREAK - 15++ SECONDS
                            
                            out.println("GAME A MOVE 4 PLAYER Red PLACED LLJJ- AT 0 -2 0 NONE");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 5 PLACE JJJJX");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 5 PLAYER Red PLACED JJJJX AT -1 -2 0 NONE");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 6 PLACE LLLL-");
                            
                            Thread.sleep(2000);
                            
                            out.println("GAME A MOVE 6 PLAYER Red PLACED TLLT- AT 1 -2 0 NONE");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 7 PLACE JLJL-");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 7 PLAYER Red PLACED JLTTB AT -1 -2 0 NONE");
                            
                            out.println("MAKE YOUR MOVE IN GAME B WITHIN 1 SECOND: MOVE 8 PLACE TJTT-");
                            
                            Thread.sleep(2000);
                            
                             out.println("GAME B MOVE 8 PLAYER Red PLACED TJTT- AT 0 1 0 NONE");
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 7 PLACE JLJL-");
                            
                            Thread.sleep(2000);
                            
                            out.println("<REPEATS OUR MOVE>");
                            
                            out.println("GAME B MOVE 7 PLAYER Red PLACED JLTTB AT -1 -2 0 NONE");
                            
                            //....

                            
                            out.println("GAME A OVER PLAYER Red <red_score> PLAYER Blue <blue_score>");
                            
                            out.println("GAME B OVER PLAYER Red <red_score> PLAYER Blue <red_score>");
                            
	                	}  
	                	
	                		                	                	
	                }
	               
	                
	            }
	        }
	        
	        finally 
	        {
	            listener.close();
	        }
	    }
}
	

