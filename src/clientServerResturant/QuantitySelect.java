package clientServerResturant;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

/**
 * QuantitySelect class is for given the user a convenience selection interface 
 * for a desired quantity , In this case for a dish.
 * 
 * @author Hagay Enoch
 * @version 6.12.19
 *  
 */
public class QuantitySelect extends RoundedPanel
{
	
	private JButton btnUp = null; // button to increase 1 from quantity
	private JButton btnDown = null; // button to decrease 1 from quantity
	private JTextArea JtxtQuantity = null; // the quantity number label
	private int quantity = 0; // the quantity number
	
	/**
	 * QuantitySelect class convenient GUI panel letting the user to select
	 * quantity. 
	 * 
	 * @author Hagay Enoch
	 * @version 203089917
	 *  
	 */
	public QuantitySelect()
	{
		
		this.setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		setSize(190, 50);
		
		// setting increase\decrease buttons with icons and mouse hover effects.
		URL urlUp = getClass().getResource("images/buttonUp.png");
		URL urlDown = getClass().getResource("images/buttonDown.png");
		URL urlUpInside = getClass().getResource("images/UpInside.png");
		URL urlDownInside = getClass().getResource("images/DownInside.png");
    	ImageIcon iconUp = new ImageIcon(urlUp.getPath());
    	ImageIcon iconDown = new ImageIcon(urlDown.getPath());
    	ImageIcon iconUpInside = new ImageIcon(urlUpInside.getPath());
    	ImageIcon iconDownInside = new ImageIcon(urlDownInside.getPath());
    	
    	btnUp = new JButton(iconUp);
		btnDown = new JButton(iconDown);
		
		btnUp.setOpaque(false);
		btnUp.setContentAreaFilled(false);
		btnUp.setBorderPainted(false);
		
		btnDown.setOpaque(false);
		btnDown.setContentAreaFilled(false);
		btnDown.setBorderPainted(false);
		
		IncDecQntListener lis = new IncDecQntListener();

		btnUp.addActionListener(lis);
		btnDown.addActionListener(lis);
		
		IconChange btnDownHover = new IconChange(btnDown,
				iconDown, iconDownInside);		
		IconChange btnUpHover = new IconChange(btnUp,
						iconUp, iconUpInside);
		
		btnUp.addMouseListener(btnUpHover);
		btnDown.addMouseListener(btnDownHover);
				
		add(btnUp);
		add(btnDown);
		btnUp.setBounds(100, 0, 30, 30);
		btnDown.setBounds(100, 18, 30, 30);
		
		// set the text quantity number
		JtxtQuantity = new JTextArea(" 0");
		AbstractBorder brdrLeft = new TextBubbleBorder(
				Color.LIGHT_GRAY, 1, 10, 0);
		JtxtQuantity.setBorder(brdrLeft);
		add(JtxtQuantity);
		JtxtQuantity.setBounds(130, 12, 50, 25);
		JtxtQuantity.setFont(new Font("Arial", Font.PLAIN, 18));
		JtxtQuantity.setEditable(false);
		
		// set the quantity label
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Arial", Font.PLAIN , 16));
		lblQuantity.setForeground(Color.BLACK);
		add(lblQuantity);
		lblQuantity.setBounds(30, 12, 100, 25);
		
		setOpaque(false); // make this panels background to be transparent
	}
	
	/**
	 * Return the current quantity.
	 * @return the current quantity.
	 */
	public int getQuantity() 
	{ 
		return quantity; 
	}
	
	/**
	 * IncDecQntListener class is for the increasing/decreasing quantity 
	 * buttons.
	 *  
	 * @author Hagay Enoch
	 *
	 */
	private class IncDecQntListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == btnUp) 
			{
				if(quantity < 1000)
					quantity++;
			}
			else if(e.getSource() == btnDown)
			{	
				if(quantity > 0)
					quantity--;
			}
			JtxtQuantity.setText(" " + quantity);
		}
	}
	
	/**
	 * IconChange class is for changing the increasing/decreasing buttons, when
	 *  a mouse hover above it.
	 * 
	 * @author Hagay Enoch
	 *
	 */
	private class IconChange extends MouseAdapter
	{
		private JButton btn; // the increasing/decreasing button
		private ImageIcon iconOutside = null; // icon - mouse outside the button  
		private ImageIcon iconInside = null; // icon - mouse inside the button
		
		/** 
		 * Constructor for IconChange.
		 */
		private IconChange(JButton _btn ,ImageIcon iconOut, ImageIcon iconIn) 
		{
			  btn = _btn;
			  iconOutside = iconOut;
			  iconInside = iconIn;
		}
		
		/**
		 * Change icon's button when mouse enter the button.
		 */
		public void mouseEntered(MouseEvent me) 
		{
			btn.setIcon(iconInside);
		}
		
		/**
		 * Change icon's button when mouse exit the button.
		 */
	    public void mouseExited(MouseEvent me) 
	    {
	    	btn.setIcon(iconOutside);
	    }
	}	
}


