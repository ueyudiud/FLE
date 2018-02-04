/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import static fle.api.recipes.instance.RecipeMaps.POLISHING;

import java.util.Arrays;

import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import nebula.base.ArrayIntMap;
import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class PolishRecipe
{
	private static final ArrayIntMap<AbstractStack> MAP = new ArrayIntMap<>();
	
	public static void addPolishLevel(AbstractStack stack, int level)
	{
		MAP.put(stack, level);
	}
	
	public static void addPolishRecipe(AbstractStack input, String map, ItemStack output)
	{
		if (map == null || map.length() != 9 || output == null || input == null) throw new IllegalArgumentException("Invalid recipe elements.");
		final char[] map1 = map.toCharArray();
		POLISHING.addRecipe(new TemplateRecipe<IPolishRecipeHandler>(handler -> input.similar(handler.getPolishingInput()) && 
				Arrays.equals(map1, handler.getPolishingMatrix()), Misc.anyTo(output)).setData(input, map, output));
	}
	
	public static boolean isPolishable(ItemStack stack)
	{
		return L.contain(POLISHING.recipes(), recipe -> ((AbstractStack) recipe.get(0)).similar(stack));
	}
	
	public static int getPolishLevel(ItemStack stack)
	{
		return MAP.find(L.toPredicate(AbstractStack::similar, stack)).orElse(-1);
	}
}
