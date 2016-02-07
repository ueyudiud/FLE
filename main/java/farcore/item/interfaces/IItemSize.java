package farcore.item.interfaces;

import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import net.minecraft.item.ItemStack;

/**
 * Implements by an item with size.<br>
 * Recommend implements on each item.
 * @author ueyudiud
 *
 */
public interface IItemSize
{
	EnumItemSize getItemSize(ItemStack stack);

	EnumParticleSize getParticleSize(ItemStack stack);
}