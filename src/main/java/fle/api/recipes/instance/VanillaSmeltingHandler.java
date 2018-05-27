/*
 * copyright 2016-2018 ueyudiud
 */

package fle.api.recipes.instance;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fle.api.recipes.IRecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ueyudiud
 */
public class VanillaSmeltingHandler implements IRecipeMap<Entry<ItemStack, ItemStack>, ItemStack, ItemStack>
{
	@Override
	public ItemStack readFrom(NBTTagCompound nbt)
	{
		return ItemStack.loadItemStackFromNBT(nbt);
	}
	
	@Override
	public void writeTo(ItemStack target, NBTTagCompound nbt)
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
		if (FurnaceRecipes.instance().getSmeltingResult(recipe.getKey()) != null) return false;
		FurnaceRecipes.instance().addSmeltingRecipe(recipe.getKey(), recipe.getValue(), 0.0F);
		return true;
	}
	
	@Override
	public ItemStack findRecipe(ItemStack stack)
	{
		return FurnaceRecipes.instance().getSmeltingResult(stack);
	}
	
	@Override
	public Set<Entry<ItemStack, ItemStack>> recipes()
	{
		return FurnaceRecipes.instance().getSmeltingList().entrySet();
	}
	
	@Override
	public void removeRecipe(Entry<ItemStack, ItemStack> recipe)
	{
		FurnaceRecipes.instance().getSmeltingList().remove(recipe.getKey(), recipe.getValue());
	}
	
	@Override
	public void removeRecipeByHandler(ItemStack handler)
	{
		Set<ItemStack> set = new HashSet<>();
		Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
		recipes.forEach((s1, s2) -> {
			if (OreDictionary.itemMatches(s1, handler, false)) set.add(s1);
		});
		set.forEach(s -> recipes.remove(s));
	}
}
