package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotOutput;
import fle.api.item.ICastingTool;
import fle.core.te.TileEntityCastingPool;

public class ContainerCastingPool extends ContainerCraftable
{
	public ContainerCastingPool(InventoryPlayer player, TileEntityCastingPool tile)
	{
		super(player, tile, 0, 0);
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				addSlotToContainer(new Slot(tile, i + j * 3, 61 + 17 * i, 19 + 17 * j)
				{
					@Override
					public boolean isItemValid(ItemStack stack)
					{
						return stack.getItem() instanceof ICastingTool ? ((ICastingTool) stack.getItem()).isCastingTool(stack) : false;
					}
				});
		addSlotToContainer(new Slot(tile, 9, 26, 13));
		addSlotToContainer(new SlotOutput(tile, 10, 26, 57));
		addSlotToContainer(new SlotOutput(tile, 11, 130, 52));
		locateRecipeInput = new TransLocation("input", 9 + 36);
		locateRecipeOutput = new TransLocation("output", 10 + 36);
	}
}