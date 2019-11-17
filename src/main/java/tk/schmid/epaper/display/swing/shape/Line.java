package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Line implements Action {

    private final int x, y, targetX, targetY;

    public Line(int x, int y, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawLine(x, y, targetX, targetY);
    }
}
