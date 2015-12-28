package fle.core.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.MatterDictionary.IFreezingRecipe;
import fle.core.te.argil.TileEntityCeramicFurnaceOutlet;

public class CeramicFurnaceOutletRecipe implements IFreezingRecipe
{
	private FluidStack stack;
	private ItemStack output;
	
	public CeramicFurnaceOutletRecipe(FluidStack aStack, ItemStack aOutput)
	{
		stack = aStack.copy();
		output = aOutput.copy();
	}

	@Override
	public boolean match(FluidStack aStack, IInventory inv)
	{
		if(inv instanceof TileEntityCeramicFurnaceOutlet && aStack != null)
		{
			return stack.isFluidEqual(aStack) && stack.amount <= aStack.amount;
		}
		return false;
	}

	@Override
	public int getMatterRequire(FluidStack aStack, IInventory inv)
	{
		return stack.amount;
	}

	@Override
	public ItemStack getOutput(FluidStack aStack, IInventory inv)
	{
		return output.copy();
	}
}