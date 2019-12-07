package clientServerResturant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class is for a retrieving the menu from the server as a background 
 * process.
 * 
 * @author Hagay Enoch
 * @version 5.12.19
 *  
 */
public class SendOrderWorker extends SwingWorker<Void, Void>
{
	private final int MAX_TRY = 3; 		// max attempts to get a connection
	private final int MAX_SEC = 10; 	// max seconds to wait if socket freeze
	private String message = null; 		// the message to the server	
	private Socket socket = null;  		// the socket connection
    private String host = "localhost";	// the host IP address
	private BufferedReader in = null;   // the output stream of the socket
	private PrintWriter out = null; 	// the output stream of the socket
	private OrderPanel continer = null;	// reference to the OrderPanel panel.
	private LoadingPanel load = null; // loading panel for the  info from server

	/**
	 * Constructor for GetMenuWorker.
	 * @param _continer is the reference to menu panel.
	 * @param _foodList is the reference to food-List array of the menu panel. 
	 */
	public SendOrderWorker(OrderPanel _continer, String msg)
	{
		message = msg;
		continer = _continer;
	}
	
    @Override
    public Void doInBackground()
    {
    	// Initializing the loading panel
		load = new LoadingPanel("Waiting for approvale...");
		continer.removeAll();
		continer.setOpaque(false);
		continer.add(load);
		load.setSize(400, 400);
		load.setBounds(178, 60, 500, 400);
		continer.revalidate();
		continer.repaint();
		
		// trying to establish a socket with the server
    	tryConnect();
    	
    	return null;
    } 
    
    @Override
    public void done()
    {
    	JOptionPane.showMessageDialog(null, 
				"Order Approved, \nAnd Will Be Arrive"
				+ " In The Next Hour.\nCon Appetito ");
    	continer.resetNewMenuPanel(); // set a new menu panel
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
				sendOrdr();
				continer.remove(load);	// removing the loading panel
	    		break;
	    	}
	    	catch(Exception e)
	    	{
	    		// if there was MAX_TRY attempts or the user wait more than
	    		// MAX_SEC seconds, then the process stop and display an error.
	    		String message =  e.getMessage();
	    		if(tryCounter >= MAX_TRY || ( message != null &&
	    				(message.equals("Socket server not respond") )))
	    		{
	    			load.hold();
	    			break;
	    		}
	    		tryCounter++;
	    		sleepAwhile(1000); // sleep a second
	    	}
		}
	}
	
    /**
     * This method is cause this thread to sleep a time given as parameter.
     * @param milliSeconds is the time to sleep in milli-seconds.
     */
    private void sleepAwhile(int milliSeconds)
    {
		try
		{
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Sends the order summary to the server.
     * @throws Exception if any unexpected exception occurred it propagate 
     * 			the exception up.  
     */
    private void sendOrdr() throws Exception
    {

	    	socket = new Socket( host, 3334);
	        out = new PrintWriter(socket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(
	        						socket.getInputStream()));
	        
	        // inform the server that it has an order to deliver
	        out.println("SENT_ORDER");
	        
	        // sent order to the server
	        out.println(message);
			
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
	        
			// waiting & reading the server response - 
			// should be order acknowledgement
	        String input = in.readLine(); 
	        	
	        out.close();
	        in.close();
	        socket.close();

	        if (!input.equals("GOT_THE_ORDER"))
	    		throw new Exception();
    }
}


