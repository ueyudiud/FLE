/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farcore.lib.material.Mat;
import fle.api.recipes.IRecipeMap;
import nebula.base.ArrayListAddWithCheck;
import nebula.base.ObjArrayParseHelper;
import nebula.base.Stack;
import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class MaterialPoolInputRecipeHandler
implements IRecipeMap<MaterialPoolInputRecipeHandler.Recipe, List<Stack<Mat>>, ItemStack>
{
	static class Recipe
	{
		AbstractStack match;
		Object[] outputs;
		
		Recipe(AbstractStack input, Object[] stacks)
		{
			this.match = input;
			this.outputs = stacks;
		}
	}
	
	private final String name;
	private final List<MaterialPoolInputRecipeHandler.Recipe> recipes = new ArrayList<>();
	
	public MaterialPoolInputRecipeHandler(String name)
	{
		this.name = name;
	}
	
	@Override
	public List<Stack<Mat>> readFromNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void writeToNBT(List<Stack<Mat>> target, NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	public void addRecipe(AbstractStack input, Object...objects)
	{
		List<Stack<Mat>> stacks = new ArrayList<>();
		ObjArrayParseHelper helper = ObjArrayParseHelper.create(objects);
		while (helper.hasNext())
		{
			if (helper.match(Stack.class))
			{
				stacks.add(helper.read());
			}
			else if (helper.match(Mat.class, Number.class))
			{
				stacks.add(new Stack(helper.read(), helper.read()));
			}
			else if (helper.match(Mat.class))
			{
				stacks.add(new Stack(helper.read()));
			}
			else throw new IllegalArgumentException("Illegal material pool input recipe table, got: " + input + "=>" + helper.toString());
		}
		addRecipe(input, stacks);
	}
	
	public void addRecipe(AbstractStack input, List<Stack<Mat>> stacks)
	{
		addRecipe(new Recipe(input, stacks.toArray()));
	}
	
	@Override
	public boolean addRecipe(Recipe recipe)
	{
		return this.recipes.add(recipe);
	}
	
	@Override
	public List<Stack<Mat>> findRecipe(ItemStack stack)
	{
		for (Recipe recipe : this.recipes)
		{
			if (recipe.match.contain(stack))
			{
				return ArrayListAddWithCheck.argument(recipe.outputs);
			}
		}
		return null;
	}
	
	@Override
	public Collection<Recipe> recipes()
	{
		return this.recipes;
	}
	
	@Override
	public void removeRecipeByHandler(ItemStack stack)
	{
		this.recipes.removeIf(r->r.match.similar(stack));
	}
}