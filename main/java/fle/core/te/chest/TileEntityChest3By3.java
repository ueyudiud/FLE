package fle.core.te.chest;

import net.minecraft.item.ItemStack;

public class TileEntityChest3By3 extends TileEntityAbstractChest
{
	public TileEntityChest3By3()
	{
		super(9);
	}

	@Override
	protected String getDefaultName()
	{
		return "inventory.small.chest";
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}	
}