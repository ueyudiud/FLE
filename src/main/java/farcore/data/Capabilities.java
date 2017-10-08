/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * All capabilities may used in mod were collected here.
 * @author ueyudiud
 */
public class Capabilities
{
	//Pre-pre initialization
	static
	{
		//		CapabilityManager.INSTANCE.register(IItemHandlerIO.class, new IItemHandlerIO.ItemHandlerIOStorage(), IItemHandlerIO.Instance.class);
	}
	
	//	@CapabilityInject(IItemHandlerIO.class)
	//	public static Capability<IItemHandlerIO> ITEM_HANDLER_IO;
	
	public static final Capability<IItemHandler> CAPABILITY_ITEM = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	public static final Capability<IFluidHandler> CAPABILITY_FLUID = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
}