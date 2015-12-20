package fle.core.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;

public class CeramicsRecipe 
{
	private static List<CeramicsRecipe> list = new ArrayList();
	
	static
	{
		registerRecipe(new CeramicsRecipe(new ItemStack(IB.argil_unsmelted, 1, 0), new float[][]{
			{0.5F, 0.625F}, {0.4375F, 0.5625F}, {0.875F, 1.0F}, {0.9375F, 1.0F}, {0.5F, 0.625F},
			{0.5F, 0.625F}, {0.4375F, 0.5625F}, {0.875F, 1.0F}, {0.9375F, 1.0F}, {0.5F, 0.625F}}));
		registerRecipe(new CeramicsRecipe(ItemFleSub.a("argil_brick", 4), new float[][]{
			{0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F},
			{0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}}));
		registerRecipe(new CeramicsRecipe(ItemFleSub.a("argil_plate", 4), new float[][]{
			{0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F},
			{0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}}));
		registerRecipe(new CeramicsRecipe(ItemFleSub.a("jug_argil_unsmelted"), new float[][]{
			{0.125F, 0.25F}, {0.9F, 0.8F}, {0.8F, 0.7F}, {0.7F, 0.55F}, {0.6F, 0.4F},
			{0.125F, 0.25F}, {0.9F, 0.8F}, {0.8F, 0.7F}, {0.7F, 0.55F}, {0.6F, 0.4F}}));
	}	
	public static void registerRecipe(CeramicsRecipe recipe)
	{
		list.add(recipe);
	}
	
	public static ItemStack getRecipeResult(float...fs)
	{
		for(CeramicsRecipe recipe : list)
		{
			if(recipe.match(fs))
			{
				return recipe.output.copy();
			}
		}
		return null;
	}
	
	public static List<CeramicsRecipe> getRecipeList()
	{
		return list;
	}
	
	private float[][] recipeMap;
	private ItemStack output;
	
	public CeramicsRecipe(ItemStack aOutput, float[]...fs) 
	{
		recipeMap = fs;
		output = aOutput.copy();
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

	public ItemStack getOutput()
	{
		return output.copy();
	}
}