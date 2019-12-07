package clientServerResturant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * MenuGUI class is for creating a menu panel to order from a restaurant.
 * This class manage the request of the menu from the server by a private class.
 * And after the client press the "Order" button it will prompt the OrderPanel.
 * 
 * The final keyword - n the class declaration is to prevent an inheritance 
 * from this class, because this class constructor fire a thread, and hence 
 * can cause unexpected problems at the subclasses.
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 *  
 */
public final class MenuGUI extends JPanel 
{
	private int numDishes = 0; 			// the number of dishes
	private JLabel lblTitle;			// title label
	private JPanel menuTitle = null;	// title panel for the menu
	private JButton cmdOrder = null;; 	// button to submit the order
	private BufferedImage image = null; // the background image for the menu
	private JFrame framePtr; 	// reference to the frame that holds this panel
	private JScrollPane jsc = null; // scroll for the frame
	private RoundedPanel ordrBtnPnl = null;  // panel for the order button 
	private volatile ArrayList<DishPanel> foodList 
					= new ArrayList<DishPanel>(); // dishes list for the menu
	private final int minOrdrHieghtPnl = 500; // minimum height for the order panel
	
	/**
	 * Constructor for MenuGUI
	 * @param framePointer - a reference to the frame that holds this MenuGUI.
	 * @throws Exception if any parts
	 */
	public MenuGUI(JFrame framePointer) throws Exception
	{
		framePtr = framePointer;
		
		setLayout(null);
		
		//read the background picture
    	URL url = getClass().getResource("images/menuPic.jpg");
		image = ImageIO.read(new File(url.getPath()));
		if(image == null)
		{
			System.err.println("Problem loading background image picture file");
			 throw new IOException();
		}
		
		// setting the title
		menuTitle = new JPanel();
		lblTitle = new JLabel("Welcome To Italiano Resturant!"); 
		Font titleFont = new Font("Arial",Font.ITALIC+Font.BOLD, 70);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(titleFont);
		menuTitle.add(lblTitle);
		menuTitle.setBackground(Color.BLACK);
		add(menuTitle);
		menuTitle.setBounds(0, 0, 1365, 100);
		
		/* set a new Thread out of the GUI thread -
		 * 	to get the menu from the server 
		 */
		new GetMenuWorker(this).execute();
		
		// setting the order button
 		ordrBtnPnl = new RoundedPanel();
 		ordrBtnPnl.setLayout(null);
 		ordrBtnPnl.setBackground(new Color(255, 204, 51));
 		ordrBtnPnl.setSize(250, 82);
		cmdOrder = new JButton("Order"); 
		cmdOrder.setFont(new Font("Arial",Font.PLAIN + Font.BOLD, 14));

		ordrBtnPnl.add(cmdOrder);
		cmdOrder.setSize(200, 50);
		cmdOrder.setBounds(25, 15, 200, 50);
		add(ordrBtnPnl);
		ordrBtnPnl.setVisible(false);
		
		// set up a scroll and set the position on top
		jsc = new JScrollPane(this);	
		jsc.getVerticalScrollBar().setUnitIncrement(16);
		framePtr.add(jsc);
		framePtr.getContentPane().revalidate();
		scrollUp();
	}
	
	/**
	 * This method is for getting the scroll up to the top of the panel
	 */
	private void scrollUp() 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{	
			 public void run() 
			 {
			      // Here, we can safely update the GUI
			      // because we'll be called from the
			      // event dispatch thread
			    	if(jsc != null)
						jsc.getViewport().setViewPosition(
															new Point(0, 0) );
			    	framePtr.getContentPane().revalidate();
			    	framePtr.getContentPane().repaint();
			 }
		});
	}
	
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		for(int i = 0; i < Math.ceil( ((double)numDishes + 2) / 4); i++)
	        g.drawImage(image, 0, i*800, this);
	}

	/**
	 * This private class ButtonListener is for the order button.
	 * <p> If all the conditions are legal then the order panel will appear.
	 */
	private class ButtonOrderListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int howManyDishes = 0;
			int countQun = 0;
			for(int i = 0; i < numDishes; i++) 
			{
				countQun = (foodList.get(i)).getQuantity(); 
				if(countQun != 0) 
					howManyDishes += countQun;
			}

			if(howManyDishes == 0) // there wasn't any dish selection.
			{
				JOptionPane.showMessageDialog(null, "You Must Choose "
										+ "At Least One Food To Order...");
				return;
			}
			else 
			{
				removeAll(); 
				revalidate();
				repaint();
				
				// setting the frame size to adjust the order panel
				setPreferredSize(new Dimension(1100, 
												(numDishes + 3) * 30 + 300 ));
		
				OrderPanel ordPnl = new OrderPanel(foodList, framePtr);
				
				add(ordPnl);
				
				// setting the size of the panel, this is ratio to the dish 
				//list length 
				if (minOrdrHieghtPnl > (numDishes + 3) * 30 +  120  )
					ordPnl.setBounds(250, 120, 700, minOrdrHieghtPnl );	
				else 
					ordPnl.setBounds(250, 120, 700,
										(numDishes + 3) * 30 +  120 );
				
				if(jsc != null)
					jsc.getViewport().setViewPosition(new Point(0, 0));
				
				lblTitle.setText("Order Description");
				add(menuTitle);	
			}	
		}
	}
	
	/**
	 * This method is for edit the menu panel and adding it all the dishes,
	 * And it will append outside the GUI thread.  
	 */
	public void setMenu() 
	{
		numDishes = foodList.size();
		new setMenuWorker().execute();
	}
	
	/**
	 * This class is manage the attachment of the dish list to the panel.
	 * It does it outside the GUI thread.
	 */
	class setMenuWorker extends SwingWorker<Void, Void>
	{
		
		@Override
	    public Void doInBackground() throws Exception 
	    {
			backgroundGUI();
			return null;
	    }
	
	    @Override
	    public void done()
	    {
	    	framePtr.getContentPane().revalidate();
	    	framePtr.getContentPane().repaint();	
	    }
		
	    /**
	     * This method adds the dishes to the menu panel. 
	     * And set the order button location.
	     */
	    public void backgroundGUI() 
	    {
	    	for(int i = 0; i < foodList.size(); i++) 
	 		{	
	 			foodList.get(i).setBounds(10,(1+ i)*150, 750, 120);
	    		
	 			add(foodList.get(i));
	  			framePtr.getContentPane().revalidate();
		    	framePtr.getContentPane().repaint();
		    	try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
	    	 
			// setting the frame size to adjust the this menu panel
	 		setPreferredSize(new Dimension(1100, 150 * (numDishes + 3)));
			
	 		// setting the order button
			ButtonOrderListener lis = new ButtonOrderListener ();
			cmdOrder.addActionListener(lis);
			ordrBtnPnl.setBounds(540, 150*(numDishes + 1), 250, 82);
			ordrBtnPnl.setVisible(true);
		}
    }
	
	/**
	 * Return the food list.
	 * @return the food list.
	 */
	public  ArrayList<DishPanel> getFoodList()
	{
		return foodList;
	}
}


