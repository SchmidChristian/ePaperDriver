package tk.schmid.epaper.display.protocol;

import tk.schmid.epaper.display.serialcom.EPaperCommunicationException;

public enum DisplayFontSize {
	DotMatrix_32(0x00), DotMatrix_48(0x01), DotMatrix_64(0x02);

	private byte fontSize;

	DisplayFontSize(int fontSize) {
		this.fontSize = (byte) fontSize;
	}

	public byte getDisplayFontSizeConstant() {
		return this.fontSize;
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