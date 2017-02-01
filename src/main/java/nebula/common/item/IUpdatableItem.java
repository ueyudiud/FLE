package nebula.common.item;

import nebula.common.enviornment.IEnvironment;
import net.minecraft.item.ItemStack;

public interface IUpdatableItem
{
	ItemStack updateItem(IEnvironment environment, ItemStack stack);
}