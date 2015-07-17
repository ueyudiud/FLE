package fla.api.recipe;

import net.minecraft.util.ResourceLocation;
import fla.api.util.FlaValue;

public enum ErrorType 
{
	DEFAULT(0, 0),
	CAN_NOT_OUTPUT(1, 0),
	LOW_ENERGY(2, 0);
	
	public static ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/error.png");
	int index;
	int xPos;
	int yPos;
	
	ErrorType(int x, int y) 
	{
		index = 1 << ordinal();
		xPos = x * 16;
		yPos = y * 16;
	}
	
	public int getU()
	{
		return xPos;
	}
	
	public int getV()
	{
		return yPos;
	}
}
