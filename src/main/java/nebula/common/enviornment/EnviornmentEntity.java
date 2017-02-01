package nebula.common.enviornment;

import nebula.common.world.ICoord;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnviornmentEntity implements IEnvironment, ICoord
{
	Entity entity;
	
	public EnviornmentEntity(Entity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public long worldTime()
	{
		return this.entity.world.getWorldTime();
	}
	
	@Override
	public ICoord coord()
	{
		return this;
	}
	
	@Override
	public float biomeTemperature()
	{
		return getBiome().getFloatTemperature(pos());
	}
	
	@Override
	public World world()
	{
		return this.entity.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.entity.getPosition();
	}
}