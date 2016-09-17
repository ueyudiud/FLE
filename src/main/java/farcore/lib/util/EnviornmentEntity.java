package farcore.lib.util;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.world.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class EnviornmentEntity implements IEnvironment
{
	Entity entity;

	public EnviornmentEntity(Entity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public float temperature()
	{
		return ThermalNet.getTemperature(entity.worldObj, new BlockPos(entity), false);
	}
}