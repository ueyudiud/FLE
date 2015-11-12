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
		if(evt.block == Blocks.lava && evt.isEmmit)
		{
			if(evt.world.rand.nextInt((int) (50000D / evt.heat)) == 0)
			{
				evt.world.setBlock(evt.x, evt.y, evt.z, Blocks.obsidian);
				evt.setHeat(evt.heat / 2);
				return;
			}
		}
		else if(evt.block == Blocks.water)
		{
			if(evt.world.rand.nextInt((int) (40000D / evt.heat)) == 0)
			{
				if(evt.isEmmit)
					evt.world.setBlock(evt.x, evt.y, evt.z, Blocks.ice);
				else
					evt.world.setBlockToAir(evt.x, evt.y, evt.z);
				evt.setHeat(evt.heat / 2);
				return;
			}
		}
	}
}