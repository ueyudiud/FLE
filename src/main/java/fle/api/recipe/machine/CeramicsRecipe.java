package fle.api.recipe.machine;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.stack.AbstractStack;
import net.minecraft.item.ItemStack;

public class CeramicsRecipe 
{
	private static List<CeramicsRecipe> list = new ArrayList();
		
	public static void registerRecipe(CeramicsRecipe recipe)
	{
		list.add(recipe);
	}
	
	public static ItemStack getRecipeResult(ItemStack input, float...fs)
	{
		for(CeramicsRecipe recipe : list)
		{
			if(!recipe.input.valid()) continue;
			if(recipe.input.contain(input) &&
					recipe.match(fs))
			{
				return recipe.output.copy();
			}
		}
		return null;
	}
	
	public static CeramicsRecipe getRecipe(ItemStack input, float...fs)
	{
		for(CeramicsRecipe recipe : list)
		{
			if(!recipe.input.valid()) continue;
			if(recipe.input.contain(input) &&
					recipe.match(fs))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static List<CeramicsRecipe> getRecipeList()
	{
		return list;
	}
	
	private AbstractStack input;
	private float[][] recipeMap;
	private ItemStack output;
	
	public CeramicsRecipe(AbstractStack input, ItemStack output, float[]...fs) 
	{
		this.input = input;
		this.recipeMap = fs;
		this.output = output.copy();
	}
	
	public boolean match(float[] fs)
	{
		if(fs.length != recipeMap.length) return false;
		for(int i = 0; i < fs.length; ++i)
		{
			float a1 = Math.min(recipeMap[i][0], recipeMap[i][1]);
			float a2 = Math.max(recipeMap[i][0], recipeMap[i][1]);
			if(a1 > fs[i] || a2 < fs[i]) return false;
		}
		return true;
	}

	public float[] getDefaultValue()
	{
		float[] fs = new float[10];
		for(int i = 0; i < fs.length; ++i)
		{
			fs[i] = (recipeMap[i][0] + recipeMap[i][1]) / 2;
		}
		return fs;
	}
	
	public AbstractStack getInput()
	{
		return input;
	}

	public ItemStack getOutput()
	{
		return output.copy();
	}
}