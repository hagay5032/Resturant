package clientServerResturant;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * GridBagEx class is for a summarize all the order details , including 
 * the list of dishes and quantities, all organize in a convenience panel. 
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 *  
 */
public class GridBagEx extends JPanel 
{
	
	private GridBagLayout gridbag = null;
	private GridBagConstraints c = null;
	
	/**
	 * Constructor for GridBagEx
	 * @param foodList is the food list , containing the order.
	 */
    public GridBagEx(ArrayList<DishPanel> foodList) 
    {
         gridbag = new GridBagLayout();
         c = new GridBagConstraints();

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setLayout(gridbag);
        
		c.weighty = 1;
		c.gridheight = 2; 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.ipadx = 100;
        c.gridwidth = 1;   //2 columns wide
        addCompunent(new JLabel("Name"));
        c.ipadx = 0;
        c.gridwidth = 1;  
		addCompunent(new JLabel("Quantity"));
		addCompunent(new JLabel("Price"));
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        addCompunent(new JLabel("Amount"));
    }
	
	/**
	 * Add a dish to this panel and place as a entry in a table.
	 * 
	 * @param dishPanel the dish panel with all its 
	 * @param dishNum
	 */
	public void addDish(DishPanel dishPanel, int dishNum) 
	{
		Font orderFont = new Font("Arial", Font.ITALIC+Font.BOLD, 16);
		
		// set & build labels
		JLabel lblFoodName = new JLabel(dishPanel.getTtl());
		JLabel lblQuantity = new JLabel(""+	dishPanel.getQuantity());
		JLabel lblPrice = new JLabel("" + dishPanel.getPrice());
		JLabel lblTotalSum = new JLabel("" + (dishPanel
				.getPrice()) * (dishPanel.getQuantity()));

		lblFoodName.setFont(orderFont);
		lblQuantity.setFont(orderFont);
		lblPrice.setFont(orderFont);
		lblTotalSum.setFont(orderFont);

		// add the labels as a entry in atable
		c.weighty = 1;
		c.gridheight = 2; 
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.ipadx = 100;		// padding to the left
        c.gridwidth = 1;    // 1 columns wide
        addCompunent(lblFoodName);
        c.ipadx = 0;		// reset padding
		addCompunent(lblQuantity);
        addCompunent(lblPrice);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        addCompunent(lblTotalSum);
        
	}

	/**
	 * Add a component to this panel with the 'c' instance as a constraint.
	 * @param cmpnt the new component to be added to this panel.
	 */
    protected void addCompunent(JComponent component)
    {
        gridbag.setConstraints(component, c);
        add(component);
    }

    /**
     * This method add a final row to this panel, It set a panel 'pnl' 
     * in the bottom of this panel with a total-sum label next to it.
     * @param pnl the new panel to be added to this panel.
     * @param lblTotalSum the total sum label to be added to this panel.
     */
	public void Summarize(JPanel pnl, JLabel lblTotalSum) 
	{
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20;
        c.weighty = 2;
        c.gridwidth = 2;    //2 columns wide
        addCompunent(pnl);
        c.gridwidth = GridBagConstraints.REMAINDER; 
        addCompunent(lblTotalSum);
        JPanel transpernat = new JPanel();
        addCompunent(transpernat);
        transpernat.setVisible(false);
	}
}


