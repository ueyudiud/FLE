package fle.core.handler;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.world.BlockEvent.MultiPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fle.api.FleAPI;
import fle.api.recipe.ItemBaseStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;
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
	
	private int buf = 0;

	@SubscribeEvent
	public void onPlayerUpdate(PlayerTickEvent evt)
	{
		EntityPlayer player = (EntityPlayer) evt.player;
		if(WasherManager.tryWashingItem(player.worldObj, player))
			evt.setCanceled(true);
		if(buf++ > 100)
		{
			buf = 0;
			if(FleAPI.dosePlayerHas(player, new ItemBaseStack(ItemFleSub.a("stone_a"))) != -1)
			{
				givePlayerBook(player, "oldStoneAge", ItemFleSub.a("guide_book_1"));
			}
			if(FleAPI.dosePlayerHas(player, new ItemBaseStack(ItemFleSub.a("ingot_cu"))) != -1)
			{
				givePlayerBook(player, "newStoneAge", ItemFleSub.a("guide_book_2"));
			}
		}
	}
	
	private void givePlayerBook(EntityPlayer player, String tag, ItemStack aStack)
	{
		if(player.worldObj.isRemote) return;
		if(!player.getEntityData().hasKey("FLE"))
		{
			player.getEntityData().setTag("FLE", new NBTTagCompound());
		}
		if(!player.getEntityData().getCompoundTag("FLE").hasKey("CGBook"))
		{
			player.getEntityData().getCompoundTag("FLE").setTag("CGBook", new NBTTagCompound());
		}
		if(!player.getEntityData().getCompoundTag("FLE").getCompoundTag("CGBook").getBoolean(tag))
		{
			int slotID = player.inventory.getFirstEmptyStack();
			if(slotID != -1)
			{
				player.inventory.setInventorySlotContents(slotID, aStack);
			}
			else
			{
				player.dropPlayerItemWithRandomChoice(aStack, false);
			}
			player.getEntityData().getCompoundTag("FLE").getCompoundTag("CGBook").setBoolean(tag, true);
		}
	}
}