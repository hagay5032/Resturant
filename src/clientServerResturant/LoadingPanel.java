package clientServerResturant;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * LoadingPanel class is for creating a loading panel for waiting to the server
 * until the information received.
 * 
 * @author Hagay Enoch
 * @version 4.12.19
 *  
 */
public class LoadingPanel extends  RoundedPanel {

	private JLabel ttl = null;  // the title of this panel
	
	public LoadingPanel(String title) 
	{
		setBackground(Color.DARK_GRAY.darker().darker());

		// set the gif of loading
	    java.net.URL imageURL   = getClass().getResource("images/loading.gif");
	    ImageIcon imageIcon = new ImageIcon(imageURL);
	    JLabel iconLabel = new JLabel();
	    iconLabel.setIcon(imageIcon);
	    imageIcon.setImageObserver(iconLabel);

	    // set label
	    ttl = new JLabel(title);
	    ttl.setForeground(Color.WHITE);
	    ttl.setFont(new Font("Arial",Font.PLAIN + Font.BOLD, 22));
	    ttl.setSize(100, 20);
	    
	    add(ttl);
	    add(iconLabel);
	}
	
	/**
	 * This method stop the moving circle and declare of occurrence of some
	 * error.
	 */
	public void hold() 
	{
		removeAll(); 
		revalidate();
		repaint();
		
		// set the label
		ttl.setText("Error, Please check your network. And try again");
		ttl.setForeground(Color.RED);
		ttl.setFont(new Font("Arial",Font.PLAIN, 18));
		add(ttl);
		
		// add an image instead of the gif
		java.net.URL imageURL = getClass().
					getResource("images/wait.png");
		ImageIcon imageIcon = new ImageIcon(imageURL);
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		imageIcon.setImageObserver(iconLabel);
		add(iconLabel);
		iconLabel.setBounds(200, 0, 400, 400);
		
		revalidate();
		repaint();
	}
}


