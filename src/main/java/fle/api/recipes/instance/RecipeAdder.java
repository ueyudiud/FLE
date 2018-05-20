/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import static farcore.lib.solid.SolidStack.COPY_SOLIDSTACK;
import static fle.api.recipes.instance.RecipeMaps.CERAMIC;
import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_ADD_TO_MIX;
import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_BASE;
import static fle.api.recipes.instance.RecipeMaps.DIRT_MIXTURE_INPUT;
import static fle.api.recipes.instance.RecipeMaps.DIRT_MIXTURE_OUTPUT;
import static fle.api.recipes.instance.RecipeMaps.DRYING;
import static fle.api.recipes.instance.RecipeMaps.LEVER_OIL_MILL;
import static fle.api.recipes.instance.RecipeMaps.STONE_MILL;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT1;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT2;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT3;
import static fle.api.recipes.instance.RecipeMaps.WASHING_BARGRIZZLY;
import static nebula.base.function.F.anyf;
import static nebula.common.stack.IS.copy;
import static nebula.common.util.FluidStacks.COPY_FLUIDSTACK;
import static nebula.common.util.ItemStacks.COPY_ITEMSTACK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;

import farcore.lib.material.Mat;
import farcore.lib.solid.SolidStack;
import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.SingleInputMatch;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.interfaces.IRecipeInput;
import nebula.base.collection.A;
import nebula.base.collection.ObjArrayParseHelper;
import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.IS;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * All recipe registration helper method are here.
 * <p>
 * 
 * @author ueyudiud
 * @see fle.api.recipes.instance.RecipeMaps
 */
