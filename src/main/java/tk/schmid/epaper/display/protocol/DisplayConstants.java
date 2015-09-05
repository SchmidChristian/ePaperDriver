package tk.schmid.epaper.display.protocol;

public class DisplayConstants {

	public static final byte[] FRAME_HEADER = new byte[] { (byte) 0xA5 };
	
	public static final byte STRING_TERMINATOR = (byte) 0x00;

	public static final byte[] Frame_END = new byte[] { (byte) 0xCC, (byte) 0x33, (byte) 0xC3, (byte) 0x3C };
	
	public static final String COMMAND_OK = "OK";

}
