package tk.schmid.epaper.display.protocol;

import tk.schmid.epaper.display.serialcom.EPaperCommunicationException;

public enum DisplayColor {

	Black(0x00), DarkGrey(0x01), LightGrey(0x02), White(0x03);

	private byte colorCode;

	DisplayColor(int colorCode) {
		this.colorCode = (byte) colorCode;
	}
	
	
	public byte getColorCode() {
		return this.colorCode;
	}
	
	public static DisplayColor mapDeviceResponseToEnum(byte deviceResponse) {
		for (DisplayColor current : DisplayColor.values()) {
			if (current.getColorCode() == deviceResponse) {
				return current;
			}
		}
		throw new EPaperCommunicationException("Unknown color code returned");
	}
}
