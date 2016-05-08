package farcore.lib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotHolo extends SlotBase
{
	public SlotHolo(IInventory inventory, int id, int x, int y)
	{
		super(inventory, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public ItemStack decrStackSize(int size)
	{
		return null;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		;
	}
}