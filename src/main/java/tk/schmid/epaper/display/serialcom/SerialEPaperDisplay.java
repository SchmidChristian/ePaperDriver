package tk.schmid.epaper.display.serialcom;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import tk.schmid.epaper.display.EPaperDisplay;
import tk.schmid.epaper.display.protocol.DisplayColor;
import tk.schmid.epaper.display.protocol.DisplayCommand;
import tk.schmid.epaper.display.protocol.DisplayConstants;
import tk.schmid.epaper.display.protocol.DisplayDirection;
import tk.schmid.epaper.display.protocol.DisplayFontSize;
import tk.schmid.epaper.display.protocol.DisplayStorage;

public class SerialEPaperDisplay implements EPaperDisplay {

	private EPaperSerialCommunication communication;

	public SerialEPaperDisplay(String deviceName) {
		this.communication = new EPaperSerialCommunication(deviceName);
	}

	public void connect() {
		communication.connect();
		communication.sendCommand(DisplayCommand.Handshacke);
	}

	public void repaint() {
		communication.sendCommand(DisplayCommand.Refresh_And_Update_Display);
	}

	public void clearScreen() {
		communication.sendCommand(DisplayCommand.Clear_Screen);
	}

	public void drawPoint(int x, int y) {
		ByteBuffer coordinates = ByteBuffer.allocate(4);
		coordinates.putShort((short) x);
		coordinates.putShort((short) y);
		communication.sendCommand(DisplayCommand.Draw_Point_At, coordinates.array());
	}

	public void drawLine(int startX, int startY, int targetX, int targetY) {
		ByteBuffer coordinates = ByteBuffer.allocate(8);
		coordinates.putShort((short) startX);
		coordinates.putShort((short) startY);
		coordinates.putShort((short) targetX);
		coordinates.putShort((short) targetY);
		communication.sendCommand(DisplayCommand.Draw_Line_At, coordinates.array());
	}

	public void drawRectangle(int x0, int y0, int x1, int y1, boolean filled) {
		DisplayCommand command = filled ? DisplayCommand.Draw_Filled_Rectangle : DisplayCommand.Draw_Rectangle;
		ByteBuffer coordinates = ByteBuffer.allocate(8);
		coordinates.putShort((short) x0);
		coordinates.putShort((short) y0);
		coordinates.putShort((short) x1);
		coordinates.putShort((short) y1);
		communication.sendCommand(command, coordinates.array());
	}

	public void drawCircle(int x, int y, int radius, boolean filled) {
		DisplayCommand command = filled ? DisplayCommand.Draw_Filled_Circle : DisplayCommand.Draw_Circle;
		ByteBuffer coordinates = ByteBuffer.allocate(6);
		coordinates.putShort((short) x);
		coordinates.putShort((short) y);
		coordinates.putShort((short) radius);
		communication.sendCommand(command, coordinates.array());
	}

