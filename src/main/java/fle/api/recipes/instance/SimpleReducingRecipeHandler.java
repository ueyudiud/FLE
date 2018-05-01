/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farcore.data.MC;
import farcore.items.ItemOreChip;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import fle.api.recipes.IRecipeMap;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class SimpleReducingRecipeHandler
implements IRecipeMap<SimpleReducingRecipeHandler.Recipe, SimpleReducingRecipeHandler.Cache, ItemStack>
{
	public class Recipe
	{
		Mat source;
		Mat target;
	}
	
	public class Cache
	{
		public int duration;
		public ItemStack output;
	}
	
	private final List<Recipe> recipes = new ArrayList<>();
	
	SimpleReducingRecipeHandler()
	{
	}
	
	@Override
	public Cache readFrom(NBTTagCompound nbt)
	{
		Cache cache = new Cache();
		cache.duration = nbt.getInteger("duration");
		cache.output = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("output"));
		return cache;
	}
	
	@Override
	public void writeTo(Cache target, NBTTagCompound nbt)
	{
		nbt.setInteger("duration", target.duration);
		NBTTagCompound nbt1 = new NBTTagCompound();
		target.output.writeToNBT(nbt1);
		nbt.setTag("output", nbt1);
	}
	
	@Override
	public String getRegisteredName()
	{
		return "fle.simple.reducing";
	}
	
	@Override
	public boolean addRecipe(Recipe recipe)
	{
		return this.recipes.add(recipe);
	}
	
	@Override
	public Cache findRecipe(ItemStack handler)
	{
		if (handler == null) return null;
		if (handler.getItem() instanceof ItemOreChip && handler.stackSize == 4)
		{
			Mat material = ItemOreChip.getMaterial(handler);
			for (Recipe recipe : this.recipes)
			{
				if (matchRecipe(recipe, material))
				{
					Cache cache = new Cache();
					cache.duration = 2000;
					cache.output = ItemMulti.createStack(recipe.target, MC.impure_reduced_pile);
					return cache;
				}
			}
		}
		return null;
	}
	
	protected boolean matchRecipe(Recipe recipe, Mat handler)
	{
		return recipe.source == handler;
	}
	
	@Override
	public Collection<Recipe> recipes()
	{
		return this.recipes;
	}
	
	public void removeRecipeByHandler(Mat handler)
	{
		this.recipes.removeIf(L.toPredicate(this::matchRecipe, handler));
	}
}
