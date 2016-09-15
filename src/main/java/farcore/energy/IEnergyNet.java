package farcore.energy;

import farcore.energy.EnergyHandler.Energy;
import net.minecraft.world.World;

public interface IEnergyNet
{
	default Energy getEnergyFormat()
	{
		return EnergyHandler.STANDARD_DOUBLE;
	}

	void add(Object tile);
	
	void remove(Object tile);
	
	void reload(Object tile);
	
	void mark(Object tile);
	
	void unload(World world);
	
	void load(World world);
	
	void update(World world);
}