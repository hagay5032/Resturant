package clientServerResturant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingWorker;

/**
 * This class is for a retrieving the menu from the server as a background 
 * process.
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 *  
 */
public class GetMenuWorker extends SwingWorker<Void, Void>
{
	private final int MAX_DISH_NAME_LEN = 40; // max length for dish name
	private final int MAX_DESCRIPTION_LEN = 150; // max length for description
	private final int MAX_TRY = 3; // max attempts to get a connection
	private final int MAX_SEC = 10; // max seconds to wait if socket freeze
	
	private Socket socket = null;  		// the socket connection
    private String host = "localhost";	// the host IP address
	private BufferedReader in = null;   // the output stream of the socket
	private PrintWriter out = null; 	// the output stream of the socket
	private MenuGUI continer = null;	// reference to the MenuGUI panel.
	private ArrayList<DishPanel> foodList 
			= new ArrayList<DishPanel>(); // the food list of the MenuGUI
	private LoadingPanel load = null; // loading panel for the  info from server

	/**
	 * Constructor for GetMenuWorker.
	 * @param _continer is the reference to menu panel.
	 */
	public GetMenuWorker(MenuGUI _continer)
	{
		continer = _continer;
		foodList = _continer.getFoodList();
	}
	
    @Override
    public Void doInBackground()
    {
    	// Initializing the loading panel
		load = new LoadingPanel("loading...");
		continer.add(load);
		load.setSize(400, 400);
		load.setBounds(428, 180, 500, 400);
 
		// trying to establish a socket with the server
    	tryConnect();
    	
		//#########################################################
		//################# For Debug purpose #####################
		// dnsAttack();
		//#########################################################
		//#########################################################
    	
    	return null;
    }
    
    /**
     * This method is for counting the number of attempts to connect to the 
     * server and retrieve the menu. In case there was MAX_TRY attempts or
     *  the user wait more than MAX_SEC seconds, then the process stop and 
     *  display an error.
     */
	public void tryConnect()
	{
		int tryCounter = 0;
		while(true) 
		{
			try 
			{
				getMenuFromServer();
				continer.remove(load);	// removing the loading panel
	    		continer.setMenu();	// new thread to set the menu that Obtained.
	    		break;
	    	}
	    	catch(Exception e)
	    	{
	    		// if there was MAX_TRY attempts or the user wait more than
	    		// MAX_SEC seconds, then the process stop and display an error
	    		String message =  e.getMessage();
	    		if(tryCounter >= MAX_TRY|| ( message != null &&
	    				(message.equals("Socket server not respond") )))
	    		{
	    			load.hold();
	    			break;
	    		}
	    		System.err.println(e);
	    		tryCounter++;
	    		sleepAwhile(1000); // sleep a second
	    	}
		}
	}
    
    
    /**
     * This method asking the server for the menu, And if success it construct
     * the dishes list reference.
     * @throws Exception if any exception happened
     */
	public void getMenuFromServer() throws Exception
	{
		String line = null; 		
		
    	// reading the ip and port from the configuration file
    	URL url = Server.class.getResource("clientConfig");
    	Scanner scan = new Scanner(new File(url.getPath()));
    	int port = scan.nextInt();
    	host = scan.next();
    	scan.close();
		
		socket = new Socket( InetAddress.getByName(host), port);
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	     
	    System.out.println("After connection");
	
	    // sending request to the server - asking the menu.
		out.println("GET_MENU"); 
	
		int tryRecieve = 0;
		while(in.ready() == false) // there is no input from server
		{
			if(tryRecieve >= MAX_SEC) 
			{
				throw new IOException("Socket server not respond");
			}
			else // waiting one second
			{
				tryRecieve++;
				sleepAwhile(1000);
			}
		}
		
		line = in.readLine(); // read hash symbol
		foodList.clear();
		
		while(line != null) 
		{
			int code = 0;				// holds the dish code 
			String dishTitle = null;	// holds the dish title 
			String description = null;	// holds the dish description 
			int price = 0;				// holds the dish price
			
			line = in.readLine(); 
			
			code = Integer.parseInt(line);
			dishTitle = in.readLine();			
			description = in.readLine();
			price = Integer.parseInt(in.readLine());
			
			// checking the legality of the input
			if(illegalInput(dishTitle, description, price))
			{
				line = in.readLine();
				continue; // input illegal then skip this dish
			}
			
			foodList.add(new DishPanel(code, dishTitle, 
											description, price));
			
			line = in.readLine();
			
			//search for a hash symbol (represent a start point of a dish)
			while(line != null && !line.equals("#")) 
			{
				line = in.readLine(); // read hash symbol
			}
		}
		
		// if there wasn't any legal dishes then throw exception
		if(foodList.isEmpty()) 
		{
			System.err.println("Input from server is illegal.");
			throw new Exception("Input from server is illegal.");
		}
		
		out.close();
	    in.close();
	    socket.close();
	}
	
