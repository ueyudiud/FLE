package farcore.lib.item;

import farcore.lib.world.IEnvironment;
import net.minecraft.item.ItemStack;

public interface IUpdatableItem
{
	ItemStack updateItem(IEnvironment environment, ItemStack stack);
}