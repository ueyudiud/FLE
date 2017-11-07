/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import static fle.api.recipes.instance.RecipeMaps.POLISHING;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import nebula.base.Ety;
import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class PolishRecipe
{
	private static final List<Entry<AbstractStack, Integer>> MAP = new ArrayList<>();
	
	public static void addPolishLevel(AbstractStack stack, int level)
	{
		MAP.add(new Ety<>(stack, level));
	}
	
	public static void addPolishRecipe(AbstractStack input, String map, ItemStack output)
	{
		if (map == null || map.length() != 9 || output == null || input == null) throw new IllegalArgumentException("Invalid recipe elements.");
		POLISHING.addRecipe(new TemplateRecipe<IPolishRecipeHandler>(handler -> input.similar(handler.getPolishingInput()) && map.equals(new String(handler.getPolishingMatrix())), Misc.anyTo(output)).setData(input, map, output));
	}
	
	public static boolean isPolishable(ItemStack stack)
	{
		return L.contain(POLISHING.recipes(), recipe -> ((AbstractStack) recipe.get(0)).similar(stack));
	}
	
	public static int getPolishLevel(ItemStack stack)
	{
		return L.cast(L.getFromEntries(MAP, e -> e.similar(stack)), -1);
	}
}
