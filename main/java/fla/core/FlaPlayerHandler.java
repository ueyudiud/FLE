package fla.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import fla.api.FlaAPI;
import fla.api.tech.Technology;

public class FlaPlayerHandler 
{	
	@SubscribeEvent
	public void openGui(PlayerOpenContainerEvent event)
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
	public void onPlayerLogIn(PlayerLoggedInEvent evt)
	{
		if(evt.player instanceof EntityPlayerMP)
		{
			for(Technology tech : FlaAPI.techManager.getPlayerInfo(evt.player).getPlayerTechList())
				Fla.fla.nwm.get().initatePlayerTechupdate((EntityPlayerMP) evt.player, tech, (byte) 0);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogOut(PlayerLoggedOutEvent evt)
	{
		if(Fla.fla.p.get().isSimulating())
		{
			Fla.fla.km.get().removePlayerReferences(evt.player);
		}
	}
	
	@SubscribeEvent
	public void onPlaceItem(PlaceEvent evt)
	{
		if(evt.player != null)
			if(!evt.player.capabilities.isCreativeMode)
			{
				if(evt.block == Blocks.log || evt.block == Blocks.log2)
				{
					evt.setCanceled(true);
				}
			}
	}
}