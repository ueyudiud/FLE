package farcore.handler;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.interfaces.item.IContainerItemCollectable;
import farcore.util.FleLog;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
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
}