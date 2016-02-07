package fle.resource;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.world.BlockPos;
import fle.core.enums.EnumDirtState;
import fle.init.Blocks;
import fle.resource.block.auto.BlockUniversalDirt;
import fle.resource.tile.TileEntityDirt;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ResourceHandler
{
	@SubscribeEvent
	public void onUseHoe(UseHoeEvent evt)
	{
		if(evt.world.isRemote)
		{
			if(evt.world.getBlock(evt.x, evt.y, evt.z) == Blocks.dirt)
			{
				evt.setResult(Result.ALLOW);
				return;
			}
		}
		else
		{
			if(evt.world.getBlock(evt.x, evt.y, evt.z) == Blocks.dirt)
			{
				TileEntityDirt tile
				= ((BlockUniversalDirt) Blocks.dirt).getTile(new BlockPos(evt.world, evt.x, evt.y, evt.z));
				if(tile.state == EnumDirtState.dirt)
				{
					tile.state = EnumDirtState.farmland;
					tile.markDirty();
					evt.current.damageItem(1, evt.entityPlayer);
					evt.setResult(Result.ALLOW);
					return;
				}
			}
		}
	}
}