/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.handler;

import farcore.FarCore;
import farcore.lib.capability.IFluidHandlerHelper;
import nebula.common.util.Direction;
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
	@SubscribeEvent
	public static void onCapabilityWraped(AttachCapabilitiesEvent.TileEntity event)
	{
		TileEntity tile = event.getTileEntity();
		event.addCapability(new ResourceLocation(FarCore.ID, "capabilities"), new WrapperChecker(tile));
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
		@SuppressWarnings("unchecked")
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
