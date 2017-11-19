/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import nebula.common.enviornment.EnviornmentBlockPos;
import nebula.common.stack.AbstractStack;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;

/**
 * @author ueyudiud
 */
public class FlamableRecipes
{
	private static class FlameSource
	{
		ToIntFunction<ICoord> temperatureProvider;
		
		FlameSource(ToIntFunction<ICoord> function)
		{
			this.temperatureProvider = function;
		}
	}
	
	private static class Flamable
	{
		AbstractStack	stack;
		float			smoderTemp;
		float			flamableTemp;
		
		Flamable(AbstractStack stack, float smoderTemp, float flamableTemp)
		{
			this.stack = stack;
		}
	}
	
	private static final List<FlameSource>	FLAMESOURCES	= new ArrayList<>();
	private static final List<Flamable>		FLAMABLES		= new ArrayList<>();
	
	static
	{
		addFlameSource(coord-> {
			Block block = coord.getBlockState().getBlock();
			if (block == Blocks.TORCH)
				return 400;
			if (block == Blocks.LIT_FURNACE)
				return 420;
			return -1;
		});
	}
	
	public static void addFlameSource(ToIntFunction<ICoord> function)
	{
		FLAMESOURCES.add(new FlameSource(function));
	}
	
	public static void addFlamableItem(AbstractStack stack, float smoderTemp, float flamableTemp)
	{
		FLAMABLES.add(new Flamable(stack, smoderTemp, flamableTemp));
	}
	
	public static int getFlameTemperature(ICoord coord)
	{
		int temp;
		for (FlameSource source : FLAMESOURCES)
		{
			if ((temp = source.temperatureProvider.applyAsInt(coord)) > 0) return temp;
		}
		return (temp = BlockFluidBase.getTemperature(coord.world(), coord.pos())) == Integer.MAX_VALUE ? -1 : temp;
	}
	
	public static boolean isFlamable(ICoord coord, int minFlameTemperature)
	{
		for (Direction direction : Direction.DIRECTIONS_3D)
		{
			if (getFlameTemperature(new EnviornmentBlockPos(coord.world(), direction.offset(coord.pos()))) >= minFlameTemperature)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFlamable(ItemStack stack)
	{
		return L.contain(FLAMABLES, check -> check.stack.similar(stack));
	}
	
	public static boolean isSmoder(ItemStack stack, float temperature)
	{
		Flamable flamable = L.get(FLAMABLES, check -> check.stack.similar(stack));
		return flamable == null ? false : flamable.smoderTemp <= temperature;
	}
	
	public static boolean isFlamable(ItemStack stack, float temperature)
	{
		Flamable flamable = L.get(FLAMABLES, check -> check.stack.similar(stack));
		return flamable == null ? false : flamable.flamableTemp <= temperature;
	}
}
