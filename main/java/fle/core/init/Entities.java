package fle.core.init;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import farcore.entity.EntityFleFallingBlock;
import fle.FLE;

public class Entities
{
	public static void init()
	{
		int id = 0;
		EntityRegistry.registerModEntity(
				EntityFleFallingBlock.class, 
				"fle.falling.block", id++, 
				FLE.fle, 64, 1, true);
	}
}