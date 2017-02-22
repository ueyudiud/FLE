/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import nebula.common.base.Ety;
import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class SingleInputMatch
{
	public static final SingleInputMatch EMPTY = new SingleInputMatch(null);
	
	/**
	 * The input stack.
	 */
	protected AbstractStack input;
	/**
	 * The effect on output.
	 */
	protected @Nullable Consumer<Entry<ItemStack, ItemStack>> consumer;
	/**
	 * The result of input.
	 */
	protected @Nullable Function<ItemStack, ItemStack> result;
	
	public SingleInputMatch(AbstractStack input)
	{
		this(input, null);
	}
	public SingleInputMatch(AbstractStack input, Function<ItemStack, ItemStack> result)
	{
		this(input, null, result);
	}
	public SingleInputMatch(AbstractStack input, Consumer<Entry<ItemStack, ItemStack>> consumer, Function<ItemStack, ItemStack> result)
	{
		this.input = input;
		this.consumer = consumer;
		this.result = result;
	}
	
	public boolean match(ItemStack stack)
	{
		return this.input == null ? stack == null : (stack != null && this.input.contain(stack));
	}
	
	public void applyOutput(ItemStack input, ItemStack output)
	{
		if (this.consumer != null)
		{
			this.consumer.accept(new Ety(input, output));
		}
	}
	
	public ItemStack getRemain(ItemStack input)
	{
		return this.result == null ? null : this.result.apply(input);
	}
}