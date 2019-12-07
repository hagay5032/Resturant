package clientServerResturant;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;

/**
 * TextBubbleBorder class is for creating a rounded border, For a fancy
 * decorative interface
 * 
 * @author Hagay Enoch
 * @version 8.12.19
 *
 */
public class TextBubbleBorder extends AbstractBorder {

    private Color color = null; 
    private int thickness = 4; 
    private int arcs = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad = 0;
    private int pointerPad = 4;
    private boolean left = true;
    private RenderingHints hints = null;

    /**
     * Constructor for TextBubbleBorder.
     * @param color the color for this border.
     */
    public TextBubbleBorder(Color color) 
    {
        this(color, 4, 8, 7);
    }

    /**
     * 
     * @param color
     * @param thickness
     * @param radii
     * @param pointerSize
     */
    TextBubbleBorder(Color color, int thickness, int radii, int pointerSize)
    {
        this.thickness = thickness;
        this.arcs = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        pad /= 5;
        int bottomPad = pad + pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c) 
    {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets)
    {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                arcs,
                arcs);

        Polygon pointer = new Polygon();

        if (left) {
            // left point
            pointer.addPoint(
                    strokePad + arcs + pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    strokePad + arcs + pointerPad + pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    strokePad + arcs + pointerPad + (pointerSize / 2),
                    height - strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (strokePad + arcs + pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (strokePad + arcs + pointerPad + pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (strokePad + arcs + pointerPad + (pointerSize / 2)),
                    height - strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
       
        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0,0,width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }
	
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}


