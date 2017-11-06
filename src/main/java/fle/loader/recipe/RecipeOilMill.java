/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addLeverOilMillRecipe;

import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import fle.loader.Crops;
import fle.loader.IBFS;
import nebula.common.stack.BaseStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeOilMill
{
	public static void init()
	{
		ItemStack output = IBFS.iCropRelated.getSubItem("plant_waste");
		addLeverOilMillRecipe(new BaseStack(ItemMulti.createStack(Crops.reed, MC.seed)), 400,
				output, 2500, IBFS.fsJuice[0], 200, 300);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("citrus")), 400,
				output, 2500, IBFS.fsJuice[1], 100, 125);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("bitter_orange")), 300,
				output, 2500, IBFS.fsJuice[2], 120, 150);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("lemon")), 300,
				output, 2500, IBFS.fsJuice[3], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("tangerine")), 300,
				output, 2500, IBFS.fsJuice[4], 120, 150);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("pomelo")), 300,
				output, 2500, IBFS.fsJuice[5], 75, 100);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("lime")), 300,
				output, 2500, IBFS.fsJuice[6], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("orange")), 300,
				output, 2500, IBFS.fsJuice[7], 150, 200);
		addLeverOilMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("grapefruit")), 300,
				output, 2500, IBFS.fsJuice[8], 125, 175);
	}
}