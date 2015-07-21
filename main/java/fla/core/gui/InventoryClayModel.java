package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import fla.core.gui.base.InvtnroySingle;

public class InventoryClayModel extends InvtnroySingle
{
	boolean outputed = false;
	
	public InventoryClayModel(ContainerClayModel container) 
	{
		super(true, "inventory.clayModel", container);
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] != null) outputed = true;
		return super.decrStackSize(i, size);
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return super.isUseableByPlayer(player) && !outputed;
	}
}