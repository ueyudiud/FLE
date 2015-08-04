package fle.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.tool.WasherManager;

public class PlayerHandler
{
	public PlayerHandler() 
	{
		
	}
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent evt)
	{
		
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
		if(evt.player != null)
			if(!evt.player.capabilities.isCreativeMode)
			{
				if(evt.block == Blocks.log || evt.block == Blocks.log2 || evt.block == IB.log)
				{
					evt.setCanceled(true);
				}
			}
	}
	

	@SubscribeEvent
	public void onWashItem(LivingUpdateEvent evt)
	{
		if(evt.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) evt.entityLiving;
			if(WasherManager.tryWashingItem(evt.entityLiving.worldObj, (EntityPlayer) evt.entityLiving))
				evt.setCanceled(true);
		}
	}
}