	/**
	 * Legality check for the input from the server.
	 * Return true if all terms are legal. false otherwise.
	 * @param dishTitle holds the dish title
	 * @param description holds the dish price
	 * @param price holds the dish price
	 * @return true if all terms are legal. false otherwise.
	 */
	private boolean illegalInput(String dishTitle, 
			String description, int price) 
	{		
		if(dishTitle.length() >= MAX_DISH_NAME_LEN) 
		{
			System.err.println("Dish name is to big - " + dishTitle);
			return true;
		}
		if(description.length() >= MAX_DESCRIPTION_LEN) 
		{
			System.err.println("Dish description is to big - " + description);
			return true;
		}
		if(price < 0) 
		{
			System.err.println("Dish price is negative for - " + dishTitle);
			return true;
		}
		
		return false;
	}

    /**
     * This method is cause this thread to sleep a time given as parameter.
     * @param milliSeconds is the time to sleep in milli-seconds.
     */
    private void sleepAwhile(int milliSeconds){
		try
		{
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Method for the programmer to simulate a DNS attack
	 * @throws Exception
	 */
	private void dnsAttack() throws Exception
	{
		int dnsAttackCount = 0; // counter for the threads
		while(dnsAttackCount < 2001) 
		{
			sleepAwhile(2);
			new ThreadDns(dnsAttackCount).start();
			dnsAttackCount++;
			if(dnsAttackCount % 20 == 0)
				System.out.println();
		}
	}
	/**
	 * This class is a a simple thread that create a socket with the server, 
	 *  And then goes to sleep a while, that create many sockets open in the
	 *  server.
	 *  
	 * @author Hagay Enoch
	 *
	 */
	class ThreadDns extends Thread{
		
		public int num = 0; // the number of the thread 
		
		/**
		 * Constructor for ThreadDns
		 * @param _num is the number of thread.
		 */
		public ThreadDns(int _num) 
		{
			num = _num;
		}
	    
		@Override
		public void run() 
		{
	    	try 
	    	{
	        	// reading the ip and port from the configuration file
	        	URL url = Server.class.getResource("clientConfig");
	        	Scanner scan = new Scanner(new File(url.getPath()));
	        	int port = scan.nextInt();
	        	host = scan.next();
	        	scan.close();
	        	
	    	    Socket Dsocket = new Socket( InetAddress.getByName(host), port);
	    	    PrintWriter Dout = new PrintWriter(
	    	    						Dsocket.getOutputStream(), true);
	    	    BufferedReader Din = new BufferedReader(new InputStreamReader(
	    	    		Dsocket.getInputStream()));
	    	  	   
	    	    // sleep 2 minutes creating overflow at the server
	    	    sleepAwhile(120000);
	    		Dout.println("GET_MENU"); 
	    		
	    		Dout.close();
	    	    Din.close();
	    	    Dsocket.close();
			} 
	    	catch (Exception e) 
	    	{
				 System.out.print(num + "\t");
				//e.printStackTrace();
			}
	    }
	}
}


