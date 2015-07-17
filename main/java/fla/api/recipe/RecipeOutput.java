package fla.api.recipe;

import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface RecipeOutput 
{
	public String getOutputName();
	
	public ItemStack getOutputWithRandomChoise(NBTTagCompound nbt, Random rand);
	
	/**
	 * Get output of this stack to show in NEI.
	 * @return list of output.
	 */
	public List<ItemStack> getOutputs();
}
