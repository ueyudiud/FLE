package farcore.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import farcore.interfaces.item.IContainerItemCollectable;
import farcore.util.FarFoodStats;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent.CreateSpawnPosition;

public class FarCorePlayerHandler
{
	private static final int CHECK_LOOP = 2048;
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if(event.item.getEntityItem() == null || event.item.getEntityItem().stackSize <= 0)
		{
			event.setResult(Result.DENY);
			return;
		}
		if(event.entityPlayer != null)
		{
			InventoryPlayer inventory = event.entityPlayer.inventory;
			for(int i = 0; i < inventory.mainInventory.length; ++i)
			{
				if(inventory.mainInventory[i] != null)
				{
					if(inventory.mainInventory[i].getItem() instanceof IContainerItemCollectable)
					{
						ItemStack container = inventory.mainInventory[i];
						IContainerItemCollectable collectable = (IContainerItemCollectable) container.getItem();
						if(collectable.canCollectItemToContainer(container, event.item.getEntityItem()))
						{
							collectable.collectItemToContainer(container, event.item.getEntityItem());
							if(event.item.getEntityItem().stackSize <= 0)
							{
								event.setResult(Result.DENY);
								return;
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerSpawn(CreateSpawnPosition event)
	{
		if(event.world.provider.isHellWorld) return;
		int i = 0;
		Random rand = new Random(event.world.getSeed());
		FleLog.getCoreLogger().info("Looking for spawn coord...");
		List<BiomeGenBase> list = event.world.getWorldChunkManager().getBiomesToSpawnIn();
		while(i < CHECK_LOOP)
		{
			int x = (rand.nextInt() ^ 24724927) & 0x7FFF;
			int z = (rand.nextInt() ^ 19472847) & 0x7FFF;
			BiomeGenBase biome = event.world.getBiomeGenForCoords(x, z);
			if(list.contains(biome))
			{
				int y = event.world.getTopSolidOrLiquidBlock(x, z);
				if(y > event.world.provider.getHorizon())
				{
					event.world.setSpawnLocation(x, y, z);
					event.setCanceled(true);
					FleLog.getCoreLogger().info("Get spawn coord at " + x + ", " + y + ", " + z + " checked " + i + " loop.");
					return;
				}
			}
			++i;
		}
		FleLog.getCoreLogger().info("Fail to get spawn coord.");
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == Side.SERVER)
		{
			if(event.phase == Phase.START)
			{
				if(event.player.openContainer instanceof IUpdatePlayerListBox)
				{
					((IUpdatePlayerListBox) event.player.openContainer).update();
				}
			}
			else if(event.phase == Phase.END)
			{
				updatePlayStat(event.player);
			}
		}
	}
	
	public static void jump(EntityPlayer player)
	{
		FarFoodStats stats = (FarFoodStats) player.getFoodStats();
		if(player.isSprinting())
		{
			stats.addFoodExhaustion(0.1F);
			stats.addWaterExhaustion(0.3F);
		}
		else
		{
			stats.addFoodExhaustion(0.02F);
			stats.addWaterExhaustion(0.08F);
		}
	}
	
	private void updatePlayStat(EntityPlayer player)
	{
		FarFoodStats stats = (FarFoodStats) player.getFoodStats();
		double xM = player.posX - player.prevPosX;
		double yM = player.posY - player.prevPosY;
		double zM = player.posZ - player.prevPosZ;
		if (player instanceof EntityPlayerMP)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			boolean destorying = (boolean) U.Reflect.getValue(ItemInWorldManager.class, Arrays.asList("isDestroyingBlock"), playerMP.theItemInWorldManager);
			if(destorying)
			{
				stats.addFoodExhaustion(0.000625F);
			}
		}
		if (player.ridingEntity == null)
        {
            int i;

            if (player.isInsideOfMaterial(Material.water))
            {
                i = Math.round(MathHelper.sqrt_double(xM * xM + yM * yM + zM * zM) * 100.0F);

                if (i > 0)
                {
                    stats.addFoodExhaustion(0.03F * (float)i * .01F);
                    stats.addWaterExhaustion(0.1F * (float)i * .01F);
                }
            }
            else if (player.isInWater())
            {
                i = Math.round(MathHelper.sqrt_double(xM * xM + zM * zM) * 100.0F);

                if (i > 0)
                {
                	stats.addFoodExhaustion(0.03F * (float)i * .01F);//Use the shortest width of English Channel to divide 1000 unit.
                    stats.addWaterExhaustion(0.075F * (float)i * .01F);
                }
            }
            else if (player.isOnLadder())
            {
                if (yM > 0.0D)
                {
                	stats.addFoodExhaustion(0.05F * (float) yM);
                    stats.addWaterExhaustion(0.08F * (float) yM);
                }
            }
            else if (player.onGround)
            {
                i = Math.round(MathHelper.sqrt_double(xM * xM + zM * zM) * 100.0F);

                if (i > 0)
                {
                	if (player.isSprinting())
                	{
                		stats.addFoodExhaustion(0.08F * (float)i * .01F);
                        stats.addWaterExhaustion(0.25F * (float)i * .01F);
                	}
                	else
                	{
                		stats.addFoodExhaustion(0.0125F * (float)i * .01F);
                        stats.addWaterExhaustion(0.06F * (float)i * .01F);
                	}
                }
            }
//            else
//            {
//            	i = Math.round(MathHelper.sqrt_double(xM * xM + zM * zM) * 100.0F);
//
//                if (i > 25)
//                {
//                    this.addStat(StatList.distanceFlownStat, i);
//                }
//            }
        }
	}
}