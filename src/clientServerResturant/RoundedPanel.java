package clientServerResturant;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * RoundedPanel class is for creating a rounded panels, For a fancy decorative 
 * interface, with a shadow effect.
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 * 
 */
public class RoundedPanel extends JPanel
{
    protected int _widthOutline = 1; 
    protected Color _shadowColor = Color.BLACK; 
    protected boolean _shadowed = true; 
    protected boolean _highQuality = true; 
    protected Dimension _arcs = new Dimension(30, 30); 
    protected int _shadowGap = 5; 
    protected int _shadowOffset = 4;
    protected int _shadowAlpha = 150;
    protected Color _backgroundColor = Color.LIGHT_GRAY;

    /**
     * Constructor for RoundedPanel.
     */
    public RoundedPanel()
    {
        super();
        setOpaque(false); // create the edges of this panel to be transparent
    }

    @Override
    public void setBackground(Color c)
    {
    	super.setBackground(c);
        _backgroundColor = c;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int shadowGap = this._shadowGap;
        Color shadowColorA = new Color( _shadowColor.getRed(),
        								_shadowColor.getGreen(),
						        		_shadowColor.getBlue(),
						        		_shadowAlpha);
        
        // setting a shadow effect on this panel
        Graphics2D graphics = (Graphics2D) g;

        if(_highQuality)
        {
        	//this method edit the image edges pixel to be high quality
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
            						RenderingHints.VALUE_ANTIALIAS_ON);
        }

        if(_shadowed)
        {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(_shadowOffset, _shadowOffset,
            						width - _widthOutline - _shadowOffset,
            						height - _widthOutline - _shadowOffset,
            						_arcs.width, _arcs.height);
        }
        else
        {
            _shadowGap = 1;
        }

        graphics.setColor(_backgroundColor);
        graphics.fillRoundRect(0,  0, width - shadowGap,
        						height - shadowGap, _arcs.width, _arcs.height);
        
        graphics.setStroke(new BasicStroke(_widthOutline));
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0,  0, width - shadowGap,
        						height - shadowGap, _arcs.width, _arcs.height);
        
        graphics.setStroke(new BasicStroke());
    }
}


