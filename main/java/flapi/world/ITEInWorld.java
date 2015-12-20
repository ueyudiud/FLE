package flapi.world;

import fle.api.te.IObjectInWorld;
import net.minecraft.tileentity.TileEntity;

public interface ITEInWorld extends IObjectInWorld
{
	public TileEntity getTileEntity();
	
	public int getLightValue();
	
	public boolean isRedStoneEmmit();
	
	public boolean isCatchRain();
}