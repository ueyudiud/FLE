package fle.api.te;

import net.minecraft.tileentity.TileEntity;

public interface ITEInWorld extends IObjectInWorld
{
	public TileEntity getTileEntity();
	
	public int getLightValue();
	
	public boolean isRedStoneEmmit();
	
	public boolean isCatchRain();
}