package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Triangle implements Action {

    private final Polygon polygon;

    public Triangle(int x0, int y0, int x1, int y1, int x2, int y2, boolean filled) {
        this.filled = filled;
        polygon = new Polygon(new int[]{x0, x1, x2}, new int[]{y0, y1, y2}, 3);
    }

    private final boolean filled;

    @Override
    public void draw(Graphics2D g2d) {
        if (filled) {
            g2d.fillPolygon(polygon);
        } else {
            g2d.drawPolygon(polygon);
        }
    }
}
