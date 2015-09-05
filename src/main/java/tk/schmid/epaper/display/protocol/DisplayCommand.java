package tk.schmid.epaper.display.protocol;

public enum DisplayCommand {

	Handshacke(0x00), 
	Set_Baud_Rate(0x01), 
	Get_Baud_Rate(0x02), 
	Read_Selected_Storage_Area(0x06),
	Set_Selected_Storage_Area(0x07),
	Enter_Sleep_Mode(0x08),
	Refresh_And_Update_Display(0x0A),
	Get_Display_Direction(0x0C),
	Set_Display_Direction(0x0D),
	Import_Font_Library(0x0E),
	Import_Image(0x0F),
	Set_Color(0x10),
	Get_Color(0x11),
	Get_Font_Size_English(0x1C),
	Set_Font_Size_English(0x1E),
	Set_Font_Size_Chinese(0x1F),
	Get_Font_Size_Chinese(0x1D),
	Draw_Point_At(0x20),
	Draw_Line_At(0x22),
	Draw_Filled_Rectangle(0x24),
	Draw_Rectangle(0x25),
	Draw_Filled_Circle(0x27),
	Draw_Circle(0x26),
	Draw_Filled_Triangle(0x29),
	Draw_Triangle(0x28),
	Clear_Screen(0x2E),
	Write_Text(0x30),
	Display_Image(0x70);
	
	private byte commandCode;

	DisplayCommand(int commandCode) {
		this.commandCode = (byte)commandCode;
	}

	public byte getCommandCode() {
		return this.commandCode;
	}

}
