package clientServerResturant;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

/**
 * DishPanel class is for a MenuGUI objects, creates a convenient panel 
 * to show the dish details. 
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 *  
 */
public class DishPanel extends RoundedPanel
{	 
	 
	private int code; 			// the code of this dish
	private String ttl; 		// the name of this dish
	private String description; // the description of this dish
	private int price; 			// the price of this dish
	private QuantitySelect quntPnl;	// the quantity panel for this dish

	
	/**
	 * Constructor for a DishPanel.
	 * 
	 * @param _code the code of this dish.
	 * @param _ttl the name of this dish.
	 * @param _description the description of this dish.
	 * @param _price the price of this dish.
	 * 
	 */
	public DishPanel (int _code, String _ttl, String _description, int _price) 
	{
		
		code = _code;
		description  = _description; 
		ttl = _ttl;
		price = _price;
		
		this.setLayout(null);
		
		// creates a JLabel that will contain the title	
		JLabel lblPrices = new JLabel("Price:    " + price);
		lblPrices.setFont(new Font("Arial",Font.PLAIN, 18));
		lblPrices.setForeground(Color.BLACK);
		add(lblPrices);
		lblPrices.setBounds(600, 10, 100, 25);
		
		// creates a JLabel that will contain the title	 
		JLabel lblTitle = new JLabel(ttl); 
		Font titleFont = new Font("Arial",Font.ITALIC+Font.BOLD, 18);
		Font txtFont = new Font("Arial",Font.PLAIN+Font.BOLD, 14);
		@SuppressWarnings("unchecked")
		Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) 
												titleFont.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		lblTitle.setFont(titleFont.deriveFont(attributes));
		lblTitle.setForeground(Color.BLACK);
		add(lblTitle);
		lblTitle.setBounds(10, -30, 500, 100);
		
		// creates a rounded boarder for the description area 
		AbstractBorder brdrLeft = new TextBubbleBorder(Color.orange,1,20,0);
		setBackground(new Color(255,204,51));
				
		// add the description of the dish
		JTextArea jtxt = new JTextArea(description);
		jtxt.setFont(txtFont);
		jtxt.setLineWrap(true);
		jtxt.setWrapStyleWord(true);
		jtxt.setEditable(false);
		jtxt.setBackground(new Color(255,255,153));
		jtxt.setBorder(brdrLeft);
		add(jtxt);
		jtxt.setBounds(15, 40, 400, 68);
		
		// add quantity selection panel
		quntPnl = new QuantitySelect(); 
		add(quntPnl);
		quntPnl.setBounds(550, 50, 190, 50);
	}
	 
	
	/**
	 * Return the name of this dish. 
	 * @return the name of this dish.
	 */
	public String getTtl() 
	{ 
		return ttl; 
	}
	
	/**
	 * Return the quantity of this dish.
	 * @return the quantity of this dish.
	 */
	public int getQuantity() 
	{ 
		return quntPnl.getQuantity();	
	}
	
	/**
	 * Return the description of this dish.
	 * @return the description of this dish.
	 */
	public String getDescription() 
	{
		return description;
	}

	/**
	 * Return the price of this dish.
	 * @return the price of this dish.
	 */
	public int getPrice() 
	{
		return price;
	}

	/**
	 * Return the code of this dish.
	 * @return the code of this dish.
	 */
	public int getCode() 
	{
		return code;
	}
}
 
 
 