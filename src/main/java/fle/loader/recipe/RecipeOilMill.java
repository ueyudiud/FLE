/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addLeverOilMillRecipe;

import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import fle.loader.Crops;
import fle.loader.IBF;
import nebula.common.stack.BaseStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeOilMill
{
	public static void init()
	{
		ItemStack output = IBF.iCropRelated.getSubItem("plant_waste");
		addLeverOilMillRecipe(new BaseStack(ItemMulti.createStack(Crops.reed, MC.seed)), 400,
				output, 2500, IBF.fsJuice[0], 200, 300);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("citrus")), 400,
				output, 2500, IBF.fsJuice[1], 100, 125);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("bitter_orange")), 300,
				output, 2500, IBF.fsJuice[2], 120, 150);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("lemon")), 300,
				output, 2500, IBF.fsJuice[3], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("tangerine")), 300,
				output, 2500, IBF.fsJuice[4], 120, 150);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("pomelo")), 300,
				output, 2500, IBF.fsJuice[5], 75, 100);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("lime")), 300,
				output, 2500, IBF.fsJuice[6], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("orange")), 300,
				output, 2500, IBF.fsJuice[7], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBF.iCropRelated.getSubItem("grapefruit")), 300,
				output, 2500, IBF.fsJuice[8], 125, 175);
	}
}