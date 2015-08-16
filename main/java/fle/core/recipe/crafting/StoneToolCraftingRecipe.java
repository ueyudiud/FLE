package fle.core.recipe.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.recipe.ShapelessFleRecipe;
import fle.api.recipe.SingleInputRecipe;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.item.tool.ToolMaterialInfo;

public class StoneToolCraftingRecipe extends ShapelessFleRecipe
{
	public StoneToolCraftingRecipe(String aTool, int size, Object stick, Object other)
	{
		super(null, new Object[]{new ToolRecipe(aTool, size), stick, other});
	}
	public StoneToolCraftingRecipe(String aTool, Object stick)
	{
		super(null, new Object[]{new ToolRecipe(aTool), stick});
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
	}
}