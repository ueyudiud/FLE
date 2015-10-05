package fle.core.recipe.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.cg.RecipesTab;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapelessFleRecipe;
import fle.api.recipe.SingleInputRecipe;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.item.tool.ToolMaterialInfo;

public class ToolCraftingRecipe extends ShapelessFleRecipe
{
	private static Object[] a(Object obj, Object...other)
	{
		if(other == null)
		{
			return new Object[]{obj};
		}
		else
		{
			Object[] objs = new Object[1 + other.length];
			objs[0] = obj;
			System.arraycopy(other, 0, objs, 1, other.length);
			return objs;
		}
	}
	
	public ToolCraftingRecipe(RecipesTab aTab, String aTool, int size, Object...other)
	{
		super(aTab, null, a(new ToolRecipe(aTool, size), other));
	}
	public ToolCraftingRecipe(RecipesTab aTab, String aTool, Object stick)
	{
		super(aTab, null, new Object[]{new ToolRecipe(aTool), stick});
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting aInv)
	{
		return super.getCraftingResult(aInv);
	}
	
	private static class ToolRecipe implements SingleInputRecipe
	{
		private String toolName;
		private int size = 1;

		public ToolRecipe(String aToolName) 
		{
			toolName = aToolName;
		}
		public ToolRecipe(String aToolName, int aSize) 
		{
			toolName = aToolName;
			size = aSize;
		}

		@Override
		public boolean match(ItemStack aInput) 
		{
			return aInput != null ? aInput.getItem() instanceof ItemToolHead && ItemTool.getToolIDFromName(toolName) == aInput.getItemDamage() : false;
		}

		@Override
		public ItemStack getResult(ItemStack aInput)
		{
			if(aInput == null)
				return new ItemStack(Blocks.stone);
			ItemStack ret = new ItemStack(IB.tool, size, aInput.getItemDamage());
			if(aInput.hasTagCompound())
			{
				ret.stackTagCompound = new ToolMaterialInfo(aInput.getTagCompound()).writeToNBT(new NBTTagCompound());
			}
			((ItemTool) IB.tool).setDisplayDamage(ret, aInput.getItemDamageForDisplay());
			return ret;
		}
		
		@Override
		public ItemAbstractStack getShowStack()
		{
			return new ItemBaseStack(ItemToolHead.a(toolName, Materials.Void));
		}
	}
}