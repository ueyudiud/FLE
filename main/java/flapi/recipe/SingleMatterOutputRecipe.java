package flapi.recipe;

import net.minecraft.item.ItemStack;
import flapi.chem.base.MatterStack;

public interface SingleMatterOutputRecipe
{
	boolean matchInput(ItemStack stack);
	
	ItemStack[] getShowStack();
	
	MatterStack getOutput(ItemStack stack);
}