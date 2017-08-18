/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.recipe;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.item.ItemMulti;
import farcore.lib.tree.Tree;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import nebula.common.inventory.IBasicInventory;
import nebula.common.util.ItemStacks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipePortableWoodwork2 implements PortableWoodworkRecipe
{
	@Override
	public boolean match(IBasicInventory inventory)
	{
		if (inventory.hasStackInSlot(2))
		{
			ItemStack stack = inventory.getStack(2);
			Block block = Block.getBlockFromItem(stack.getItem());
			if(block instanceof BlockLogArtificial)
			{
				boolean
				hasT1 = false,
				hasT2 = false,
				hasT3 = false;
				if (inventory.hasStackInSlot(0))
				{
					stack = inventory.getStack(0);
					if (EnumToolTypes.AXE.match(stack) || EnumToolTypes.BOW_SAW.match(stack))
					{
						hasT1 = true;
					}
					if (EnumToolTypes.ADZ.match(stack))
					{
						hasT3 = true;
					}
				}
				if (inventory.hasStackInSlot(1))
				{
					stack = inventory.getStack(1);
					if (EnumToolTypes.AWL.match(stack) || EnumToolTypes.BIFACE.match(stack) ||
							(stack.getItem() instanceof IPolishableItem && ((IPolishableItem) stack.getItem()).getPolishResult(stack, ' ') == 'c'))
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
	public int[] getIntScaleRange(IBasicInventory inventory)
	{
		return new int[] {1, 1};
	}
	
	@Override
	public ItemStack[] getOutputs(IBasicInventory inventory, int value)
	{
		ItemStack stack = inventory.getStack(2);
		Tree tree = ((BlockLogArtificial) Block.getBlockFromItem(stack.getItem())).tree;
		ItemStack[] result = {new ItemStack(tree.plank), ItemMulti.createStack(tree.material, MC.bark)};
		return result;
	}
	
	@Override
	public void onOutput(IBasicInventory inventory, int value)
	{
		ItemStack stack;
		boolean
		hasT1 = false,
		hasT2 = false,
		hasT3 = false,
		flag = false;
		if (inventory.hasStackInSlot(0))
		{
			stack = inventory.getStack(0);
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
		if (inventory.hasStackInSlot(1))
		{
			stack = inventory.getStack(1);
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
			ItemStacks.damageTool(inventory.getStack(0), 0.25F, null, null);
			if (inventory.getStack(0).stackSize <= 0)
			{
				inventory.removeStackFromSlot(0);
			}
			if (!flag)
			{
				ItemStacks.damageTool(inventory.getStack(1), 0.1F, null, null);
				if (inventory.getStack(1).stackSize <= 0)
				{
					inventory.removeStackFromSlot(1);
				}
			}
			else
			{
				stack = inventory.getStack(1);
				((IPolishableItem) stack.getItem()).onPolished(null, stack);
				if (stack.stackSize <= 0)
				{
					inventory.removeStackFromSlot(1);
				}
			}
		}
		else if (hasT3)
		{
			ItemStacks.damageTool(inventory.getStack(0), 0.4F, null, null);
			if (inventory.getStack(0).stackSize <= 0)
			{
				inventory.removeStackFromSlot(0);
			}
		}
		inventory.decrStackSize(2, 1);
	}
}