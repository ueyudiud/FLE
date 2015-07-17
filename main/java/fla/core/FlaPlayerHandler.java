package fla.core;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Start;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import fla.api.item.IPlaceableItem;
import fla.core.util.Keyboard;

public class FlaPlayerHandler 
{
	@SubscribeEvent
	public void onPlayerUseItem(PlaceEvent evt)
	{
		if(evt.block == Blocks.log || evt.block == Blocks.log2)
		{
			evt.setCanceled(true);
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