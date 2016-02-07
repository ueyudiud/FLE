package farcore.item.interfaces;

import farcore.item.enums.EnumParticleSize;
import net.minecraft.item.ItemStack;

/**
 * Item separator.<br>
 * Use different size to separate item to part.<br>
 * This is kind of tool to divide items.
 * @author ueyudiud
 *
 */
public interface IItemSeparator
{
	EnumParticleSize getAllowSeparateSize(ItemStack stack);
	
	void onSeparateTarget(ItemStack stack, ItemStack target);
}