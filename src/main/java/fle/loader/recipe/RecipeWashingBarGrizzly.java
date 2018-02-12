/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.oredict.OreStackExt;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.RecipeAdder;
import fle.api.recipes.instance.RecipeMaps;
import fle.loader.IBFS;
import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import nebula.common.util.L;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeWashingBarGrizzly
{
	public static void init()
	{
		RecipeAdder.addWashingBarGrizzlyRecipe(new OreStack(MC.pile.getOreName(M.gravel)), 100,
				new ItemStack[] { IBFS.iResources.getSubItem("flint_fragment"), IBFS.iResources.getSubItem("flint_sharp"), IBFS.iResources.getSubItem("flint_small"), new ItemStack(Items.FLINT), IBFS.iResources.getSubItem("quartz_chip"), IBFS.iResources.getSubItem("opal") },
				new int[][] { { 6000, 2000, 1000 }, { 2000, 500 }, { 10000, 3000, 1000 }, { 3000, 1000 }, { 200, 50 }, { 125, 25 } });
		RecipeAdder.addWashingBarGrizzlyRecipe(new OreStack(MC.pile.getOreName(M.sand)), 100, new ItemStack[] { ItemMulti.createStack(M.sand, MC.pile_purified), IBFS.iResources.getSubItem("flint_small"), IBFS.iResources.getSubItem("quartz_chip"), IBFS.iResources.getSubItem("quartz_large") },
				new int[][] { { 6000, 2000, 1000 }, { 2000, 500 }, { 750, 150 }, { 250, 50 } });
		RecipeAdder.addWashingBarGrizzlyRecipe(new OreStack(MC.pile.getOreName(M.redsand)), 100, new ItemStack[] { ItemMulti.createStack(M.redsand, MC.pile_purified), IBFS.iResources.getSubItem("flint_small"), IBFS.iResources.getSubItem("quartz_chip"), IBFS.iResources.getSubItem("quartz_large") },
				new int[][] { { 6000, 2000, 1000 }, { 2000, 500 }, { 750, 150 }, { 250, 50 } });
		for (Mat material : Mat.filt(SubTags.REDUCABLE, false))
		{
			addImpureReducedPileWashingRecipe(material);
		}
	}
	
	private static void addImpureReducedPileWashingRecipe(Mat material)
	{
		AbstractStack stack1 = new OreStackExt(MC.impure_reduced_pile.getOreName(material));
		AbstractStack stack2 = new BaseStack(ItemMulti.createStack(material, MC.reduced_pile));
		BaseStack stack3 = new BaseStack(ItemMulti.createStack(material, MC.reduced_pile_small));
		BaseStack stack4 = new BaseStack(ItemMulti.createStack(M.plant_ash, MC.dust_small));
		RecipeMaps.WASHING_BARGRIZZLY.addRecipe(new TemplateRecipe<>(
				stack1.containCheck(), Misc.anyTo(200), (Function<ItemStack, ItemStack[]>) source -> {
					List<ItemStack> list = new ArrayList<>(3);
					ItemStack stack = stack2.instance();
					if (material.itemProp != null)
					{
						material.itemProp.copyData(source, stack, material, MC.impure_reduced_pile, MC.reduced_pile);
					}
					list.add(stack);
					int size = 0;
					if (L.nextInt(10000) < 6000)
					{
						++ size;
					}
					if (L.nextInt(10000) < 2000)
					{
						++ size;
					}
					if (L.nextInt(10000) < 1000)
					{
						++ size;
					}
					if (L.nextInt(10000) < 1000)
					{
						++ size;
					}
					if (size > 0)
					{
						stack = stack3.instance(size);
						if (material.itemProp != null)
						{
							material.itemProp.copyData(source, stack, material, MC.impure_reduced_pile, MC.reduced_pile_small);
						}
						list.add(stack);
					}
					if (L.nextInt(10000) < 5000)
					{
						list.add(stack4.instance());
					}
					return list.toArray(new ItemStack[list.size()]);
				}).setData(stack1, 200, new ItemStack[] {stack2.instance(), stack3.instance()}, new int[][] {{10000}, {6000, 2000, 1000, 1000}, {5000}}));
	}
}
