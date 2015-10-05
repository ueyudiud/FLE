package fle.core.recipe;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.RecipesTab;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.RecipeAdder;
import fle.api.util.Compact;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;

public class FLERA implements RecipeAdder
{
	@Override
	public void addPolishRecipe(ItemAbstractStack input, String map,
			ItemStack output)
	{
		FLEPolishRecipe.getInstance().registerRecipe(new PolishRecipe(RecipesTab.tabClassic, input, map, output.copy()));
	}

	@Override
	public void addDryingRecipe(ItemAbstractStack input, int tick,
			ItemStack output)
	{
		FLEDryingRecipe.getInstance().registerRecipe(new DryingRecipe(input, tick, output));
	}

	@Override
	public void addRCBlastFurnaceRecipe(ItemStack aInput, boolean flag,
			int tick, ItemStack aOutput)
	{
		try 
		{
			Compact.getRCRecipeRegister((byte) 1).invoke(Compact.RCbfcm, aInput, flag, false, tick, aOutput);
		}
		catch (Throwable e)
		{
			;
		}
	}

	@Override
	public void addRCCokeOvenRecipe(ItemStack aInput, boolean flag, int tick,
			ItemStack aOutput1, FluidStack aOutput2)
	{
		try 
		{
			Compact.getRCRecipeRegister((byte) 0).invoke(Compact.RCcocm, aInput, flag, false, aOutput1, aOutput2, tick);
		}
		catch (Throwable e)
		{
			;
		}
	}

	@Override
	public void addRCRollingRecipe(ItemStack aOutput, Object... objects)
	{
		try 
		{
			Compact.getRCRecipeRegister((byte) 2).invoke(Compact.RCrmcm, aOutput, objects);
		}
		catch (Throwable e)
		{
			;
		}
	}
}