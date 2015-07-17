package fla.core.tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fla.api.tech.Page;

public class ContainerPage extends Container
{
	private Page page;

	public ContainerPage(Page page)
	{
		this.page = page;
		for(int i = 0; i < page.getStacks().getSizeInventory(); ++i)
		{
			addSlotToContainer(new Slot(page.getStacks(), i, page.getXPosFromId(i), page.getYPosFromId(i)));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int id)
	{
		return getSlot(id) != null ? getSlot(id).getStack() : null;
	}
}