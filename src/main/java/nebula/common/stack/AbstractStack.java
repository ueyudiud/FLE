package nebula.common.stack;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.base.Judgable;
import net.minecraft.item.ItemStack;

/**
 * Abstract stack type. Which is used in recipes, container checks, etc.
 * 
 * @author ueyudiud
 *
 */
public interface AbstractStack
{
	/**
	 * Check is this stack similar to target stack. This method doesn't check
	 * size.
	 * 
	 * @param stack
	 * @return
	 */
	boolean similar(@Nullable ItemStack stack);
	
	/**
	 * Check this stack is fully to input with target stack.
	 * 
	 * @param stack
	 * @return
	 */
	default boolean contain(ItemStack stack)
	{
		return similar(stack) && stack.stackSize >= size(stack);
	}
	
	int size(@Nullable ItemStack stack);
	
	default AbstractStack split(ItemStack stack)
	{
		throw new UnsupportedOperationException();
	}
	
	default AbstractStack copyWithSize(int size)
	{
		throw new UnsupportedOperationException();
	}
	
	// INFO : Please at least override instance or display one, or
	// the stack will be over flow!
	/**
	 * Create a instance stack, which should be a copy of source
	 * that any modification of instance will not affect this stack.
	 * 
	 * @return the instance {@link ItemStack} of this stack.
	 */
	default ItemStack instance()
	{
		return display().get(0);
	}
	
	/**
	 * Display most of stack matched, for most allowed matched in
	 * consideration should be contained, but it needn't contain
	 * all if matched stack are not enumable.<p>
	 * The result list should be unmodifiable.
	 * 
	 * @return the
	 */
	default List<ItemStack> display()
	{
		return ImmutableList.of(instance());
	}
	
	/**
	 * If this stack is valid. (Invalid stack will cause whole recipe is valid).
	 * Such like use empty ore dictionary list, no container item, etc.
	 * 
	 * @return Is this stack valid.
	 */
	default boolean valid()
	{
		return true;
	}
	
	default Judgable<ItemStack> similarCheck()
	{
		return this::similar;
	}
	
	default Judgable<ItemStack> containCheck()
	{
		return this::contain;
	}
}
