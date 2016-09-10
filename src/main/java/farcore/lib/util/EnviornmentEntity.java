package farcore.lib.util;

import farcore.lib.world.IEnvironment;
import net.minecraft.entity.Entity;

public class EnviornmentEntity implements IEnvironment
{
	public EnviornmentEntity(Entity entity)
	{
	}

	@Override
	public float temperature()
	{
		return 0;
	}
}