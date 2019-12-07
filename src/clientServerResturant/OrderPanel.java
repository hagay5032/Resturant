package clientServerResturant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;


/**
 * This class is for a OrderPanel objects, creates a order panel with the 
 * <p> description of the order.
 * 
 * @author Hagay Enoch
 * @version 6.12.19
 *  
 */
public class OrderPanel extends JPanel
{
	private GridBagEx orderSummaryPnl = null; // panel summary for the order
	private JButton cmdSubmit = null;	// button for submit the order
	private JButton cmdCancel = null;	// button for cancel the order
	private JTextField fullNameTxt = null; // text area for insert user name
	private JTextField addrTxt = null; // text area for insert user address
	private JTextField telTxt = null; // text area for insert user telephone
	private JLabel lblFullName = null; // label 'Name'
	private JLabel lblAddr = null; // label address
	private JLabel lblTel = null; // label telephone
	private JLabel lblTotalSum = null; // label for the total sum of the order
	private JFrame framePtr = null; /* reference to the frame container of 
										this panel*/
	private String resultToServer = null;  /* string that will be the 
												description of the order to 
												send to	the server */
	private int howManyLinesInesultServer = 0; /* holds the number of lines in 
										      'resultToServer' */
	
	/**
	 * Constructor for OrderPanel objects.
	 * @param foodList - is a reference to the foodList created earlier.
	 * @param framePointer - is a reference to the frame that contain this panel
	 */
	public OrderPanel(ArrayList<DishPanel> foodList, JFrame framePointer) 
	{
		cmdSubmit = new JButton("Submit & Pay");
		cmdCancel = new JButton("Cancel");
		framePtr = framePointer;
		howManyLinesInesultServer = 0;
		
		setLayout(null);
		
		Font orderFont = new Font("Arial", Font.ITALIC+Font.BOLD, 18);
		
		// creates a JLabels & text-areas for insert the user Name, address and
		// telephone number.
		lblFullName = new JLabel("Full Name: ");  
		lblAddr = new JLabel("Address: ");
		lblTel = new JLabel("Telephone: "); 
		lblTotalSum = new JLabel("Total Sum: "); 
		lblTel.setFont(orderFont );
		lblAddr.setFont(orderFont );
		lblFullName.setFont(orderFont );
		
		fullNameTxt = new JTextField();
		addrTxt = new JTextField();
		telTxt = new JTextField();
		telTxt.setFont(orderFont );
		addrTxt.setFont(orderFont );
		fullNameTxt.setFont(orderFont );
		
		add(lblFullName);
		add(lblAddr);
		add(lblTel);
		add(fullNameTxt);
		add(addrTxt);
		add(telTxt);
		
		lblFullName.setBounds(10,10, 100, 30);
		fullNameTxt.setBounds(110,10, 150, 30);
		
		lblAddr.setBounds(10,45, 200, 30);
		addrTxt.setBounds(110,45, 150, 30);
		
		lblTel.setBounds(300,10, 200, 30);
		telTxt.setBounds(420,10,150, 30);
		
		// initialize the summary panel
		orderSummaryPnl = new GridBagEx(foodList);
		add(orderSummaryPnl);
		
		// process the order by going through 'food-list' array.
		int count = 0;
		int totalSum = 0; 		// holds the total sum of the order
		resultToServer = "";  	// holds the answer to the server 
		DishPanel tmpDish = null; 
		for(int i = 0; i < foodList.size(); i++)
		{
			tmpDish = foodList.get(i);
			if(tmpDish.getQuantity() != 0 )
			{
				// add this dish to the summary panel
				orderSummaryPnl.addDish(tmpDish, i);
				
				totalSum += tmpDish.getQuantity() * tmpDish.getPrice();
				
				// adding a a dish request lines that eventually will be 
				// send to the server 
				resultToServer += tmpDish.getCode() + "\n" + 
									tmpDish.getQuantity() + "\n";
				howManyLinesInesultServer += 2;
				count++;
			}
		}

		orderSummaryPnl.setBounds(10,100, 690, (count + 3) * 30 );
		
		AbstractBorder brdrLeft = new TextBubbleBorder(
										Color.black, 1, 20, 0);
		
		
		
		ButtonOrderListener lis = new ButtonOrderListener();
		
		// initialize the submit command button 
		cmdSubmit.setFont(new Font("Arial",Font.PLAIN + Font.BOLD, 14));
		cmdSubmit.addActionListener(lis);
		cmdSubmit.setBorder(brdrLeft);
		cmdSubmit.setPreferredSize(new Dimension(100, 52));
		
		// initialize the cancel command button 
		cmdCancel.setFont(new Font("Arial",Font.PLAIN + Font.BOLD, 14));
		cmdCancel.addActionListener(lis);
		cmdCancel.setBorder(brdrLeft);
		cmdCancel.setPreferredSize(new Dimension(100, 52));
		
		// creating a panel for the 2 buttons - 'cmdSubmit' & 'cmdCancel'
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		pnl.add(cmdSubmit);
		pnl.add(cmdCancel);
		
		// creating a total sum cost for the order 
		lblTotalSum.setText(lblTotalSum.getText() + totalSum);
		lblTotalSum.setFont(orderFont);
		lblTotalSum.setPreferredSize(new Dimension(200, 52));
		
		// adding 'lblTotalSum' & 'pnl' to the order summary panel
		orderSummaryPnl.Summarize(pnl,lblTotalSum);		
	}
	
