/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance;

import static farcore.lib.solid.SolidStack.COPY_SOLIDSTACK;
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
import static nebula.common.data.Misc.anyTo;
import static nebula.common.util.FluidStacks.COPY_FLUIDSTACK;
import static nebula.common.util.ItemStacks.COPY_ITEMSTACK;
import static net.minecraft.item.ItemStack.copyItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import farcore.lib.solid.SolidStack;
import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.SingleInputMatch;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.interfaces.IRecipeInput;
import nebula.base.ObjArrayParseHelper;
import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
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
		WASHING_BARGRIZZLY.addRecipe(new TemplateRecipe<>(input.containCheck(), anyTo(duration), asShapelessChanceOutput(output, chances)).setData(input, duration, ItemStacks.copyStacks(output), chances));
	}
	
	public static void addPolishRecipe(AbstractStack input, String map, @Nonnull ItemStack output)
	{
		PolishRecipe.addPolishRecipe(input, map, output);
	}
	
	public static void addDringRecipe(AbstractStack input, int duration, float rainfall, @Nonnull ItemStack output)
	{
		DRYING.addRecipe(new TemplateRecipe<>(input.similarCheck(), anyTo(duration), anyTo(rainfall), anyTo(output).andThen(COPY_ITEMSTACK)).setData(input, duration, rainfall, output.copy()));
	}
	
	public static void addStoneMillRecipe(AbstractStack input, int duration, @Nullable SolidStack output1, @Nullable FluidStack output2)
	{
		STONE_MILL.addRecipe(new TemplateRecipe<>(input.containCheck(), anyTo(duration), anyTo(output1).andThen(COPY_SOLIDSTACK), anyTo(output2).andThen(COPY_FLUIDSTACK)).setData(input, duration, SolidStack.copyOf(output1), FluidStacks.copy(output2)));
	}
	
	public static void addLeverOilMillRecipe(AbstractStack input, int duration, ItemStack output1, int outputChance, @Nonnull Fluid output2, int minOutput, int maxOutput)
	{
		addLeverOilMillRecipe(input, duration, output1, new int[] { outputChance }, new FluidStack(output2, 0), minOutput, maxOutput);
	}
	
	public static void addLeverOilMillRecipe(AbstractStack input, int duration, @Nullable ItemStack output1, int[] outputChance, @Nullable FluidStack output2, int minOutput, int maxOutput)
	{
		LEVER_OIL_MILL
		.addRecipe(new TemplateRecipe<>(input.containCheck(), anyTo(duration), asChanceOutput(output1, outputChance), asRandomOutput(output2, minOutput, maxOutput)).setData(input, duration, copyItemStack(output1), FluidStacks.copy(output2), outputChance, (long) maxOutput << 32 | minOutput));
	}
	
	public static void addDirtMixtureInputRecipe(AbstractStack input, Object...objects)
	{
		DIRT_MIXTURE_INPUT.addRecipe(input, objects);
	}
	
	public static void addDirtMixtureOutputRangedRecipe(@Nonnull ItemStack output, long size, Object...objects)
	{
		ObjArrayParseHelper helper = ObjArrayParseHelper.create(objects);
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
			return ItemStacks.sizeOf(output, (int) (stacks.getAmount() * output.stackSize / size));
		});
	}
	
	public static void addBoilingPotRecipe(@Nonnull AbstractStack input1, @Nonnull FluidStack input2, @Nullable SingleInputMatch input3, int minTemp, int duration, @Nullable ItemStack output1, @Nullable ItemStack output2)
	{
		final SingleInputMatch input3i = input3 == null ? SingleInputMatch.EMPTY : input3;
		CERAMICPOT_BASE.addRecipe(
				new TemplateRecipe<IRecipeInput>(handler -> input1.contain(handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT1)) && FluidStacks.containFluid(handler.<FluidStack> getRecipeInput(TAG_CERAMICPOT_BASE_INPUT2), input2) && input3i.match(handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT3)),
						anyTo(minTemp), anyTo(duration), anyTo(output1).andThen(COPY_ITEMSTACK), input3i.toOutputTransferFunction(output2).compose(handler -> handler.getRecipeInput(TAG_CERAMICPOT_BASE_INPUT1))).setData(input1, FluidStacks.copy(input2), input3i, minTemp, duration,
								copyItemStack(output1), copyItemStack(output2)));
	}
	
	public static void addSolidItemMixToPotRecipe(@Nonnull SolidStack input1, @Nonnull FluidStack input2, @Nonnull FluidStack output)
	{
		Function<Entry<SolidStack, FluidStack>, Integer> function = e -> e.getValue().amount / input2.amount;
		CERAMICPOT_ADD_TO_MIX.addRecipe(new TemplateRecipe<>(e -> input1.isSoildEqual(e.getKey()) && input2.isFluidEqual(e.getValue()) && e.getKey().amount / input1.amount >= e.getValue().amount / input2.amount, function.andThen(i -> i * input1.amount),
				function.andThen(i -> FluidStacks.sizeOf(output, i * output.amount))));
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
						l.add(ItemStacks.sizeOf(list[i], list[i].stackSize * j));
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
			return size > 0 ? ItemStacks.sizeOf(s, size * s.stackSize) : null;
		};
	}
	
	private static <E> Function<E, FluidStack> asRandomOutput(FluidStack stack, int min, int max)
	{
		if (stack == null) return Misc.TO_NULL;
		final FluidStack s = stack.copy();
		final int l = max - min;
		return l == 0 ? anyTo(s) : any -> FluidStacks.sizeOf(s, min + L.nextInt(l));
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
