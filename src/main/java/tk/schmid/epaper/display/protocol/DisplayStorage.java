package tk.schmid.epaper.display.protocol;

import tk.schmid.epaper.display.serialcom.EPaperCommunicationException;

public enum DisplayStorage {
	NanoFlash(0x00), MicroSD(0x01);

	private byte storageConstant;

	DisplayStorage(int storageConstant) {
		this.storageConstant = (byte)storageConstant;
	}

	public byte getDisplayStorageConstant() {
		return this.storageConstant;
	}
	
	public static DisplayStorage mapDeviceResponseToEnum(byte deviceResponse) {
		for (DisplayStorage current : DisplayStorage.values()) {
			if (current.getDisplayStorageConstant() == deviceResponse) {
				return current;
			}
		}
		throw new EPaperCommunicationException("Unknown storage code returned");
	}
}
