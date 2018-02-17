/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.common.environment.IEnvironment;
import net.minecraft.item.ItemStack;

public interface IUpdatableItem
{
	/**
	 * Called when item ticking in environment.
	 * 
	 * @param environment the environment that ItemStack in.
	 * @param stack the ItemStack.
	 * @return the ItemStack remain after ticking.
	 */
	@Nullable ItemStack updateItem(@Nonnull IEnvironment environment, @Nonnull ItemStack stack);
}
