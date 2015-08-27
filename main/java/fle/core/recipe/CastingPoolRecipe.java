package fle.core.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.material.MatterDictionary;
import fle.api.material.MatterDictionary.IFreezingRecipe;
import fle.core.gui.InventoryCastingPool;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;

public class CastingPoolRecipe implements IFreezingRecipe
{
	public static void init()
	{
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.copper, 100), "xxxx xxxx", ItemFleSub.a("ingot_cu")));
	}
	
	FluidStack input;
	int s;
	ItemStack output;
	
	public CastingPoolRecipe(FluidStack aInput, String str, ItemStack aOutput)
	{
		try
		{
			input = aInput.copy();
			if(str.length() != 9)
			{
				throw new RuntimeException();
			}
			s = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				s += str.charAt(i) != ' ' ? 1 << i : 0;
			}
			output = aOutput.copy();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			String c = "";
			c += aInput.getUnlocalizedName() + ", ";
			c += str + ", ";
			c += aOutput.getUnlocalizedName();
			throw new RuntimeException(c);
		}
	}

	@Override
	public boolean match(FluidStack aStack, IInventory inv)
	{
		if(aStack == null || !(inv instanceof InventoryCastingPool)) return false;
		if((!input.isFluidEqual(aStack)) || input.amount > aStack.amount) return false;
		for(int i = 0; i < 9; ++i)
		{
			boolean flag = (s & (1 << i)) != 0;
			if((inv.getStackInSlot(i) != null) != flag) return false;
		}
		return true;
	}

	@Override
	public int getMatterRequire(FluidStack aStack, IInventory inv)
	{
		return input.amount;
	}

	@Override
	public ItemStack getOutput(FluidStack aStack, IInventory inv)
	{
		return output.copy();
	}	
}