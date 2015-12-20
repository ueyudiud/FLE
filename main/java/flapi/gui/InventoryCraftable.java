package flapi.gui;

import net.minecraft.item.ItemStack;

public abstract class InventoryCraftable<T extends ContainerCraftable> extends InventoryBase<T>
{
	private String name;

	public InventoryCraftable(String name, T container, int i) 
	{
		super(container, i);
		this.name = name;
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		ItemStack ret = stacks[i].copy();
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		container.onCraftMatrixChanged(this);
		if(isInputSlot(i))
			((ContainerCraftable) container).onInputChanged(this, i);
		if(isOutputSlot(i))
			((ContainerCraftable) container).onOutputChanged(this, i);
		return ret;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		if(stack == null)
		{
			stacks[i] = null;
			container.onCraftMatrixChanged(this);
			if(isInputSlot(i))
				((ContainerCraftable) container).onInputChanged(this, i);
			if(isOutputSlot(i))
				((ContainerCraftable) container).onOutputChanged(this, i);
		}
		else
		{
			stacks[i] = stack.copy();
			container.onCraftMatrixChanged(this);
			if(isInputSlot(i))
				((ContainerCraftable) container).onInputChanged(this, i);
			if(isOutputSlot(i))
				((ContainerCraftable) container).onOutputChanged(this, i);
		}
	}

	@Override
	public String getInventoryName() 
	{
		return name;
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return isInputSlot(i) ? true : (isOutputSlot(i) ? false : isItemValidForAbstractSlot(i, itemstack));
	}
	
	protected abstract boolean isItemValidForAbstractSlot(int i, ItemStack itemstack);
	
	protected abstract boolean isInputSlot(int i);
	
	protected abstract boolean isOutputSlot(int i);
}