package fle.tool;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flapi.block.IHarvestHandler;

public class ToolHandler
{
	@SubscribeEvent
	public void checkHarvest(BreakEvent evt)
	{
		if(evt.world.isRemote) return;
		if(evt.block instanceof IHarvestHandler)
		{
			boolean result = true;
			String tool = null;
			int level = -1;
			if(evt.getPlayer() != null)
			{
				if(evt.getPlayer().getCurrentEquippedItem() == null)
				{
					result = ((IHarvestHandler) evt.block).canHarvestBlock(evt.world, evt.x, evt.y, evt.z, evt.blockMetadata, tool = "hand", -1);
				}
				else
				{
					Item item = evt.getPlayer().getCurrentEquippedItem().getItem();
					Set<String> set = item.getToolClasses(evt.getPlayer().getCurrentEquippedItem());
					for(String tool1 : set)
					{
						level = item.getHarvestLevel(evt.getPlayer().getCurrentEquippedItem(), tool1);
						result = ((IHarvestHandler) evt.block).canHarvestBlock(evt.world, evt.x, evt.y, evt.z, evt.blockMetadata, tool = tool1, level);
						if(result) break;
					}
					if(tool == null)
					{
						tool = "item";
						level = -1;
					}
				}
			}
			if(result)
			{
				ArrayList<ItemStack> drops = ((IHarvestHandler) evt.block).getHarvestDrop(evt.world, evt.x, evt.y, evt.z, evt.blockMetadata, tool, level);
				HarvestDropsEvent evt1 = new HarvestDropsEvent(evt.x, evt.y, evt.z, evt.world, evt.block, evt.blockMetadata, 0, 1.0F, drops, evt.getPlayer(), false);
				MinecraftForge.EVENT_BUS.post(evt1);
				for(ItemStack stack : evt1.drops)
				{
					dropBlockAsItem(evt.world, evt.x, evt.y, evt.z, stack);
				}
			}
		}
	}
	
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
	
	@SubscribeEvent
	public void cancelHarvest(HarvestCheck evt)
	{
		if(evt.block instanceof IHarvestHandler) evt.success = false;
	}
}