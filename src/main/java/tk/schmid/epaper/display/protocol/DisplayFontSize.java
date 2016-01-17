package tk.schmid.epaper.display.protocol;

import tk.schmid.epaper.display.serialcom.EPaperCommunicationException;

public enum DisplayFontSize {
	DotMatrix_32(32, 0x01), DotMatrix_48(48, 0x02), DotMatrix_64(64, 0x03);

	private byte fontSizeConstant;
	private int pixelHeight; 
	
	DisplayFontSize(int pixelHeight, int fontSize) {
		this.fontSizeConstant = (byte) fontSize;
		this.pixelHeight = pixelHeight;
	}

	public int getFontPixelHeight() {
		return this.pixelHeight;
	}

	public byte getDisplayFontSizeConstant() {
		return this.fontSizeConstant;
	}

	public static DisplayFontSize mapDeviceResponseToEnum(byte deviceResponse) {
		for (DisplayFontSize current : DisplayFontSize.values()) {
			if (current.getDisplayFontSizeConstant() == deviceResponse) {
				return current;
			}
		}
		throw new EPaperCommunicationException("Unknown font size code returned");
	}
}