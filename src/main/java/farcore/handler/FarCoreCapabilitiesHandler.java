/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.handler;

import farcore.FarCore;
import farcore.data.Capabilities;
import farcore.lib.capability.IFluidHandlerHelper;
import farcore.lib.item.IMaterialCapabilityCreative;
import farcore.lib.material.behavior.MaterialPropertyManager.MaterialHandler;
import nebula.common.util.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * The capabilities wrapper handler.
 * 
 * @author ueyudiud
 */
public class FarCoreCapabilitiesHandler
{
	public static final ResourceLocation ITEM_PROPERTY = new ResourceLocation(FarCore.ID, "item/property");
	public static final ResourceLocation TILE_PROPERTY = new ResourceLocation(FarCore.ID, "tile/property");
	
	@SubscribeEvent
	public static void onCapabilityProviding(AttachCapabilitiesEvent.Item event)
	{
		if (event.getObject() instanceof IMaterialCapabilityCreative)
		{
			final IMaterialCapabilityCreative item = (IMaterialCapabilityCreative) event.getObject();
			final ItemStack stack = event.getItemStack();
			event.addCapability(ITEM_PROPERTY, new ICapabilityProvider()
			{
				private MaterialHandler handler;
				
				private MaterialHandler handler()
				{
					if (this.handler == null)
					{
						this.handler = item.createMaterialHandler(stack);
					}
					return this.handler;
				}
				
				@Override
				public boolean hasCapability(Capability<?> capability, EnumFacing facing)
				{
					return capability == Capabilities.CAPABILITY_MATERIAL && facing == null;
				}
				
				@Override
				public <T> T getCapability(Capability<T> capability, EnumFacing facing)
				{
					return hasCapability(capability, facing) ?
							Capabilities.CAPABILITY_MATERIAL.cast(handler()) : null;
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void onCapabilityWraped(AttachCapabilitiesEvent.TileEntity event)
	{
		TileEntity tile = event.getTileEntity();
		event.addCapability(TILE_PROPERTY, new WrapperChecker(tile));
	}
	
	static class WrapperChecker implements ICapabilityProvider
	{
		TileEntity tile;
		
		public WrapperChecker(TileEntity tile)
		{
			this.tile = tile;
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.tile instanceof IFluidHandlerHelper && ((IFluidHandlerHelper) this.tile).shouldProviedeFluidIOFrom(Direction.of(facing)));
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return !hasCapability(capability, facing) ? null : (T) castCapability(capability, facing);
		}
		
		private Object castCapability(Capability<?> capability, EnumFacing facing)
		{
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return new IFluidHandlerHelper.FluidHandlerWrapper(this.tile, Direction.of(facing));
			return null;
		}
	}
}
