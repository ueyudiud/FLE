package fle.core.handler;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.MultiPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.net.FlePackets.CoderFWMUpdate;
import fle.api.recipe.ItemBaseStack;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.tool.WasherManager;
import fle.core.util.FleFoodStats;
import fle.core.util.FlePotionEffect;
import fle.core.util.Util;

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
				Util.overrideField(EntityPlayer.class, Arrays.asList("foodStats", "field_71100_bB"), evt.player, 
						new FleFoodStats((FoodStats) Util.getValue(EntityPlayer.class, Arrays.asList("foodStats", "field_71100_bB"), evt.player)), true);
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
				{
					if(evt.itemInHand.getItem() instanceof ItemBlock)
					{
						Block block = Block.getBlockFromItem(evt.itemInHand.getItem());
						if(block == Blocks.log || block == Blocks.log2 || block == IB.log)
						{
							evt.setCanceled(true);
						}
					}
					else if(evt.itemInHand.getItem() == Items.wheat_seeds)
					{
						evt.setCanceled(true);
					}
				}
	}
	
	@SubscribeEvent
	public void onPlayerHarvest(HarvestDropsEvent evt)
	{
		if(evt.block == Blocks.reeds)
		{
			evt.drops.clear();
			evt.drops.add(ItemFleSeed.a("suger_cances"));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onAttack(LivingAttackEvent evt)
	{
		if(evt.source instanceof EntityDamageSource)
		{
			if(evt.entityLiving instanceof EntityPlayer || evt.entityLiving instanceof IAnimals)
			{
				if(evt.ammount > 10.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.bleeding.id, 1000, 2));
				else if(evt.ammount > 4.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.bleeding.id, 1000, 1));
				else evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.bleeding.id, 1000, 0));
			}
		}
		else if(evt.source == DamageSource.fall)
		{
			if(evt.ammount > 10.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 2));
			else if(evt.ammount > 4.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 1));
			else evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 0));
		}
	}
	
	private int buf = 0;

	@SubscribeEvent
	public void onPlayerUpdate(PlayerTickEvent evt)
	{
		EntityPlayer player = (EntityPlayer) evt.player;
		WasherManager.tryWashingItem(player.worldObj, player);
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
			int i;
			i = FleAPI.dosePlayerHas(player, new ItemBaseStack(Items.wheat_seeds));
			if(i != -1)
			{
				player.inventory.setInventorySlotContents(i, ItemFleSeed.a(player.inventory.getStackInSlot(i).stackSize, "wheat"));
			}
			i = FleAPI.dosePlayerHas(player, new ItemBaseStack(Items.reeds));
			if(i != -1)
			{
				player.inventory.setInventorySlotContents(i, ItemFleSeed.a(player.inventory.getStackInSlot(i).stackSize, "suger_cances"));
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