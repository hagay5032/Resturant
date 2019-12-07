package clientServerResturant;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * HungryClient class is for creating a client to order from a restaurant.
 * This program manage the frame of the GUI ,And create a socket to connect 
 * the server, Afterwards send the server a packet with "GET_MENU" that should
 * cause the server to deliver back the menu. Then build the menu panel with 
 * the given info that come back from the server.
 * 
 * @author Hagay Enoch
 * @version 4.12.19
 *  
 */
public class Client 
{
	public static void main(String[] args)
	{	 
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
			    JFrame frame = new JFrame("Hungry Client");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(1365, 745);
				try 
				{
					new MenuGUI(frame);
					frame.getContentPane().revalidate();
					frame.setVisible(true);
			    } 			   	
			    catch(Exception e) 
				{
			    	System.err.println("System Error, Please contact your "
			    			+ "supplier.\nProgram exit.\n"+e);
			    }	
			}
		});
	}
}

