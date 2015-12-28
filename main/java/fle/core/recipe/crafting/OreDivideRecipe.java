package fle.core.recipe.crafting;

import flapi.material.MaterialOre;
import fle.core.init.IB;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class OreDivideRecipe implements IRecipe
{
	public OreDivideRecipe()
	{
		
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		MaterialOre material = null;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if(inv.getStackInSlot(i) != null)
			{
				if(inv.getStackInSlot(i).getItem() != IB.oreChip) return false;
				
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return null;
	}

	@Override
	public int getRecipeSize()
	{
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
}