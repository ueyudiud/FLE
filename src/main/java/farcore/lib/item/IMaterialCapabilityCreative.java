/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.item;

import farcore.lib.material.behavior.MaterialPropertyManager.MaterialHandler;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface IMaterialCapabilityCreative
{
	MaterialHandler createMaterialHandler(ItemStack stack);
}
