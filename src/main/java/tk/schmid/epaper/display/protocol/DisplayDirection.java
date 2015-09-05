package tk.schmid.epaper.display.protocol;

import tk.schmid.epaper.display.serialcom.EPaperCommunicationException;

public enum DisplayDirection {
	Normal(0x00), Rotated(0x01);

	private byte displayDirectionConstant;

	DisplayDirection(int displayDirectionConstant) {
		this.displayDirectionConstant = (byte) displayDirectionConstant;
	}

	public byte getDisplayDirectionConstant() {
		return this.displayDirectionConstant;
	}

	public static DisplayDirection mapDeviceResponseToEnum(byte deviceResponse) {
		for (DisplayDirection current : DisplayDirection.values()) {
			if (current.getDisplayDirectionConstant() == deviceResponse) {
				return current;
			}
		}
		throw new EPaperCommunicationException("Unknown display direction code returned");
	}
}
