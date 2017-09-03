/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.List;

import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class FlamableItems
{
	private static class Flamable
	{
		AbstractStack stack;
		float smoderTemp;
		float flamableTemp;
		
		Flamable(AbstractStack stack, float smoderTemp, float flamableTemp)
		{
			this.stack = stack;
		}
	}
	
	private static final List<Flamable> LIST = new ArrayList<>();
	
	public static void addFlamableItem(AbstractStack stack, float smoderTemp, float flamableTemp)
	{
		LIST.add(new Flamable(stack, smoderTemp, flamableTemp));
	}
	
	public static boolean isFlamable(ItemStack stack)
	{
		return L.contain(LIST, check->check.stack.similar(stack));
	}
	
	public static boolean isSmoder(ItemStack stack, float temperature)
	{
		Flamable flamable = L.get(LIST, check -> check.stack.similar(stack));
		return flamable == null ? false : flamable.smoderTemp <= temperature;
	}
	
	public static boolean isFlamable(ItemStack stack, float temperature)
	{
		Flamable flamable = L.get(LIST, check -> check.stack.similar(stack));
		return flamable == null ? false : flamable.flamableTemp <= temperature;
	}
}