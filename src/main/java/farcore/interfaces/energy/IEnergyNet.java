package farcore.interfaces.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IEnergyNet
{
	void add(TileEntity tile);
	
	void remove(TileEntity tile);
	
	void reload(TileEntity tile);
	
	void mark(TileEntity tile);
	
	void unload(World world);
	
	void load(World world);
	
	void update(World world);
}