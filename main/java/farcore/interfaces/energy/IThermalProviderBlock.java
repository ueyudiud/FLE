package farcore.interfaces.energy;

import net.minecraft.world.World;

public interface IThermalProviderBlock
{
	int getBlockTemperature(World world, int x, int y, int z);
}