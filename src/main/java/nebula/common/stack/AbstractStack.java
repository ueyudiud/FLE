package nebula.common.stack;

import java.util.List;

import javax.annotation.Nullable;

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
	boolean contain(ItemStack stack);
	
	int size(@Nullable ItemStack stack);
	
	AbstractStack split(ItemStack stack);
	
	AbstractStack copyWithSize(int size);
	
	/**
	 * Create a instance stack.
	 * @return
	 */
	ItemStack instance();
	
	/**
	 * Display most of stack matched.
	 * @return
	 */
	List<ItemStack> display();
	
	/**
	 * If this stack is valid.
	 * (Invalid stack will cause whole
	 * recipe is valid).
	 * Such like use empty ore dictionary
	 * list, no container item, etc.
	 * @return Is this stack valid.
	 */
	boolean valid();
	
	/**
	 * Some tools or container has container item.
	 * This option is result whether use the
	 * container item. (Default command value
	 * is false)
	 * @return
	 */
	@Deprecated
	boolean useContainer();
}