package fle.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.enums.EnumBlock;
import farcore.event.FluidEvent.FluidTouchBlockEvent;
import farcore.util.U;
import fle.core.tile.TileEntityTorch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

public class WaterBehaviorHandler
{
	@SubscribeEvent
	public void onWaterTouch(FluidTouchBlockEvent event)
	{
		if(event.fluidBlock.getFluid() == FluidRegistry.WATER)
		{
			Block block = event.world.getBlock(event.x, event.y, event.z);
			if(block == EnumBlock.fire.block())
			{
				event.setBlockDestroyed(false);
				return;
			}
			TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
			if(tile instanceof TileEntityTorch)
			{
				((TileEntityTorch) tile).isWet = true;
				event.setBlockDestroyed();
				return;
			}
		}
	}
}