/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.recipes;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import farcore.lib.material.Mat;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import nebula.base.Ety;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.base.register.IRegister;
import nebula.base.register.Register;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.ItemStacks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ShapedFleDataRecipe extends ShapedFleRecipe
{
	public static class SingleInputMatchMat extends SingleInputMatch
	{
		int id;
		Function<ItemStack, String> dataSupplier;
		
		SingleInputMatchMat(SingleInputMatch match, int id, Function<ItemStack, String> dataSupplier)
		{
			super(match.input, match.consumer, match.result);
			this.id = id;
			this.dataSupplier = dataSupplier;
		}
	}
	
	static class Variable
	{
		public int id;
		public Predicate<String> predicate;
		public BiConsumer<ItemStack, String> transformer;
		
		public Variable(int id, Predicate<String> predicate, BiConsumer<ItemStack, String> consumer)
		{
			this.id = id;
			this.predicate = predicate;
			this.transformer = consumer;
		}
	}
	
	public static class Builder
	{
		final AbstractStack output;
		final String[] inputs;
		final int width;
		final int height;
		boolean mirror = true;
		IRegister<Entry<Predicate<String>, BiConsumer<ItemStack, String>>> variables = new Register<>();
		IntObjectMap<SingleInputMatch> transforms = new IntObjectHashMap<>();
		
		Builder(AbstractStack output, String[] inputs)
		{
			this.output = output;
			this.inputs = inputs;
			this.width = inputs[0].length();
			this.height = inputs.length;
			for (int i = 1; i < inputs.length; ++i)
			{
				if (inputs[i].length() != this.width)
				{
					throw new RuntimeException("Illegal recipe witdh! got: " + Arrays.toString(inputs));
				}
			}
		}
		
		public Builder m(boolean flag)
		{
			this.mirror = flag;
			return this;
		}
		
		public Builder v(String variable, BiConsumer<ItemStack, String> supplier)
		{
			return v(variable, F.P_T, supplier);
		}
		
		public Builder v_nbt(String variable, Judgable<? super Mat> predicate, String key)
		{
			return v(variable, predicate.from(Mat::material), (stack, data) -> ItemStacks.getOrSetupNBT(stack, true).setString(key, data));
		}
		
		public Builder v(String variable, Predicate<String> bound, BiConsumer<ItemStack, String> supplier)
		{
			this.variables.register(variable, new Ety(bound, supplier));
			return this;
		}
		
		public Builder t(char key, Object vlaue)
		{
			this.transforms.put(key, ShapedFleRecipe.castAsInputMatch(vlaue));
			return this;
		}
		
		public Builder t_m(char key, Object vlaue, String variable, Function<ItemStack, Mat> function)
		{
			return t(key, vlaue, variable, function.andThen(Mat.materials()::name));
		}
		
		public Builder t(char key, Object vlaue, String variable, Function<ItemStack, String> function)
		{
			int id = this.variables.id(variable);
			if (id == -1)
			{
				throw new RuntimeException("Undefined variable: " + variable);
			}
			this.transforms.put(key, new SingleInputMatchMat(ShapedFleRecipe.castAsInputMatch(vlaue), id, function));
			return this;
		}
		
		public ShapedFleDataRecipe b()
		{
			SingleInputMatch[][] inputs = new SingleInputMatch[this.height][this.width];
			for (int i = 0; i < this.height; ++i)
			{
				for (int j = 0; j < this.width; ++j)
				{
					SingleInputMatch match = this.transforms.get(this.inputs[i].charAt(j));
					if (match == null)
					{
						match = SingleInputMatch.EMPTY;
					}
					inputs[i][j] = match;
				}
			}
			Variable[] variables = new Variable[this.variables.size()];
			for (int i = 0; i < variables.length; ++i)
			{
				Entry<Predicate<String>, BiConsumer<ItemStack, String>> entry = this.variables.get(i);
				variables[i] = new Variable(i, entry.getKey(), entry.getValue());
			}
			return new ShapedFleDataRecipe(this.output, this.mirror, this.width, this.height, inputs, variables);
		}
	}
	
	public static Builder builder(ItemStack output, String...inputLayout) { return builder(new BaseStack(output), inputLayout); }
	public static Builder builder(AbstractStack output, String...inputLayout) { return new Builder(output, inputLayout); }
	
	private Variable[] variables;
	
	ShapedFleDataRecipe(AbstractStack output, boolean mirror, int w, int h, SingleInputMatch[][] inputs, Variable[] variables)
	{
		this.output = output;
		this.width = w;
		this.height = h;
		this.enableMirror = mirror;
		this.inputs = inputs;
		this.variables = variables;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int maxXOff = inv.getWidth() - this.width;
		int maxYOff = inv.getHeight() - this.height;
		for (int j = 0; j <= maxYOff; ++j)
		{
			for (int i = 0; i <= maxXOff; ++i)
			{
				String[] datas;
				if ((datas = matchWithCoordData(inv, i, j, false)) != null)
				{
					return getCraftResult(inv, i, j, false, datas);
				}
				if (this.enableMirror && (datas = matchWithCoordData(inv, i, j, true)) != null)
				{
					return getCraftResult(inv, i, j, true, datas);
				}
			}
		}
		return null;
	}
	
	private ItemStack getCraftResult(InventoryCrafting inv, int xOff, int yOff, boolean mirror, String[] datas)
	{
		ItemStack result = this.output.instance();
		for (int i = 0; i < datas.length; ++i)
		{
			this.variables[i].transformer.accept(result, datas[i]);
		}
		for (int j = 0; j < this.height; ++j)
		{
			for (int i = 0; i < this.width; ++i)
			{
				this.inputs[j][mirror ? this.width - i - 1 : i].applyOutput(ItemStack.copyItemStack(inv.getStackInRowAndColumn(i + xOff, j + yOff)), result);
			}
		}
		return result;
	}
	
	@Override
	protected boolean matchWithCoordOffset(InventoryCrafting inv, int xOff, int yOff, boolean mirror)
	{
		return matchWithCoordData(inv, xOff, yOff, mirror) != null;
	}
	
	protected String[] matchWithCoordData(InventoryCrafting inv, int xOff, int yOff, boolean mirror)
	{
		String[] datas = new String[this.variables.length];
		for (int j = 0; j < this.height; ++j)
		{
			for (int i = 0; i < this.width; ++i)
			{
				SingleInputMatch match = this.inputs[j][mirror ? this.width - i - 1 : i];
				ItemStack stack = inv.getStackInRowAndColumn(i + xOff, j + yOff);
				if (!match.match(stack))
				{
					return null;
				}
				if (match instanceof SingleInputMatchMat)
				{
					SingleInputMatchMat materialData = (SingleInputMatchMat) match;
					int id = materialData.id;
					String data = materialData.dataSupplier.apply(stack);
					if (data == null)
					{
						return null;
					}
					if (datas[id] != null)
					{
						if (!datas[id].equals(data))
						{
							return null;
						}
					}
					else if (!this.variables[id].predicate.test(data))
					{
						return null;
					}
					else
					{
						datas[id] = data;
					}
				}
			}
		}
		return datas;
	}
}
