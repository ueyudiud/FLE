/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.item;

import nebula.common.tool.EnumToolType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface ICustomUsableItem
{
	void onItemUse(ItemStack stack, float amount, EnumToolType type, EntityLivingBase user);
}