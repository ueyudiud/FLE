package farcore.lib.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot
{
	private String[] transferTarget = {};
	
	public SlotBase(IInventory inventory, int id, int x, int y)
	{
		super(inventory, id, x, y);
	}
	
	public SlotBase addTransferTarget(String...strings)
	{
		this.transferTarget = strings;
		return this;
	}
	
	public String[] getTransferTarget()
	{
		return transferTarget;
	}
}