package fle.core.recipe.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import fle.api.cg.RecipesTab;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapelessFleRecipe;
import fle.core.block.ItemRopeLadder;
import fle.core.init.IB;

public class RopeLadderCraftingRecipe extends ShapelessFleRecipe
{
	private boolean flag;
	
	public RopeLadderCraftingRecipe(boolean aFlag, Object...aInput)
	{
		super(IB.ropeLadder, aInput);
		flag = aFlag;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting aInv)
	{
		if(aInv == null) return getRecipeOutput();
		if(flag)
		{
			int length = 0;
			for(int i = 0; i < aInv.getSizeInventory(); ++i)
			{
				if(new ItemBaseStack(IB.ropeLadder).isStackEqul(aInv.getStackInSlot(i)))
				{
					length += ItemRopeLadder.b(aInv.getStackInSlot(i));
				}
			}
			return ItemRopeLadder.a(length);
		}
		else
		{
			int size = 0;
			int length = 0;
			for(int i = 0; i < aInv.getSizeInventory(); ++i)
			{
				if(new ItemBaseStack(IB.ropeLadder).isStackEqul(aInv.getStackInSlot(i)))
				{
					++size;
					length += ItemRopeLadder.b(aInv.getStackInSlot(i));
				}
			}
			ItemStack ret = ItemRopeLadder.a(length / (size * 2));
			ret.stackSize = size * 2;
			return ret;
		}
	}
}