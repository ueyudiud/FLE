/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import java.util.Collection;
import java.util.Map.Entry;

import fle.api.recipes.IRecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class VanillaSmeltingHandler implements IRecipeMap<Entry<ItemStack, ItemStack>, ItemStack, ItemStack>
{
	@Override
	public ItemStack readFromNBT(NBTTagCompound nbt)
	{
		return ItemStack.loadItemStackFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(ItemStack target, NBTTagCompound nbt)
	{
		target.writeToNBT(nbt);
	}
	
	@Override
	public String getRegisteredName()
	{
		return "minecraft.smelting";
	}
	
	@Override
	public boolean addRecipe(Entry<ItemStack, ItemStack> recipe)
	{
		if (FurnaceRecipes.instance().getSmeltingResult(recipe.getKey()) != null)
			return false;
		FurnaceRecipes.instance().addSmeltingRecipe(recipe.getKey(), recipe.getValue(), 0.0F);
		return true;
	}
	
	@Override
	public ItemStack findRecipe(ItemStack stack)
	{
		return FurnaceRecipes.instance().getSmeltingResult(stack);
	}
	
	@Override
	public Collection<Entry<ItemStack, ItemStack>> recipes()
	{
		return FurnaceRecipes.instance().getSmeltingList().entrySet();
	}
}