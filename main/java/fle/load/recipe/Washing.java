package fle.load.recipe;

import static farcore.lib.recipe.DropHandler.info;

import farcore.enums.EnumItem;
import farcore.lib.recipe.DropHandler;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import fle.api.recipe.WashingRecipe;
import net.minecraft.init.Items;

public class Washing
{
	public static void init()
	{
		WashingRecipe.addRecipe("flintI", new OreStack("pileGravel"), 
				new DropHandler(3, 
						info(new BaseStack(EnumItem.stone_chip.instance(1, "flintSmall")), 0.8F, 6), 
						info(new BaseStack(EnumItem.stone_chip.instance(1, "flintSharp")), 0.8F, 1), 
						info(new BaseStack(EnumItem.stone_fragment.instance(1, "flint")), 0.8F, 3), 
						info(new BaseStack(Items.flint), 0.8F, 4)));
	}
}