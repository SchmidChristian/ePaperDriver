package tk.schmid.epaper.display.swing.shape;

import tk.schmid.epaper.display.protocol.DisplayColor;

import java.awt.*;

public class SetColor implements Action {

    private final Color colorFg;
    private final Color colorBg;

    public SetColor(DisplayColor fg, DisplayColor bg) {
        colorFg = mapColor(fg);
        colorBg = mapColor(bg);
    }

    private Color mapColor(DisplayColor fg) {
        switch (fg) {
            case Black:
                return Color.BLACK;
            case DarkGrey:
                return Color.DARK_GRAY;
            case LightGrey:
                return Color.LIGHT_GRAY;
            default:
                return Color.WHITE;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(colorFg);
    }
}
