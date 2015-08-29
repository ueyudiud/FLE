package fle.core.tool;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.recipe.ItemOreStack;

public class ChiselHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHaevest(BreakEvent evt)
	{
		if (evt.getPlayer() != null)
			if (!evt.getPlayer().capabilities.isCreativeMode)
				if (evt.getPlayer().getCurrentEquippedItem() != null)
				{
					ItemStack tStack = evt.getPlayer().getCurrentEquippedItem();
					if(tStack.getItem().getHarvestLevel(tStack, "chisel") >= 0)
					{
						int level = tStack.getItem().getHarvestLevel(tStack, "chisel");
						int id = FleAPI.dosePlayerHas(evt.getPlayer(), new ItemOreStack("craftingToolHardHammer"));
						if(id != -1 && evt.block.canSilkHarvest(evt.world, evt.getPlayer(), evt.x, evt.y, evt.z, evt.blockMetadata))
						{
							if(evt.block.getHarvestLevel(evt.blockMetadata) <= level && evt.block.getHarvestTool(evt.blockMetadata) == "pickaxe")
							{
								ItemStack drop = new ItemStack(evt.block, 1, evt.block.getDamageValue(evt.world, evt.x, evt.y, evt.z));
								StoneHammerHandler.dropBlockAsItem(evt.world, evt.x, evt.y, evt.z, drop);
								FleAPI.damageItem(evt.getPlayer(), evt.getPlayer().inventory.getStackInSlot(id), EnumDamageResource.UseTool, 0.75F);
								if(evt.getPlayer().inventory.getStackInSlot(id).stackSize <= 0)
								{
									evt.getPlayer().inventory.setInventorySlotContents(id, null);
								}
							}
						}
					}
				}
	}
}