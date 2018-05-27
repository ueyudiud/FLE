/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.recipe;

import farcore.blocks.flora.BlockLogArtificial;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.tree.Tree;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import nebula.common.inventory.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipePortableWoodwork2 implements PortableWoodworkRecipe
{
	@Override
	public boolean match(ItemStack[] inventory)
	{
		if (inventory[2] != null)
		{
			ItemStack stack = inventory[2];
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block instanceof BlockLogArtificial)
			{
				boolean hasT1 = false, hasT2 = false, hasT3 = false;
				if (inventory[0] != null)
				{
					stack = inventory[0];
					if (EnumToolTypes.AXE.match(stack) || EnumToolTypes.BOW_SAW.match(stack))
					{
						hasT1 = true;
					}
					if (EnumToolTypes.ADZ.match(stack))
					{
						hasT3 = true;
					}
				}
				if (inventory[1] != null)
				{
					stack = inventory[1];
					if (EnumToolTypes.AWL.match(stack) || EnumToolTypes.BIFACE.match(stack) || (stack.getItem() instanceof IPolishableItem && ((IPolishableItem) stack.getItem()).getPolishResult(stack, ' ') == 'c'))
					{
						hasT2 = true;
					}
				}
				return (hasT1 && hasT2) || hasT3;
			}
		}
		return false;
	}
	
	@Override
	public int[] getIntScaleRange(ItemStack[] inventory)
	{
		return new int[] { 1, 1 };
	}
	
	@Override
	public ItemStack[] getOutputs(ItemStack[] inventory, int value)
	{
		ItemStack stack = inventory[2];
		Tree tree = ((BlockLogArtificial) Block.getBlockFromItem(stack.getItem())).tree;
		ItemStack[] result = { ItemMulti.createStack(tree.material(), MC.plank, 2), ItemMulti.createStack(tree.material(), MC.bark) };
		return result;
	}
	
	@Override
	public void onOutput(ItemStack[] inventory, int value)
	{
		ItemStack stack;
		boolean hasT1 = false, hasT2 = false, hasT3 = false, flag = false;
		if (inventory[0] != null)
		{
			stack = inventory[0];
			if (EnumToolTypes.AXE.match(stack) || EnumToolTypes.BOW_SAW.match(stack))
			{
				hasT1 = true;
			}
			if (EnumToolTypes.ADZ.match(stack))
			{
				hasT1 = true;
				hasT3 = true;
			}
		}
		if (inventory[1] != null)
		{
			stack = inventory[1];
			if (EnumToolTypes.AWL.match(stack) || EnumToolTypes.BIFACE.match(stack))
			{
				hasT2 = true;
			}
			if (stack.getItem() instanceof IPolishableItem)
			{
				flag = true;
			}
		}
		if (hasT1 && hasT2)
		{
			InventoryHelper.damageTool(inventory, 0, 0.25F, null, null);
			if (!flag)
			{
				InventoryHelper.damageTool(inventory, 1, 0.1F, null, null);
			}
			else
			{
				stack = inventory[1];
				((IPolishableItem) stack.getItem()).onPolished(null, stack);
				if (stack.stackSize <= 0)
				{
					inventory[1] = null;
				}
			}
		}
		else if (hasT3)
		{
			InventoryHelper.damageTool(inventory, 0, 0.4F, null, null);
		}
		InventoryHelper.decrSlotSize(inventory, 2, 1, true);
	}
}