	public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, boolean filled) {
		DisplayCommand command = filled ? DisplayCommand.Draw_Filled_Triangle : DisplayCommand.Draw_Triangle;
		ByteBuffer coordinates = ByteBuffer.allocate(12);
		coordinates.putShort((short) x0);
		coordinates.putShort((short) y0);
		coordinates.putShort((short) x1);
		coordinates.putShort((short) y1);
		coordinates.putShort((short) x2);
		coordinates.putShort((short) y2);
		communication.sendCommand(command, coordinates.array());
	}

	public void displayText(int x, int y, String text) {
		byte[] textBytes = this.encodeStringForDevice(text);
		ByteBuffer parameters = ByteBuffer.allocate(textBytes.length + 4);
		parameters.putShort((short) x);
		parameters.putShort((short) y);
		parameters.put(textBytes);

		communication.sendCommand(DisplayCommand.Write_Text, parameters.array());
	}

	public void displayImage(int x, int y, String fileName) {
		byte[] fileNameBytes = this.encodeStringForDevice(fileName);
		ByteBuffer parameters = ByteBuffer.allocate(fileNameBytes.length + 4);
		parameters.putShort((short) x);
		parameters.putShort((short) y);
		parameters.put(fileNameBytes);
		communication.sendCommand(DisplayCommand.Display_Image, parameters.array());
	}

	public DisplayFontSize getEnglishFontSize() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Font_Size_English);
		return DisplayFontSize.mapDeviceResponseToEnum(Byte.parseByte(new String(response)));
	}

	public void setEnglishFontSize(DisplayFontSize fontsize) {
		byte[] fontSizeParameter = new byte[] { fontsize.getDisplayFontSizeConstant() };
		communication.sendCommand(DisplayCommand.Set_Font_Size_English, fontSizeParameter);
	}

	public DisplayFontSize getChineseFontSize() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Font_Size_Chinese);
		return DisplayFontSize.mapDeviceResponseToEnum(Byte.parseByte(new String(response)));
	}

	public void setChineseFontSize(DisplayFontSize fontsize) {
		byte[] fontSizeParameter = new byte[] { fontsize.getDisplayFontSizeConstant() };
		communication.sendCommand(DisplayCommand.Set_Font_Size_Chinese, fontSizeParameter);
	}

	public void setColor(DisplayColor foregroundColor, DisplayColor backgroundColor) {
		byte[] colorParameter = new byte[] { foregroundColor.getColorCode(), backgroundColor.getColorCode() };
		communication.sendCommand(DisplayCommand.Set_Color, colorParameter);
	}

	public DisplayColor getDrawingColor() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Color);
		return DisplayColor.mapDeviceResponseToEnum(Byte.parseByte(new String(response).substring(0,1)));
	}
	
	public DisplayColor getBackgroundColor() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Color);
		return DisplayColor.mapDeviceResponseToEnum(Byte.parseByte(new String(response).substring(1,2)));
	}

	public void importFont(String fileName) {
		byte[] textBytes = this.encodeStringForDevice(fileName);
		ByteBuffer parameters = ByteBuffer.allocate(textBytes.length);
		parameters.put(textBytes);
		communication.sendCommand(DisplayCommand.Import_Font_Library, parameters.array());
	}

	public void importImage(String fileName) {
		byte[] textBytes = this.encodeStringForDevice(fileName);
		ByteBuffer parameters = ByteBuffer.allocate(textBytes.length);
		parameters.put(textBytes);
		communication.sendCommand(DisplayCommand.Import_Image, parameters.array());
	}

	public void setDisplayDirection(DisplayDirection direction) {
		byte[] directionParameter = new byte[] { direction.getDisplayDirectionConstant() };
		communication.sendCommand(DisplayCommand.Set_Display_Direction, directionParameter);
	}

	public DisplayDirection getDisplayDirection() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Display_Direction);
		return DisplayDirection.mapDeviceResponseToEnum(Byte.parseByte(new String(response)));
	}

	public DisplayStorage getActiveStorage() {
		byte[] response = communication.sendCommand(DisplayCommand.Read_Selected_Storage_Area);
		return DisplayStorage.mapDeviceResponseToEnum(Byte.parseByte(new String(response)));
	}

	public void setActiveStorage(final DisplayStorage STORAGE) {
		   byte[] storageParameter = new byte[] { STORAGE.getDisplayStorageConstant() };
		       communication.sendCommand(DisplayCommand.Set_Selected_Storage_Area, storageParameter);
		}


	private byte[] encodeStringForDevice(String text) {
		byte[] textBytes = text.getBytes(StandardCharsets.US_ASCII);

		ByteBuffer textParameter = ByteBuffer.allocate(textBytes.length + 1);
		textParameter.put(textBytes);
		textParameter.put(DisplayConstants.STRING_TERMINATOR);

		return textParameter.array();
	}

	public int getBaudRate() {
		byte[] response = communication.sendCommand(DisplayCommand.Get_Baud_Rate);
		String baudRateAscii = new String(response);
		int baudRate = Integer.parseInt(baudRateAscii);
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		communication.sendCommand(DisplayCommand.Read_Selected_Storage_Area);
	}
}
