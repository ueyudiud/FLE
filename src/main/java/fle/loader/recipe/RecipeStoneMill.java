/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addStoneMillRecipe;

import farcore.lib.solid.SolidStack;
import fle.loader.IBFS;
import nebula.common.stack.BaseStack;

/**
 * @author ueyudiud
 */
public class RecipeStoneMill
{
	public static void init()
	{
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("black_pepper")), 100, new SolidStack(IBFS.sBlackPepper, 100), null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("turmeric_rhizome")), 100, new SolidStack(IBFS.sTurmericRhizome, 100), null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("prickly_ash")), 100, new SolidStack(IBFS.sPricklyAsh, 100), null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("cinnamon")), 100, new SolidStack(IBFS.sCinnamon, 100), null);
		
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("millet")), 125, new SolidStack(IBFS.sMillet, 250), null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("wheat")), 125, new SolidStack(IBFS.sWheat, 250), null);
	}
}
