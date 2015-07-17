package fla.core.gui.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import fla.api.util.FlaValue;

public class InvtnroySingle extends InventoryBase
{
	private boolean dropStack;
	protected String name;

	public InvtnroySingle(boolean drop, String name, Container container) 
	{
		super(container, 1);
		this.dropStack = drop;
		this.name = name;
	}

	@Override
	public String getInventoryName() 
	{
		return name;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}
}