public final class RecipeAdder
{
	public static void addRecipe(IRecipe recipe)
	{
		CraftingManager.getInstance().addRecipe(recipe);
	}
	
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
		WASHING_BARGRIZZLY.addRecipe(new TemplateRecipe<>(input.containCheck(), anyf(duration), asShapelessChanceOutput(output, chances)).setData(input, duration, ItemStacks.copyStacks(output), chances));
	}
	
	public static void addPolishRecipe(AbstractStack input, String map, @Nonnull ItemStack output)
	{
		PolishRecipe.addPolishRecipe(input, map, output);
	}
	
	public static void addDringRecipe(AbstractStack input, int duration, float rainfall, @Nonnull ItemStack output)
	{
		DRYING.addRecipe(new TemplateRecipe<>(input, anyf(duration), anyf(rainfall), anyf(output).andThen(COPY_ITEMSTACK)).setData(input, duration, rainfall, output.copy()));
	}
	
	public static void addStoneMillRecipe(AbstractStack input, int duration, @Nullable SolidStack output1, @Nullable FluidStack output2)
	{
		STONE_MILL.addRecipe(new TemplateRecipe<>(input.containCheck(), anyf(duration), anyf(output1).andThen(COPY_SOLIDSTACK), anyf(output2).andThen(COPY_FLUIDSTACK)).setData(input, duration, SolidStack.copyOf(output1), FluidStacks.copy(output2)));
	}
	
	public static void addLeverOilMillRecipe(AbstractStack input, int duration, ItemStack output1, int outputChance, @Nonnull Fluid output2, int minOutput, int maxOutput)
	{
		addLeverOilMillRecipe(input, duration, output1, new int[] { outputChance }, new FluidStack(output2, 0), minOutput, maxOutput);
	}
	
	public static void addLeverOilMillRecipe(AbstractStack input, int duration, @Nullable ItemStack output1, int[] outputChance, @Nullable FluidStack output2, int minOutput, int maxOutput)
	{
		LEVER_OIL_MILL.addRecipe(new TemplateRecipe<>(input.containCheck(), anyf(duration), asChanceOutput(output1, outputChance), asRandomOutput(output2, minOutput, maxOutput)).setData(input, duration, copy(output1), FluidStacks.copy(output2), outputChance, (long) maxOutput << 32 | minOutput));
	}
	
	public static void addDirtMixtureInputRecipe(AbstractStack input, Object...objects)
	{
		DIRT_MIXTURE_INPUT.addRecipe(input, objects);
	}
	
	public static void addDirtMixtureOutputRangedRecipe(@Nonnull ItemStack output, long size, Object...objects)
	{
		ObjArrayParseHelper helper = A.create(objects);
		ImmutableList.Builder<Range<Mat>> builder = ImmutableList.builder();
		
		while (helper.hasNext())
		{
			builder.add(new Range<Mat>(helper.read(), helper.read(), helper.read()));
		}
		
		final List<Range<Mat>> list = builder.build();
		DIRT_MIXTURE_OUTPUT.addRecipe(stacks -> {
			if (stacks.getAmount() < size) return null;
			for (Range<Mat> range : list)
				if (!range.inRange(stacks.getContain(range.element))) return null;
			return IS.copy(output, (int) (stacks.getAmount() * output.stackSize / size));
		});
	}
	
	public static void addBoilingPotRecipe(@Nonnull AbstractStack input1, @Nonnull FluidStack input2, @Nullable SingleInputMatch input3, int minTemp, int duration, @Nullable ItemStack output1, @Nullable ItemStack output2)
	{
		final SingleInputMatch input3i = input3 == null ? SingleInputMatch.EMPTY : input3;
		CERAMICPOT_BASE.addRecipe(
				new TemplateRecipe<IRecipeInput>(handler -> input1.contain(handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT1)) && FluidStacks.containFluid(handler.<FluidStack> getRecipeInput(TAG_CERAMICPOT_BASE_INPUT2), input2) && input3i.match(handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT3)),
						anyf(minTemp), anyf(duration), anyf(output1).andThen(COPY_ITEMSTACK), input3i.toOutputTransferFunction(output2).compose(handler -> handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT1))).setData(input1, FluidStacks.copy(input2), input3i, minTemp, duration,
								copy(output1), copy(output2)));
	}
	
	public static void addSolidItemMixToPotRecipe(@Nonnull SolidStack input1, @Nonnull FluidStack input2, @Nonnull FluidStack output)
	{
		Function<Entry<SolidStack, FluidStack>, Integer> function = e -> e.getValue().amount / input2.amount;
		CERAMICPOT_ADD_TO_MIX.addRecipe(new TemplateRecipe<>(e -> input1.isSoildEqual(e.getKey()) && input2.isFluidEqual(e.getValue()) && e.getKey().amount / input1.amount >= e.getValue().amount / input2.amount, function.andThen(i -> i * input1.amount),
				function.andThen(i -> FluidStacks.sizeOf(output, i * output.amount))));
	}
	
	public static void addCeramicRecipe(@Nonnull byte[][] range, @Nonnull ItemStack output)
	{
		if (range.length != 10)
			throw new IllegalArgumentException();
		for (byte[] value : range)
		{
			if (value.length == 0 || (value.length == 2 && value[0] > value[1]))
				throw new IllegalArgumentException("Illegal range: " + Arrays.toString(value));
		}
		CERAMIC.addRecipe(new TemplateRecipe<>(L.toPredicate(RecipeAdder::inRange, range), anyf(range), anyf(output).andThen(COPY_ITEMSTACK)));
	}
	
	public static void addSimpleReducingRecipe(Mat input, Mat output)
	{
		SimpleReducingRecipeHandler.Recipe recipe = RecipeMaps.SIMPLE_REDUCING.new Recipe();
		recipe.source = input;
		recipe.target = output;
		RecipeMaps.SIMPLE_REDUCING.addRecipe(recipe);
	}
	
	private static boolean inRange(byte[] target, byte[][] ranges)
	{
		for (int i = 0; i < target.length; ++i)
		{
			byte[] range = ranges[i];
			switch (range.length)
			{
			case 1 :
				if (target[i] != range[0])
					return false;
				break;
			case 2 :
				if (target[i] < range[0] || target[i] > range[1])
					return false;
				break;
			default:
				if (!Bytes.contains(range, target[i]))
					return false;
				break;
			}
		}
		return true;
	}
	
	private static <E> Function<E, ItemStack[]> asShapelessChanceOutput(ItemStack[] list, int[][] chances)
	{
		Objects.requireNonNull(list);
		Function<E, ItemStack[]> result;
		if (chances == null)
		{
			result = any -> ItemStacks.copyStacks(list);
		}
		else if (list.length != chances.length)
			throw new IllegalArgumentException();
		else
		{
			result = any -> {
				List<ItemStack> l = new ArrayList<>();
				for (int i = 0; i < list.length; ++i)
				{
					int j = 0;
					for (int chance : chances[i])
						if (chance == 10000 || L.nextInt(10000) < chance) ++j;
					if (j > 0)
					{
						l.add(copy(list[i], list[i].stackSize * j));
					}
				}
				return L.cast(l, ItemStack.class);
			};
		}
		return result;
	}
	
	private static <E> Function<E, ItemStack> asChanceOutput(ItemStack stack, int[] chances)
	{
		if (stack == null) return Misc.TO_NULL;
		final ItemStack s = stack.copy();
		return any -> {
			int size = 0;
			for (int i : chances)
				if (L.nextInt(10000) < i) ++size;
			return size > 0 ? copy(s, size * s.stackSize) : null;
		};
	}
	
	private static <E> Function<E, FluidStack> asRandomOutput(FluidStack stack, int min, int max)
	{
		if (stack == null) return Misc.TO_NULL;
		final FluidStack s = stack.copy();
		final int l = max - min;
		return l == 0 ? anyf(s) : any -> FluidStacks.sizeOf(s, min + L.nextInt(l));
	}
	
	private static class Range<E>
	{
		E		element;
		float	min;
		float	max;
		
		Range(E element, float min, float max)
		{
			this.element = element;
			this.min = min;
			this.max = max;
		}
		
		boolean inRange(double f)
		{
			return L.inRange(this.max, this.min, f);
		}
	}
	
	private RecipeAdder()
	{
	}
}
