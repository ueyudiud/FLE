/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.solid.container;

import farcore.lib.inventory.ISolidHandler;
import nebula.common.data.Misc;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author ueyudiud
 */
public class CapabilitySolidHandler
{
	@CapabilityInject(ISolidHandler.class)
	public static Capability<ISolidHandler> CAPABILITY_SOLID_HANDLER;
	
	static
	{
		CapabilityManager.INSTANCE.register(ISolidHandler.class,
				(IStorage<ISolidHandler>) Misc.STORAGE, () -> new SolidTank(1000));
	}
}
