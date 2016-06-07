package farcore.lib.stack;

import java.util.List;

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
	 * This method needn't check size.
	 * @param stack
	 * @return
	 */
	boolean similar(ItemStack stack);
	
	boolean contain(ItemStack stack);
	
	int size(ItemStack stack);
	
	AbstractStack split(ItemStack stack);
	
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
	 * Some tools, container has container item.
	 * This option is result whether use the 
	 * container item. (Default command value 
	 * is false)
	 * @return
	 */
	boolean useContainer();
}