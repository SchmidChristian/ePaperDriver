package tk.schmid.epaper.development;

import java.io.IOException;

import tk.schmid.epaper.display.protocol.DisplayFontSize;
import tk.schmid.epaper.display.serialcom.SerialEPaperDisplay;

public class DevelopmentMain {

	public static void main(String[] args) throws IOException {
		SerialEPaperDisplay display = new SerialEPaperDisplay("COM5");
		display.connect();

		System.out.println("Connected...");
		System.out.println("Active storage: " + display.getActiveStorage());
		System.out.println("Display Direction: " + display.getDisplayDirection());
		System.out.println("English Font Size: " + display.getEnglishFontSize());
		System.out.println("Chinese Font Size " + display.getChineseFontSize());
		System.out.println("Drawing Color: " + display.getDrawingColor());
		System.out.println("Background Color: " + display.getBackgroundColor());
		System.out.println("Baud Rate: " + display.getBaudRate());

		display.clearScreen();

		// Draw a frame with border at the edges of the screen
		display.drawRectangle(20, 20, 40, 580, true);
		display.drawRectangle(20, 560, 780, 580, true);
		display.drawRectangle(20, 20, 780, 40, true);
		display.drawRectangle(760, 20, 780, 580, true);

		display.setEnglishFontSize(DisplayFontSize.DotMatrix_64);
		display.displayText(300, 260, "Hello World");

		display.setEnglishFontSize(DisplayFontSize.DotMatrix_32);
		display.displayText(100, 360, "https://github.com/SchmidChristian/ePaperDriver");

		display.drawCircle(410, 160, 50, true);

		display.repaint();
		
		System.exit(0);
	}
}
