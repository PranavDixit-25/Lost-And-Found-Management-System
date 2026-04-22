package util;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom border that draws rounded corners.
 * Used to give panels and cards a modern, polished look.
 */
public class RoundedBorder extends AbstractBorder {

    private final Color color;
    private final int thickness;
    private final int radius;

    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(new RoundRectangle2D.Float(
            x + thickness / 2f, y + thickness / 2f,
            width - thickness, height - thickness,
            radius, radius
        ));
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        return insets;
    }
}
