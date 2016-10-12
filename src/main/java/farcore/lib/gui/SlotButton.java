package farcore.lib.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotButton extends SlotTool
{
	private ItemStack currentStack;

	public SlotButton(int index, int xPosition, int yPosition)
	{
		super(null, index, xPosition, yPosition);
	}

	@Override
	public void putStack(ItemStack stack)
	{
		currentStack = ItemStack.copyItemStack(stack);
	}

	/**
	 * Get stack from this method please.
	 * @return
	 */
	public ItemStack getCurrentStack()
	{
		return currentStack;
	}
	
	@Override
	public boolean getHasStack()
	{
		return false;
	}
	
	@Override
	public boolean isHere(IInventory inv, int slotIn)
	{
		return false;
	}
	
	@Override
	public boolean isSameInventory(Slot other)
	{
		return false;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
}