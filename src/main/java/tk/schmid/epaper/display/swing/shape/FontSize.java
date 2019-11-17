package tk.schmid.epaper.display.swing.shape;

import tk.schmid.epaper.display.protocol.DisplayFontSize;

import java.awt.*;

public class FontSize implements Action {

    private final DisplayFontSize fontSize;

    public FontSize(DisplayFontSize fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Font font = g2d.getFont();
        g2d.setFont(font.deriveFont(Font.PLAIN,  fontSize.getFontPixelHeight() ));
    }
}
