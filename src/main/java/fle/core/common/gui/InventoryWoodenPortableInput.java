/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import fle.api.recipes.instance.RecipeMaps;
import nebula.common.inventory.IContainersArray;
import nebula.common.inventory.InventorySimple;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class InventoryWoodenPortableInput extends InventorySimple
{
	private ContainerWoodworkPortable container;
	
	PortableWoodworkRecipe	recipe;
	
	public int max, min, current;
	public int left = -1, right = -1;
	
	public InventoryWoodenPortableInput(ContainerWoodworkPortable container)
	{
		super("inventory.woodwork.portable.input", false, 3);
		this.container = container;
	}
	
	public void onCraftMatrixChanged()
	{
		if (this.recipe != null && this.recipe.match(this.stacks))
		{
			rerangeValue();
		}
		else
		{
			this.recipe = RecipeMaps.PORTABLE_WOODWORK.findRecipe(this.stacks);
			if (this.recipe != null)
			{
				rerangeValue();
			}
			else
			{
				this.max = this.min = 0;
			}
		}
		updateOutput();
	}
	
	public void onOptionChanged(int value)
	{
		if (this.recipe != null)
		{
			int i = value == 0 ? 1 : value == 1 ? -1 : value == 2 ? 5 : value == 3 ? -5 : 0;
			this.current = L.range(this.max, this.min, this.current + i);
			updateOutput();
			this.container.detectAndSendChanges();
		}
	}
	
	private void updateOutput()
	{
		if (this.recipe != null)
		{
			((IContainersArray<ItemStack>) this.container.outputs.containers).fromArray(this.recipe.getOutputs(this.stacks, this.current));
			int[] range = this.recipe.getDisplayNumbers(this.stacks, this.current);
			if (range != null)
			{
				this.left = range[0];
				this.right = range[1];
			}
			else
			{
				this.left = this.right = -1;
			}
		}
		else
		{
			this.container.outputs.clear();
			this.left = this.right = -1;
		}
	}
	
	public void onOutput()
	{
		this.recipe.onOutput(this.stacks, this.current);
	}
	
	private void rerangeValue()
	{
		int[] range = this.recipe.getIntScaleRange(this.stacks);
		this.min = range[0];
		this.max = range[1];
		this.current = L.range(this.min, this.max, this.current);
	}
	
	@Override
	public int getFieldCount()
	{
		return 5;
	}
	
	@Override
	public int getField(int id)
	{
		switch (id)
		{
		case 0 : return this.current;
		case 1 : return this.min;
		case 2 : return this.max;
		case 3 : return this.left;
		case 4 : return this.right;
		default: return 0;
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
		case 0 : this.current = value; break;
		case 1 : this.min = value; break;
		case 2 : this.max = value; break;
		case 3 : this.left = value; break;
		case 4 : this.right = value; break;
		}
	}
}
