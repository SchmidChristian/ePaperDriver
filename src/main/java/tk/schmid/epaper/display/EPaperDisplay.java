package tk.schmid.epaper.display;

import tk.schmid.epaper.display.protocol.DisplayColor;
import tk.schmid.epaper.display.protocol.DisplayDirection;
import tk.schmid.epaper.display.protocol.DisplayFontSize;
import tk.schmid.epaper.display.protocol.DisplayStorage;

public interface EPaperDisplay {

	public void connect();

	public void repaint();

	public void clearScreen();

	public void drawPoint(int x, int y);

	public void drawLine(int startX, int startY, int targetX, int targetY);

	public void drawRectangle(int x0, int y0, int x1, int y1, boolean filled);

	public void drawCircle(int x, int y, int radius, boolean filled);

	public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, boolean filled);

	public void displayText(int x, int y, String text);

	public void displayImage(int x, int y, String fileName);

	public DisplayFontSize getEnglishFontSize();

	public void setEnglishFontSize(DisplayFontSize fontsize);

	public DisplayFontSize getChineseFontSize();

	public void setChineseFontSize(DisplayFontSize fontsize);
	
	public void setColor(DisplayColor foregroundColor, DisplayColor backgroundColor);

	public DisplayColor getDrawingColor();
	
	public DisplayColor getBackgroundColor();

	public void importFont(String fileName);

	public void importImage(String fileName);

	public void setDisplayDirection(DisplayDirection direction);

	public DisplayDirection getDisplayDirection();

	public DisplayStorage getActiveStorage();

	public void setActiveStorage(DisplayStorage storage);
	
	public int getBaudRate();
	
	public void setBaudRate(int baudRate);
	
}
