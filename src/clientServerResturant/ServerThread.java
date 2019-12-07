package clientServerResturant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;


/**
 * This class is for managing a TCP connection request from a client.
 * This program receive a message from a client and process it. 
 * Any clients that connect to the server need that the first line of his packet
 * will be one of the two options - 
 * 1) "GET_MENU"- tells the server to send the menu back to the client.
 * 2) "SEND_ORDER"- tells the server that the client is going to send an order. 
 * In case of option 1 - then the server read the "menu" file and send it to 
 * the client line-by-line.
 *
 *In case of option 1 - then the server is reading the order it get from
 * the client line-by-line. The first line after the "SEND_ORDER" is the number 
 * of lines in the order, afterwards suppose to come 3 more lines of the client 
 * details. Next come all the dishes that the client choose, each dish is 
 * represented by two lines-
 * <p>1. the code of the dish.
 * <p>2. the quantity of this dish.
 *  
 * @author Hagay Enoch
 * @version 8.12.19
 *  
 */
public class ServerThread extends Thread
{
	
    private Socket socket = null; 	// the socket connection 
    private PrintWriter out;      	// the output stream of the socket
    private BufferedReader in;		// the output stream of the socket
    private Scanner scan;			// scanner for reading the menu
    private int conNum = 0;			// it is the number of clients that connect 
    								// to the server before 
    
    public ServerThread(Socket s, int _conNum) 
    {
    	conNum = _conNum;
        socket = s;
        try 
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(IOException e)
        { 
        	e.printStackTrace();
    	}
    }
    
    public void run()
    {
        String input;
        
        try 
        {    
            input = in.readLine();
            if(input != null && input.equals("GET_MENU")) 
            { 
            	//the socket asks for the menu
            	
            	URL url = getClass().getResource("menu");
            	scan = new Scanner(new File(url.getPath()));
            	
            	System.out.println("### Server send ###\n");
        		
            	//###########################################################
            	//#########  Adding delay time for debug ####################
            	//sleepAwhile(1000);
        		//###########################################################
            	//###########################################################
            	
            	while(scan.hasNextLine()) 
            	{
            		String str = scan.nextLine();
            		out.println(str);
            		System.out.println(conNum +"   " + str);
            	}
            }
            else if(input != null && input.equals("SENT_ORDER")) 
            { 
            	//the socket sends the order
            	int HowManyLines = Integer.parseInt(in.readLine());
            	String str;
            	
            	System.out.println("### Server recieve ###\n");
            	
            	for(int i = 0; i < HowManyLines; i++)
            	{
            		str = in.readLine();
					//*********************************************************
					// NOT REQUIRED IN THE TASK- But essential for a
            		// real/working program  - Here needed processing the order
        			// that come from the client - line by line 
					//*********************************************************	
            		System.out.println(str);
            	}
            	
            	// send an acknowledgment for the receiving of the order
            	out.println("GOT_THE_ORDER");
            }
            in.close();
            out.close();
            socket.close();
         }
         catch(IOException e) 
         { 
        	 e.printStackTrace(); 
        	 System.out.println(conNum);
         }
    }

    /**
     * This method is for debugging and research the program.
     * @param milliSeconds is the time to sleep in milli- seconds.
     */
    private void sleepAwhile(int milliSeconds){
		try
		{
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
