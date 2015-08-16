package fle.api.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class ItemAbstractStack 
{
	public abstract boolean isStackEqul(ItemStack item);

	public abstract boolean isStackEqul(FluidStack item);
	
	public abstract boolean isStackEqul(ItemAbstractStack stack);
	
	public abstract List<ItemStack> toArray();
	
	@Override
	public String toString()
	{
		ItemStack tStack = toArray().get(0);
		return "stack.abstract." + tStack.getUnlocalizedName() + "x" + tStack.stackSize;
	}
}
