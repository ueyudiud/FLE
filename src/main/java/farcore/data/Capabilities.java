/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.data;

import farcore.lib.inventory.ISolidHandler;
import farcore.lib.material.behavior.MaterialPropertyManager;
import farcore.lib.material.behavior.MaterialPropertyManager.MaterialHandler;
import farcore.lib.solid.container.CapabilitySolidHandler;
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
	
	public static final Capability<MaterialHandler> CAPABILITY_MATERIAL = MaterialPropertyManager.CAPABILITY_MATERIAL_HANDLER;
}
