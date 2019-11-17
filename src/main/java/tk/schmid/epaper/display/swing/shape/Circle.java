package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Circle implements Action {

    private final int x, y, radius;
    private final boolean filled;

    public Circle(int x, int y, int radius, boolean filled) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.filled = filled;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (filled) {
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        } else {
            g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }
}
