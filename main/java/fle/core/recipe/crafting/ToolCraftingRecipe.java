package fle.core.recipe.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import flapi.recipe.IPlayerToolCraftingRecipe;
import flapi.recipe.SingleInputRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.tool.ToolMaterialInfo;
import fle.tool.item.ItemTool;
import fle.tool.item.ItemToolHead;

public class ToolCraftingRecipe implements IPlayerToolCraftingRecipe
{
	private SingleInputRecipe recipe;
	private ItemAbstractStack other;
	private ItemAbstractStack tool;
	
	public ToolCraftingRecipe(String tool, int size, ItemAbstractStack c)
	{
		this(tool, size, null, c);
	}
	public ToolCraftingRecipe(String tool, int size, ItemAbstractStack input, ItemAbstractStack c)
	{
		recipe = new ToolRecipe(tool, size);
		other = input;
		this.tool = c;
	}
	
	@Override
	public boolean match(ItemStack input1, ItemStack input2, ItemStack tool)
	{
		return recipe.match(input1) ? (other != null ? other.equal(input2) : true) && this.tool.equal(tool) : false;
	}

	@Override
	public ItemStack useTool(EntityPlayer player, ItemStack tool)
	{
		tool.stackSize--;
		return tool;
	}

	@Override
	public ItemStack getOutput(ItemStack input1, ItemStack input2,
			ItemStack tool)
	{
		return recipe.getResult(input1);
	}
	
	private static class ToolRecipe implements SingleInputRecipe
	{
		private String toolName;
		private int size = 1;

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
			return new BaseStack(ItemToolHead.a(toolName, Materials.Void));
		}
	}
}