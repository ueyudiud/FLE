/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.item;

import nebula.common.environment.IEnvironment;
import net.minecraft.item.ItemStack;

public interface IUpdatableItem
{
	ItemStack updateItem(IEnvironment environment, ItemStack stack);
}
