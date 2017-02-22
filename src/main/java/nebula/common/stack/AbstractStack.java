package nebula.common.stack;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

/**
 * Abstract stack type.
 * Which is used in recipes, container checks,
 * etc.
 * @author ueyudiud
 *
 */
public interface AbstractStack
{
	/**
	 * Check is this stack similar to
	 * target stack.
	 * This method doesn't check size.
	 * @param stack
	 * @return
	 */
	boolean similar(ItemStack stack);
	
	/**
	 * Check this stack is fully to input with
	 * target stack.
	 * @param stack
	 * @return
	 */
	default boolean contain(ItemStack stack) { return similar(stack) && stack.stackSize >= size(stack); }
	
	int size(@Nullable ItemStack stack);
	
	default AbstractStack split(ItemStack stack)  { throw new UnsupportedOperationException(); }
	
	default AbstractStack copyWithSize(int size) { throw new UnsupportedOperationException(); }
	
	//INFO : Please at least override instance or display one, or
	//       the stack will be over flow!
	/**
	 * Create a instance stack.
	 * @return
	 */
	default ItemStack instance() { return display().get(0); }
	
	/**
	 * Display most of stack matched.
	 * @return
	 */
	default List<ItemStack> display() { return ImmutableList.of(instance()); }
	
	/**
	 * If this stack is valid.
	 * (Invalid stack will cause whole
	 * recipe is valid).
	 * Such like use empty ore dictionary
	 * list, no container item, etc.
	 * @return Is this stack valid.
	 */
	default boolean valid() { return true; }
	
	/**
	 * Some tools or container has container item.
	 * This option is result whether use the
	 * container item. (Default command value
	 * is false)
	 * @return
	 */
	@Deprecated
	default boolean useContainer() { return false; }
}