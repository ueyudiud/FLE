/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import static fle.api.recipes.instance.RecipeMaps.DRYING;
import static fle.api.recipes.instance.RecipeMaps.WASHING_BARGRIZZLY;
import static nebula.common.data.Misc.anyTo;
import static nebula.common.util.ItemStacks.COPY_ITEMSTACK;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import nebula.common.stack.AbstractStack;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * @author ueyudiud
 */
public class RecipeAdder
{
	public static void addShapedRecipe(AbstractStack result, Object...objects)
	{
		CraftingManager.getInstance().addRecipe(new ShapedFleRecipe(result, objects));
	}
	public static void addShapedRecipe(ItemStack result, Object...objects)
	{
		CraftingManager.getInstance().addRecipe(new ShapedFleRecipe(result, objects));
	}
	public static void addShapelessRecipe(AbstractStack result, Object...objects)
	{
		CraftingManager.getInstance().addRecipe(new ShapelessFleRecipe(result, objects));
	}
	public static void addShapelessRecipe(ItemStack result, Object...objects)
	{
		CraftingManager.getInstance().addRecipe(new ShapelessFleRecipe(result, objects));
	}
	
	public static void addWashingBarGrizzlyRecipe(AbstractStack input, int duration, ItemStack[] output, int[][] chances)
	{
		WASHING_BARGRIZZLY.addRecipe(new TemplateRecipe<>(input.containCheck(), anyTo(duration), asShapelessChanceOutput(output, chances))
				.setData(input, duration, ItemStacks.copyStacks(output), chances));
	}
	
	public static void addPolishRecipe(AbstractStack input, String map, ItemStack output)
	{
		PolishRecipe.addPolishRecipe(input, map, output);
	}
	
	public static void addDringRecipe(AbstractStack input, int duration, float rainfall, ItemStack output)
	{
		DRYING.addRecipe(new TemplateRecipe<>(input.similarCheck(), anyTo(duration), anyTo(rainfall), anyTo(output).compose(COPY_ITEMSTACK))
				.setData(input, duration, rainfall, output.copy()));
	}
	
	private static <E> Function<E, ItemStack[]> asShapelessChanceOutput(ItemStack[] list, int[][] chances)
	{
		Objects.requireNonNull(list);
		Function<E, ItemStack[]> result;
		if (chances == null)
		{
			result = any->ItemStacks.copyStacks(list);
		}
		else if (list.length != chances.length) throw new IllegalArgumentException();
		else
		{
			result = any-> {
				List<ItemStack> l = new ArrayList<>();
				for (int i = 0; i < list.length; ++i)
				{
					int j = 0;
					for (int chance : chances[i]) if (chance == 10000 || L.nextInt(10000) < chance) ++j;
					if (j > 0)
					{
						l.add(ItemStacks.sizeOf(list[i], list[i].stackSize * j));
					}
				}
				return L.cast(l, ItemStack.class);
			};
		}
		return result;
	}
}