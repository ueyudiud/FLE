package fle.core.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import flapi.cg.RecipesTab;
import flapi.recipe.DropInfo;
import flapi.recipe.IPlayerToolCraftingRecipe;
import flapi.recipe.RecipeAdder;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import flapi.util.Compact;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;
import fle.core.recipe.FLESoakRecipe.SoakRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;
import fle.core.recipe.crafting.PlayerToolCraftingRecipe;

public class FLERA implements RecipeAdder
{
	@Override
	public void addPlayerCraftingRecipe(IPlayerToolCraftingRecipe recipe)
	{
		PlayerToolCraftingRecipe.addRecipe(recipe);
	}
	
	@Override
	public void addPlayerCraftingRecipe(ItemAbstractStack input1,
			ItemAbstractStack input2, ItemAbstractStack tool, float toolDamage,
			ItemStack output)
	{
		PlayerToolCraftingRecipe.addRecipe(new PlayerToolCraftingRecipe(input1, input2, tool, toolDamage, output));
	}
	
	public void addWashingRecipe(ItemAbstractStack input, DropInfo info)
	{
		WashingRecipe.registryDust(input, info);
	}
	
	@Override
	public void addPolishRecipe(ItemAbstractStack input, String map,
			ItemStack output)
	{
		FLEPolishRecipe.getInstance().registerRecipe(new PolishRecipe(RecipesTab.tabClassic, input, map, output.copy()));
	}

	@Override
	public void addDryingRecipe(String name, ItemAbstractStack input, int tick,
			ItemStack output)
	{
		FLEDryingRecipe.getInstance().registerRecipe(new DryingRecipe(name, input, tick, output));
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
	public void addSifterRecipe(String name, ItemAbstractStack input, SolidStack output1,
			float chance, ItemStack output2)
	{
		FLESifterRecipe.a(new SifterRecipe(name, input, output1, output2, chance));
	}

	@Override
	public void addSifterRecipe(String name, Solid input, SolidStack output1,
			float chance, ItemStack output2)
	{
		FLESifterRecipe.a(new SifterRecipe(name, input, output1, output2, chance));
	}

	@Override
	public void addOilMillRecipe(String name, ItemAbstractStack input, FluidStack output1,
			float change, ItemStack output2)
	{
		FLEOilMillRecipe.a(new OilMillRecipe(name, input, output2, change, output1));
	}

	@Override
	public void addBoilingRecipe(String name, ItemAbstractStack input1, FluidStack input2,
			int e, ItemStack output)
	{
		FLEBoilingHeaterRecipe.a(new BHRecipe(name, input1, input2, e, output));
	}
	
	@Override
	public void addSoakRecipe(String name, FluidStack fInput,
			ItemAbstractStack iInput, int tick, ItemStack output)
	{	
		FLESoakRecipe.a(new SoakRecipe(name, fInput, iInput, tick, output));
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