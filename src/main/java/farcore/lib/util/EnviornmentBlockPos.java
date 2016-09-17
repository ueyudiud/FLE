package farcore.lib.util;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.world.ICoord;
import farcore.lib.world.IEnvironment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnviornmentBlockPos implements IEnvironment
{
	World world;
	BlockPos pos;
	
	public EnviornmentBlockPos(ICoord coord)
	{
		this(coord.world(), coord.pos());
	}
	public EnviornmentBlockPos(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}

	@Override
	public float temperature()
	{
		return ThermalNet.getTemperature(world, pos, true);
	}
}