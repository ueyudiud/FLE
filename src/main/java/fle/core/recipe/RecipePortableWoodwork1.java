/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.recipe;

import farcore.data.EnumToolTypes;
import farcore.data.MP;
import farcore.items.ItemTreeLog;
import farcore.lib.material.prop.PropertyWood;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import nebula.common.inventory.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipePortableWoodwork1 implements PortableWoodworkRecipe
{
	@Override
	public boolean match(ItemStack[] inventory)
	{
		if (inventory[2] != null)
		{
			ItemStack stack = inventory[2];
			if (stack.getItem() instanceof ItemTreeLog)
			{
				int length = ItemTreeLog.getLogSize(stack);
				if (length <= 1) return false;
				if (inventory[1] != null) return false;
				if (inventory[0] != null)
				{
					stack = inventory[0];
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
	public int[] getIntScaleRange(ItemStack[] inventory)
	{
		int length = ItemTreeLog.getLogSize(inventory[2]);
		return new int[] { 1, length / 2 };
	}
	
	@Override
	public ItemStack[] getOutputs(ItemStack[] inventory, int value)
	{
		ItemStack stack = inventory[2];
		ItemStack[] result = new ItemStack[2];
		PropertyWood tree = ItemTreeLog.getMaterial(stack).getProperty(MP.property_wood);
		int length = ItemTreeLog.getLogSize(stack);
		Block log = tree.block;
		if (length == 2)
		{
			result[0] = new ItemStack(log);
			result[1] = new ItemStack(log);
		}
		else if (value == 1)
		{
			result[0] = new ItemStack(log);
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
	public void onOutput(ItemStack[] inventory, int value)
	{
		InventoryHelper.damageTool(inventory, 0, 1.0F, null, null);
		InventoryHelper.decrSlotStack(inventory, 2, 1, true);
	}
	
	@Override
	public int[] getDisplayNumbers(ItemStack[] inventory, int value)
	{
		int length = ItemTreeLog.getLogSize(inventory[2]);
		return new int[] { value, length - value };
	}
}
