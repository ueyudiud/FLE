/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import farcore.lib.material.Mat;
import fle.api.mat.StackContainer;
import fle.api.recipes.IRecipeMap;
import nebula.base.Judgable;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class MaterialPoolOutputRecipeHandler implements IRecipeMap<Function<StackContainer<Mat>, ItemStack>, ItemStack, StackContainer<Mat>>
{
	private final String											name;
	private final List<Function<StackContainer<Mat>, ItemStack>>	recipes	= new ArrayList<>();
	
	public MaterialPoolOutputRecipeHandler(String name)
	{
		this.name = name;
	}
	
	@Override
	public void writeToNBT(ItemStack target, NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ItemStack readFromNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public boolean addRecipe(Function<StackContainer<Mat>, ItemStack> recipe)
	{
		return this.recipes.add(recipe);
	}
	
	@Override
	public ItemStack findRecipe(StackContainer<Mat> handler)
	{
		for (Function<StackContainer<Mat>, ItemStack> function : this.recipes)
		{
			ItemStack stack = function.apply(handler);
			if (stack != null) return stack;
		}
		return null;
	}
	
	@Override
	public Collection<Function<StackContainer<Mat>, ItemStack>> recipes()
	{
		return this.recipes;
	}
	
	@Override
	public void removeRecipeByHandler(StackContainer<Mat> handler)
	{
		this.recipes.removeIf(Judgable.NOT_NULL.from(L.funtional(handler)));
	}
}
