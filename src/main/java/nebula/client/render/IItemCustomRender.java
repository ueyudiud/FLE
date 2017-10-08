/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The custom renderer for item.
 * Used to rendering item with more flexible option.<p>
 * Register render to item by
 * <code>NebulaRenderHandler.registerRender(item, render);</code>
 * with item, then item will render in inventory by this method
 * instead of rendering by model.
 * @author ueyudiud
 * @see nebula.client.NebulaRenderHandler#registerRender(net.minecraft.item.Item, IItemCustomRender)
 */
@SideOnly(Side.CLIENT)
public interface IItemCustomRender extends IIconLoader
{
	/**
	 * Render item stack into inventory.<p>
	 * The item transform is same to item renderer give.
	 * (EX: the item render by TESR is different from this
	 * with translation [-0.5, -0.5, -0.5]).
	 * @param stack the rendering item stack.
	 */
	void renderItemStack(ItemStack stack);
}