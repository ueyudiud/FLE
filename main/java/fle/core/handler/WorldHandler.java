package fle.core.handler;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fle.FLE;
import fle.api.event.FLEThermalHeatEvent;

public class WorldHandler
{
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			FLE.fle.getKeyboard().sendKeyUpdate();
		}
	}
	
	@SubscribeEvent
	public void onHeatEmmit(FLEThermalHeatEvent evt)
	{
		if(evt.world.isRemote) return;
		double h = Math.abs(evt.heat + 1);
		if(evt.block == Blocks.lava && !evt.isEmmit)
		{
			if(evt.world.rand.nextInt((int) (10000000D / h) + 1) == 0)
			{
				evt.world.setBlock(evt.x, evt.y, evt.z, Blocks.obsidian);
				evt.setHeat(evt.heat / 2);
				return;
			}
		}
		else if(evt.block == Blocks.water)
		{
			if(evt.world.rand.nextInt((int) (4000000D / h) + 1) == 0)
			{
				if(evt.isEmmit)
				{
					evt.world.setBlockToAir(evt.x, evt.y, evt.z);
				}
				else
				{
					evt.world.setBlock(evt.x, evt.y, evt.z, Blocks.ice);
				}
				return;
			}
		}
		else if(evt.block == Blocks.ice && evt.isEmmit)
		{
			if(evt.world.rand.nextInt((int) (2000000D / h) + 1) == 0)
			{
				evt.world.setBlock(evt.x, evt.y, evt.z, Blocks.water);
				evt.setHeat(evt.heat * 1.2F);
			}
		}
	}
}