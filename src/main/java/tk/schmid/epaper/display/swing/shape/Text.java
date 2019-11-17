package tk.schmid.epaper.display.swing.shape;

import java.awt.*;

public class Text implements Action {

    private final String text;
    private final int x, y;

    public Text(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawString(text, x, y);
    }
}
