package fle.core.tool;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.SingleInputRecipe;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;

public class BurnHandler
{
	private static final List<SingleInputRecipe> burnList = new ArrayList();
	
	public static void init()
	{
		addNewBurnRecipe(new FirestarterInput(true, new OreStack("craftingToolFirestarter")));
		addNewBurnRecipe(new FirestarterInput(false, new OreStack("craftingResourceTinder")));
	}
	
	public static SingleInputRecipe getRecipe(ItemStack stack)
	{
		for(SingleInputRecipe recipe : burnList)
		{
			if(recipe.match(stack))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static boolean isItemFirable(ItemStack stack)
	{
		for(SingleInputRecipe recipe : burnList)
		{
			if(recipe.match(stack)) return true;
		}
		return false;
	}
	
	public static void addNewBurnRecipe(SingleInputRecipe recipe)
	{
		burnList.add(recipe);
	}
	
	public static class FirestarterInput implements SingleInputRecipe
	{
		float damage = .5F;
		boolean isTool;
		private ItemAbstractStack stack;
		
		public FirestarterInput(boolean flag, ItemAbstractStack stack)
		{
			this.isTool = flag;
			this.stack = stack;
		}
		
		public FirestarterInput setDamage(float damage)
		{
			this.damage = damage;
			return this;
		}

		@Override
		public boolean match(ItemStack aInput)
		{
			return stack.equal(aInput);
		}

		@Override
		public ItemStack getResult(ItemStack aInput)
		{
			if(isTool) FleAPI.damageItem(null, aInput, EnumDamageResource.UseTool, damage);
			else --aInput.stackSize;
			return aInput;
		}

		@Override
		public ItemAbstractStack getShowStack()
		{
			return stack;
		}
	}
}