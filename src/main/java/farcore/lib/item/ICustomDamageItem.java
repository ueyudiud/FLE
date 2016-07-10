package farcore.lib.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ICustomDamageItem
{
	/**
	 * Called when item is damaged.
	 * @param stack
	 * @param amount
	 * @param user
	 * @param resource
	 */
	void damangeItem(ItemStack stack, float amount, EntityLivingBase user);
}