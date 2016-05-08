package farcore.interfaces.energy.thermal;

import net.minecraft.world.World;

public interface IWorldThermalConductivityHandler
{
	float getThermalConductivity(World world, int x, int y, int z);
}