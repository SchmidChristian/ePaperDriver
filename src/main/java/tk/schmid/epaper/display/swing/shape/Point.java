package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Point implements Action {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, 1, 1);
    }
}
