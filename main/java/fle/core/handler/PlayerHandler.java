package fle.core.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import farcore.util.U.P;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.OreStack;
import fle.FLE;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.util.FleFoodStats;
import fle.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.MultiPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.WorldEvent.CreateSpawnPosition;

public class PlayerHandler
{
	private static Map<UUID, NBTTagCompound> map = new HashMap();
	
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

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerRebirth(PlayerRespawnEvent evt)
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
		if(map.containsKey(evt.player.getUniqueID()))
		{
			evt.player.getEntityData().setTag("FLE", map.get(evt.player.getUniqueID()));
			evt.player.experienceLevel = evt.player.getEntityData().getCompoundTag("FLE").getInteger("ExpLevel");
		}
		if(evt.player.experienceLevel > -128)
		{
			--evt.player.experienceLevel;
		}
	}
	
	@SubscribeEvent
	public void onPlayerDead(LivingDeathEvent evt)
	{
		if(evt.entityLiving instanceof EntityPlayer)
		{
			((EntityPlayer) evt.entityLiving).getEntityData().getCompoundTag("FLE").setInteger("ExpLevel", ((EntityPlayer) evt.entityLiving).experienceLevel);
			map.put(((EntityPlayer) evt.entityLiving).getUniqueID(), ((EntityPlayer) evt.entityLiving).getEntityData().getCompoundTag("FLE"));
		}
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void onPlayerOpenBag(PlayerOpenContainerEvent evt)
	{
		
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderHUD(RenderGameOverlayEvent.Pre evt)
	{
		Minecraft mc;
		int width;
		int height;
		if(evt.type == ElementType.EXPERIENCE)
		{
			width = evt.resolution.getScaledWidth();
			height = evt.resolution.getScaledHeight();
			mc = Minecraft.getMinecraft();
			
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_BLEND);

	        if (mc.playerController.gameIsSurvivalOrAdventure())
	        {
	            mc.mcProfiler.startSection("expBar");
	            int cap = mc.thePlayer.xpBarCap();
	            int left = width / 2 - 91;

	            if (cap > 0)
	            {
	                short barWidth = 182;
	                int filled = (int)(mc.thePlayer.experience * (float)(barWidth + 1));
	                int top = height - 32 + 3;
	                drawTexturedModalRect(left, top, 0, 64, barWidth, 5);

	                if (filled > 0)
	                {
	                	drawTexturedModalRect(left, top, 0, 69, filled, 5);
	                }
	            }

	            mc.mcProfiler.endSection();

	            if (mc.playerController.gameIsSurvivalOrAdventure() && mc.thePlayer.experienceLevel != 0)
	            {
	                mc.mcProfiler.startSection("expLevel");
	                boolean flag1 = mc.thePlayer.experienceLevel < 0;
	                int color = flag1 ? 0xAD2500 : 0x80FF20;
	                String text = "" + mc.thePlayer.experienceLevel;
	                int x = (width - mc.fontRenderer.getStringWidth(text)) / 2;
	                int y = height - 31 - 4;
	                mc.fontRenderer.drawString(text, x + 1, y, 0);
	                mc.fontRenderer.drawString(text, x - 1, y, 0);
	                mc.fontRenderer.drawString(text, x, y + 1, 0);
	                mc.fontRenderer.drawString(text, x, y - 1, 0);
	                mc.fontRenderer.drawString(text, x, y, color);
	                mc.mcProfiler.endSection();
	            }
	        }
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        evt.setCanceled(true);
		}
	}
	
    private void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double) 0, (double)((float)(u + 0) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double) 0, (double)((float)(u + w) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double) 0, (double)((float)(u + w) * f), (double)((float)(v + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double) 0, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
        tessellator.draw();
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
						if(block == Blocks.log || block == Blocks.log2)// || block instanceof BlockFleLog)
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
		if((evt.block == Blocks.dirt || evt.block == Blocks.grass || evt.block == Blocks.mycelium || evt.block == Blocks.hay_block) && !evt.isSilkTouching)
		{
			evt.drops.clear();
			evt.drops.add(ItemFleSub.a("pile_dirt", 3));
		}
		else if(evt.block == Blocks.gravel)
		{
			evt.drops.clear();
			evt.drops.add(ItemFleSub.a("pile_gravel", 3));
		}
		else if(evt.block == Blocks.sand)
		{
			evt.drops.clear();
			evt.drops.add(ItemFleSub.a("pile_sand", 3));
		}
		else if(evt.block == Blocks.reeds)
		{
			evt.drops.clear();
			evt.drops.add(ItemFleSeed.a("sugar_cances"));
		}
		else if(evt.block == Blocks.vine)
		{
			if(!evt.isSilkTouching)
			{
				evt.drops.add(ItemFleSub.a("rattan"));
			}
		}
		else if(evt.block == Blocks.grass)
		{
			for(ItemStack tStack : Util.copy(evt.drops))
			{
				if(tStack.getItem() == Items.wheat_seeds)
					evt.drops.remove(tStack);
			}
		}
	}
	
	private int buf = 0;

	@SubscribeEvent
	public void onPlayerUpdate(PlayerTickEvent evt)
	{
		EntityPlayer player = (EntityPlayer) evt.player;
//		WasherManager.tryWashingItem(player.worldObj, player);
		if(FLE.fle.getKeyboard().isTechKeyDown(player) && player.openContainer.isPlayerNotUsingContainer(player))
		{
			player.openGui(FLE.MODID, -5, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
		if(buf++ > 100)
		{
			buf = 0;
			if(P.playerHas(player, new BaseStack(ItemFleSub.a("chip_stone"))) != -1)
			{
				givePlayerBook(player, "oldStoneAge", ItemFleSub.a("guide_book_1"));
			}
			if(P.playerHas(player, new BaseStack(ItemFleSub.a("ingot_cu"))) != -1)
			{
				givePlayerBook(player, "newStoneAge", ItemFleSub.a("guide_book_2"));
			}
			if(P.playerHas(player, new OreStack("ingotAbstractBronze")) != -1)
			{
				givePlayerBook(player, "cooperAge", ItemFleSub.a("guide_book_3"));
			}
			int i;
			i = P.playerHas(player, new BaseStack(Items.wheat_seeds));
			if(i != -1)
			{
				player.inventory.setInventorySlotContents(i, ItemFleSeed.a(player.inventory.getStackInSlot(i).stackSize, "wheat"));
			}
			i = P.playerHas(player, new BaseStack(Items.reeds));
			if(i != -1)
			{
				player.inventory.setInventorySlotContents(i, ItemFleSeed.a(player.inventory.getStackInSlot(i).stackSize, "sugar_cances"));
			}
			i = P.playerHas(player, new BaseStack(Items.potato));
			if(i != -1)
			{
				player.inventory.setInventorySlotContents(i, ItemFleSeed.a(player.inventory.getStackInSlot(i).stackSize, "potato"));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerSpawn(CreateSpawnPosition evt)
	{
		if(evt.world.provider.isHellWorld) return;
		int i = 0;
		Random rand = new Random();
		while(i < 1024)
		{
			rand.setSeed(i * (i * 2895387531L) + 2842728719L + evt.world.getSeed());
			int x = (rand.nextInt() ^ 24724927) & 0x3FFF;
			int z = (rand.nextInt() ^ 19472847) & 0x3FFF;
			BiomeGenBase biome = evt.world.getBiomeGenForCoords(x, z);
			if(biome.getTempCategory() != TempCategory.OCEAN)
			{
				int y = evt.world.getTopSolidOrLiquidBlock(x, z);
				if(y > evt.world.provider.getHorizon())
				{
					evt.world.setSpawnLocation(x, y, z);
					evt.setCanceled(true);
					break;
				}
			}
			++i;
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