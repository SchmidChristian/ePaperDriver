package tk.schmid.epaper.display.swing;

import tk.schmid.epaper.display.EPaperDisplay;
import tk.schmid.epaper.display.protocol.DisplayColor;
import tk.schmid.epaper.display.protocol.DisplayDirection;
import tk.schmid.epaper.display.protocol.DisplayFontSize;
import tk.schmid.epaper.display.protocol.DisplayStorage;
import tk.schmid.epaper.display.swing.shape.*;
import tk.schmid.epaper.display.swing.shape.Point;
import tk.schmid.epaper.display.swing.shape.Rectangle;
import tk.schmid.epaper.display.swing.shape.Action;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SwingDisplay extends JPanel implements EPaperDisplay {

    private int width;
    private int height;

    private List<Action> actions = new ArrayList<>();
    private Font standardFont = new Font(Font.SANS_SERIF,Font.PLAIN,24);

    public SwingDisplay(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.setFont(standardFont);

        for (Action action : actions) {
            action.draw(g2d);
        }
    }


    @Override
    public void connect() {

    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void clearScreen() {
        actions.clear();
        this.repaint();
    }

    @Override
    public void drawPoint(int x, int y) {
        actions.add(new Point(x, y));
        repaint();
    }

    @Override
    public void drawLine(int startX, int startY, int targetX, int targetY) {
        actions.add(new Line(startX, startY, targetX, targetY));
        invalidate();
        repaint(new java.awt.Rectangle(0, 0, width, height));
    }

    @Override
    public void drawRectangle(int x0, int y0, int x1, int y1, boolean filled) {
        actions.add(new Rectangle(x0, y0, x1 - x0, y1 - y0, filled));
        invalidate();
        repaint();
    }

    @Override
    public void drawCircle(int x, int y, int radius, boolean filled) {
        actions.add(new Circle(x, y, radius, filled));
        invalidate();
        repaint();
    }

    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, boolean filled) {
        actions.add(new Triangle(x0, y0, x1, y1, x2, y2, filled));
        invalidate();
        repaint();
    }

    @Override
    public void displayText(int x, int y, String text) {
        actions.add(new Text(text, x, y));
        invalidate();
        repaint();
    }

    @Override
    public void displayImage(int x, int y, String fileName) {

    }

    @Override
    public DisplayFontSize getEnglishFontSize() {
        return null;
    }

    @Override
    public void setEnglishFontSize(DisplayFontSize fontsize) {
        actions.add(new FontSize(fontsize));
    }

    @Override
    public DisplayFontSize getChineseFontSize() {
        return null;
    }

    @Override
    public void setChineseFontSize(DisplayFontSize fontsize) {

    }

    @Override
    public void setColor(DisplayColor foregroundColor, DisplayColor backgroundColor) {
        actions.add(new SetColor(foregroundColor,backgroundColor));
    }

    @Override
    public DisplayColor getDrawingColor() {
        return null;
    }

    @Override
    public DisplayColor getBackgroundColor() {
        return null;
    }

    @Override
    public void importFont(String fileName) {

    }

    @Override
    public void importImage(String fileName) {

    }

    @Override
    public void setDisplayDirection(DisplayDirection direction) {

    }

    @Override
    public DisplayDirection getDisplayDirection() {
        return null;
    }

    @Override
    public DisplayStorage getActiveStorage() {
        return null;
    }

    @Override
    public void setActiveStorage(DisplayStorage storage) {

    }

    @Override
    public int getBaudRate() {
        return 0;
    }

    @Override
    public void setBaudRate(int baudRate) {

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height);
    }
}
