/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addLeverOilMillRecipe;

import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import fle.loader.IBF;
import fle.loader.Crops;
import nebula.common.stack.BaseStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @author ueyudiud
 */
public class RecipeOilMill
{
	public static void init()
	{
		ItemStack output = IBF.crop.getSubItem("plant_waste");
		addLeverOilMillRecipe(new BaseStack(ItemMulti.createStack(Crops.reed, MC.seed)), 400,
				output, 2000, FluidRegistry.WATER, 200, 300);
	}
}