package flapi.recipe.stack;

import java.util.List;

import net.minecraft.item.ItemStack;

public abstract class AbstractStack
{
	public abstract boolean contain(ItemStack stack);
	
	public abstract boolean similar(ItemStack stack);
	
	public abstract int size(ItemStack stack);
	
	public abstract List<ItemStack> list();
	
	public abstract ItemStack[] toList();
	
	public abstract ItemStack instance();
}