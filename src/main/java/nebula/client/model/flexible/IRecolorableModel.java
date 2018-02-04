/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model.flexible;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IRecolorableModel extends IModel
{
	void registerColorMultiplier(BlockColors colors);
	
	void registerColorMultiplier(ItemColors colors);
}
