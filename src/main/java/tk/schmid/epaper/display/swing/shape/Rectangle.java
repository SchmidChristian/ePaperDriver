package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Rectangle implements Action {

    public Rectangle(int x, int y, int width, int height, boolean fill) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fill = fill;
    }

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final boolean fill;

    @Override
    public void draw(Graphics2D g2d) {
        if (fill) {
            g2d.fillRect(x, y, width, height);
        } else {
            g2d.drawRect(x, y, width, height);
        }
    }
}
