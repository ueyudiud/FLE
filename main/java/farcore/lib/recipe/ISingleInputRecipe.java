package farcore.lib.recipe;

import java.util.List;

import farcore.lib.stack.AbstractStack;
import net.minecraft.item.ItemStack;

public interface ISingleInputRecipe
{
	/**
	 * Check is this stack similar to
	 * target stack.
	 * This method needn't check size.
	 * @param stack
	 * @return
	 */
	boolean similar(ItemStack stack);
	
	boolean match(ItemStack stack);
	
	/**
	 * Output from this recipe.
	 * You also should take operation
	 * for input stack (Such as mius
	 * stack size, etc).
	 * @param stack
	 * @return
	 */
	ItemStack output(ItemStack stack);
	
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
}