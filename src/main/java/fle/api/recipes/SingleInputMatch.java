/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import nebula.common.data.Misc;
import nebula.common.stack.AbstractStack;
import nebula.common.tool.EnumToolType;
import nebula.common.util.ItemStacks;
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
	protected @Nullable BiConsumer<ItemStack, ItemStack> consumer;
	/**
	 * The result of input.
	 */
	protected @Nullable Function<ItemStack, ItemStack> result;
	
	public static SingleInputMatch toolUse(EnumToolType type, float use)
	{
		return new SingleInputMatch(type.stack(), stack-> {
			ItemStacks.damageTool(stack, use, null, type);
			return stack.stackSize <= 0 ? null : stack;
		});
	}
	
	public SingleInputMatch(AbstractStack input)
	{
		this(input, null);
	}
	public SingleInputMatch(AbstractStack input, Function<ItemStack, ItemStack> result)
	{
		this(input, null, result);
	}
	public SingleInputMatch(AbstractStack input, BiConsumer<ItemStack, ItemStack> consumer, Function<ItemStack, ItemStack> result)
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
			this.consumer.accept(input, output);
		}
	}
	
	public Function<ItemStack, ItemStack> toOutputTransferFunction(final ItemStack output)
	{
		if (output == null || this.consumer == null) return Misc.<ItemStack, ItemStack>anyTo(output).andThen(ItemStacks.COPY_ITEMSTACK);
		return input-> {
			ItemStack result = output.copy();
			this.consumer.accept(input, result);
			return result;
		};
	}
	
	public ItemStack getRemain(ItemStack input)
	{
		return this.result == null ? null : this.result.apply(input);
	}
}