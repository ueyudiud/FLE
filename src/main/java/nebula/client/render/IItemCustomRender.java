/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IItemCustomRender extends IIconLoader
{
	void renderItemStack(ItemStack stack);
}