/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class SimplyKilnRecipe
{
	private static final List<SimplyKilnRecipe> RECIPES = new ArrayList<>();
	private static final Map<AbstractStack, Byte> SIZES = new HashMap<>();
	
	public static void addRecipe(AbstractStack input, AbstractStack output, int maxIn, int temp, long duration)
	{
		RECIPES.add(new SimplyKilnRecipe(input, output, temp, duration));
		SIZES.put(input, (byte) maxIn);
	}
	
	public static int getMaxStacksizeInKiln(ItemStack stack)
	{
		return L.getOptional(SIZES, L.toPredicate(AbstractStack::similar, stack)).orElse((byte) 0);
	}
	
	public static SimplyKilnRecipe getRecipe(ItemStack stack)
	{
		return L.get(RECIPES, r -> r.input.similar(stack));
	}
	
	public final AbstractStack input;
	public final AbstractStack output;
	public final int minTemp;
	public final long duration;
	
	SimplyKilnRecipe(AbstractStack input, AbstractStack output, int temp, long duration)
	{
		this.input = input;
		this.output = output;
		this.minTemp = temp;
		this.duration = duration;
	}
}