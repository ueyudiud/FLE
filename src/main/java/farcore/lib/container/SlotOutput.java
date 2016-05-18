package farcore.lib.container;

import farcore.handler.FarCoreAchievementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotOutput extends SlotBase
{
	public SlotOutput(IInventory inventory, int id, int x, int y)
	{
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		FarCoreAchievementHandler.postCrafted(player, stack);
	}
}