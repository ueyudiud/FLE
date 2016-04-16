package farcore.interfaces.energy.thermal;

import net.minecraft.world.World;

public interface IThermalProviderBlock
{
	float getBlockTemperature(World world, int x, int y, int z);

	float getThermalConductivity(World world, int x, int y, int z);
	
	void onHeatChanged(World world, int x, int y, int z, float input);
}