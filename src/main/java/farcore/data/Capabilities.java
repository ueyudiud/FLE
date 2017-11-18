/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.lib.solid.container.CapabilitySolidHandler;
import farcore.lib.solid.container.ISolidHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * All capabilities may used in mod were collected here.
 * 
 * @author ueyudiud
 */
public class Capabilities
{
	public static final Capability<IItemHandler>	CAPABILITY_ITEM		= CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	public static final Capability<IFluidHandler>	CAPABILITY_FLUID	= CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	public static final Capability<ISolidHandler>	CAPABILITY_SOLID	= CapabilitySolidHandler.CAPABILITY_SOLID_HANDLER;
}
