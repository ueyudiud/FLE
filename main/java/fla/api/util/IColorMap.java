package fla.api.util;

import net.minecraft.util.ResourceLocation;

public interface IColorMap 
{
	public int getWidth();
	
	public int getHeight();
	
	public int getColor(int xPos, int yPos);
}
