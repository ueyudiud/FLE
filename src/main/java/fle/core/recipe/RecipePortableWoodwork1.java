/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.recipe;

import farcore.data.EnumToolTypes;
import farcore.data.MP;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.material.prop.PropertyTree;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import nebula.common.inventory.IBasicInventory;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipePortableWoodwork1 implements PortableWoodworkRecipe
{
	@Override
	public boolean match(IBasicInventory inventory)
	{
		if (inventory.hasStackInSlot(2))
		{
			ItemStack stack = inventory.getStack(2);
			if(stack.getItem() instanceof ItemTreeLog)
			{
				int length = ItemTreeLog.getLogSize(stack);
				if (length <= 1) return false;
				boolean has = false;
				if (inventory.hasStackInSlot(1)) return false;
				if (inventory.hasStackInSlot(0))
				{
					stack = inventory.getStack(0);
					if (EnumToolTypes.AXE.match(stack) || EnumToolTypes.BOW_SAW.match(stack) || EnumToolTypes.ADZ.match(stack))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public int[] getIntScaleRange(IBasicInventory inventory)
	{
		int length = ItemTreeLog.getLogSize(inventory.getStack(2));
		return new int[] {1, length / 2};
	}
	
	@Override
	public ItemStack[] getOutputs(IBasicInventory inventory, int value)
	{
		ItemStack stack = inventory.getStack(2);
		ItemStack[] result = new ItemStack[2];
		PropertyTree tree = ItemTreeLog.getMaterial(stack).getProperty(MP.property_tree);
		int length = ItemTreeLog.getLogSize(stack);
		if (length == 2)
		{
			result[0] = new ItemStack(tree.logArtificial);
			result[1] = new ItemStack(tree.logArtificial);
		}
		else if (value == 1)
		{
			result[0] = new ItemStack(tree.logArtificial);
			result[1] = stack.copy();
			ItemTreeLog.setLogSize(result[1], length - 1);
		}
		else
		{
			result[0] = stack.copy();
			ItemTreeLog.setLogSize(result[0], value);
			result[1] = stack.copy();
			ItemTreeLog.setLogSize(result[1], length - value);
		}
		return result;
	}
	
	@Override
	public void onOutput(IBasicInventory inventory, int value)
	{
		ItemStacks.damageTool(inventory.getStack(0), 1.0F, null, null);
		if (inventory.getStack(0).stackSize <= 0)
		{
			inventory.removeStackFromSlot(0);
		}
		inventory.decrStackSize(2, 1);
	}
	
	@Override
	public int[] getDisplayNumbers(IBasicInventory inventory, int value)
	{
		int length = ItemTreeLog.getLogSize(inventory.getStack(2));
		return new int[]{value, length - value};
	}
}