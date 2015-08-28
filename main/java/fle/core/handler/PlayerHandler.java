package fle.core.handler;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.MultiPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import fle.core.init.IB;
import fle.core.tool.WasherManager;
import fle.core.util.FleFoodStats;

public class PlayerHandler
{
	public PlayerHandler() 
	{
		
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoggedInEvent evt)
	{
		if(!(evt.player.getFoodStats() instanceof FleFoodStats))
		{
	        try
	        {
	        	Class<?> clazz = EntityPlayer.class;
	            Field field = null;
	            for(Field f : clazz.getDeclaredFields())
	            {
	            	try
	            	{
		            	f.setAccessible(true);
		            	if(f.get(evt.player) instanceof FoodStats)
		            	{
		            		field = f;
		            		break;
		            	}
	            	}
	            	catch(Throwable e) {continue;}
	            }
	            if(field == null) throw new NullPointerException("Fle fail to find food state field.");
	            FoodStats stats = (FoodStats) field.get(evt.player);
	            field.set(evt.player, new FleFoodStats(stats));
	        }
	        catch(Throwable e)
	        {
	            e.printStackTrace();
	        }
		}
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void renderHUD(RenderGameOverlayEvent.Pre event)
	{
		if(event.type == ElementType.EXPERIENCE)
		{
			//event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlaceItem(PlaceEvent evt)
	{
		if(evt instanceof MultiPlaceEvent) return;
		if(evt.player != null)
			if(!evt.player.capabilities.isCreativeMode)
				if(evt.itemInHand != null)
					if(evt.itemInHand.getItem() instanceof ItemBlock)
					{
						Block block = Block.getBlockFromItem(evt.itemInHand.getItem());
						if(block == Blocks.log || block == Blocks.log2 || block == IB.log)
						{
							evt.setCanceled(true);
						}
					}
	}

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent evt)
	{
		if(evt.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) evt.entityLiving;
			if(WasherManager.tryWashingItem(evt.entityLiving.worldObj, (EntityPlayer) evt.entityLiving))
				evt.setCanceled(true);
		}
	}
}