	/**
	 * This private class ButtonListener is for the 2 control buttons
	 *  'Set' and 'Clear'.
	 * <p> The "Set" button is setting all the valid digits that was entered 
	 * before - to be an unchanged digits with different font for recognizing.
	 * <p> The "Clear" is clearing the board. And return the 
	 * board to the starting position.
	 */
	private class ButtonOrderListener implements ActionListener 
	{		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == cmdSubmit) 
			{
				if(allFieldsAreValid())
				{
					// create the message to the server
					String msgToSrvr = "";
					
			        howManyLinesInesultServer += 3;
			        
			        // init the message
			        msgToSrvr = howManyLinesInesultServer + "\n" + 
			        			fullNameTxt.getText() + "\n" + 
			        			addrTxt.getText() + "\n" +
			        			telTxt.getText()+ "\n" +
			        			resultToServer;
			        
			        sendOrder(msgToSrvr);
				}
				else 
				{
					JOptionPane.showMessageDialog(null, 
							"You Must Fill All Fields On Top");
				}
			}
			else if (e.getSource() == cmdCancel) 
			{
				int input = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to cancel this order?" ,
						"cancel the order" , JOptionPane.YES_NO_OPTION);
				if(input == 0) // yes - to cancel the order
					 resetNewMenuPanel();
				else if (input == 1)// no - to cancel the order
					return;
			}
		}
	}
	
	/**
	 * Send a message to the server represent the order of this client.
	 * And it a thread to run outside the GUI thread.
	 * @param msg the message to send to the server represent the order of
	 * this client.
	 */
	public void sendOrder(String msg) 
	{
		new SendOrderWorker(this, msg).execute();
	}
	
	/**
	 * This method set a new menu panel on the frame.
	 */
	public void resetNewMenuPanel() 
	{
	    framePtr.getContentPane().removeAll();	    
	    revalidate();
		framePtr.repaint();
	
		try
		{	
			new MenuGUI(framePtr);
		} 			   	
	    catch(Exception e) 
		{
	    	System.err.println("Error" + e);
	    }
	}
	
	/**
	 * Return true if all the input parameters are valid. false otherwise.
	 * @return true if all the input parameters are valid. false otherwise.
	 */
	public boolean allFieldsAreValid() 
	{
		//TODO - Make it this program more specific. And check more edge cases.
		if (telTxt.getText().equals("") || 
				addrTxt.getText().equals("") ||
		    	 	fullNameTxt.getText().equals(""))
		{		
		   return false;
		}
		else 
		{
		    return true;
		}
	}
}
 
 
