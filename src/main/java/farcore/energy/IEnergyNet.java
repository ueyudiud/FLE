package farcore.energy;

import net.minecraft.world.World;

public interface IEnergyNet
{
	void add(Object tile);

	void remove(Object tile);

	void reload(Object tile);

	void mark(Object tile);

	void unload(World world);

	void load(World world);

	void update(World world);
}