package fle.core.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.RecipesTab;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.RecipeAdder;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.util.Compact;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;

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
	public void addColdForgingRecipe(ItemAbstractStack[] input, String map,
			ItemStack output)
	{
		ColdForgingRecipe.registerRecipe(new ColdForgingRecipe(input, map, output));
	}

	@Override
	public void addStoneMillRecipe(ItemAbstractStack input, int tick, SolidStack output1,
			FluidStack output2)
	{
		FLEStoneMillRecipe.a(new StoneMillRecipe(input, tick, output1, output2));
	}

	@Override
	public void addSifterRecipe(ItemAbstractStack input, SolidStack output1,
			float chance, ItemStack output2)
	{
		FLESifterRecipe.a(new SifterRecipe(input, output1, output2, chance));
	}

	@Override
	public void addSifterRecipe(Solid input, SolidStack output1,
			float chance, ItemStack output2)
	{
		FLESifterRecipe.a(new SifterRecipe(input, output1, output2, chance));
	}

	@Override
	public void addOilMillRecipe(ItemAbstractStack input, FluidStack output1,
			float change, ItemStack output2)
	{
		FLEOilMillRecipe.a(new OilMillRecipe(input, output2, change, output1));
	}

	@Override
	public void addBoilingRecipe(ItemAbstractStack input1, FluidStack input2,
			int e, ItemStack output)
	{
		FLEBoilingHeaterRecipe.a(new BHRecipe(input1, input2, e, output));
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