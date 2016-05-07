package fle.api.recipe;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;

import farcore.enums.EnumItem;
import farcore.item.ItemSubTool;
import farcore.lib.recipe.ISingleInputRecipe;
import farcore.lib.recipe.ShapelessFleRecipe;
import farcore.lib.substance.SubstanceHandle;
import farcore.lib.substance.SubstanceTool;
import fle.core.item.ItemToolFle;
import fle.core.item.ItemToolHeadFle;
import net.minecraft.item.ItemStack;

public class ToolSingleCraftingRecipe extends ShapelessFleRecipe
{
	public ToolSingleCraftingRecipe(String tool, int tick, Object...objects)
	{
		this(tool, 1.0F, tick, objects);
	}
	public ToolSingleCraftingRecipe(String tool, float coefficient, int tick, Object...objects)
	{
		super(tick, ObjectArrays.concat(new ToolMatchRecipe(tool, coefficient), objects));
	}
	
	private static class ToolMatchRecipe implements ISingleInputRecipe
	{
		String toolName;
		float usesCoefficient;
		int tool;
		ItemStack instance;
		
		public ToolMatchRecipe(String tool, float coefficient)
		{
			this.toolName = tool;
			this.usesCoefficient = coefficient;
			this.tool = EnumItem.tool_head.instance(1, tool).getItemDamage();
			this.instance = EnumItem.tool.instance(1, tool);
		}

		@Override
		public boolean similar(ItemStack stack)
		{
			return stack != null && stack.getItem() instanceof ItemToolHeadFle &&
					stack.getItemDamage() == tool;
		}

		@Override
		public boolean match(ItemStack stack)
		{
			return similar(stack) && stack.stackSize > 0;
		}

		@Override
		public ItemStack[] output(ItemStack stack)
		{
			SubstanceTool tool = ItemSubTool.getToolMaterial(stack);
			int maxDamage = tool.maxUses;
			float damage = ItemSubTool.getCustomDamage(stack);
			ItemStack output = ((ItemToolFle) EnumItem.tool.item()).a(toolName, tool, SubstanceHandle.VOID_TOOL, Math.max(1, (int) (maxDamage * usesCoefficient)), damage * usesCoefficient);
			stack = stack.copy();
			if(stack.stackSize > 1)
			{
				stack.stackSize--;
				return new ItemStack[]{output, stack};
			}
			else
			{
				return new ItemStack[]{output, null};
			}
		}

		@Override
		public ItemStack instance()
		{
			return instance;
		}

		@Override
		public List<ItemStack> display()
		{
			return ImmutableList.of(instance);
		}

		@Override
		public boolean valid()
		{
			return instance != null;
		}
	}
}