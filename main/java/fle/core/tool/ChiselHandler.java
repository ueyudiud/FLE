package fle.core.tool;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.util.U.I;
import farcore.util.U.P;
import flapi.enums.EnumDamageResource;
import flapi.recipe.stack.OreStack;

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
						int id = P.playerHas(evt.getPlayer(), new OreStack("craftingToolHardHammer"));
						if(id != -1 && evt.block.canSilkHarvest(evt.world, evt.getPlayer(), evt.x, evt.y, evt.z, evt.blockMetadata) && (evt.block.getMaterial() == Material.rock || evt.block.getMaterial() == Material.iron))
						{
							if(evt.block.getHarvestLevel(evt.blockMetadata) <= level && evt.block.getHarvestTool(evt.blockMetadata) == "pickaxe")
							{
								ItemStack drop = new ItemStack(evt.block, 1, evt.block.getDamageValue(evt.world, evt.x, evt.y, evt.z));
								StoneHammerHandler.dropBlockAsItem(evt.world, evt.x, evt.y, evt.z, drop);
								I.damageItem(evt.getPlayer(), evt.getPlayer().inventory.getStackInSlot(id), EnumDamageResource.UseTool, 0.75F);
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