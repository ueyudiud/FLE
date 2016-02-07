package fle.core.init;

import cpw.mods.fml.common.registry.EntityRegistry;
import farcore.entity.EntityFleFallingBlock;
import fle.core.FLE;
import fle.resource.entity.EntityThrowStone;

public class Entities
{
	public static Class<?> EntityThrowStone;
	
	public static void init()
	{
		int id = 0;
		EntityRegistry.registerModEntity(
				EntityFleFallingBlock.class, 
				"fle.falling.block", id++, 
				FLE.fle, 64, 1, true);
		if(EntityThrowStone != null)
		{
			EntityRegistry.registerModEntity(
					EntityFleFallingBlock.class, 
					"fle.throw.stone", id++, 
					FLE.fle, 48, 1, true);
		}
		else
		{
			++id;
		}
	}
}