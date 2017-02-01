package nebula.common.enviornment;

import nebula.common.world.ICoord;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnviornmentBlockPos implements IEnvironment, ICoord
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
	public long worldTime()
	{
		return this.world.getWorldTime();
	}
	
	@Override
	public ICoord coord()
	{
		return this;
	}
	
	@Override
	public float biomeTemperature()
	{
		return this.world.getBiome(this.pos).getFloatTemperature(this.pos);
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
}