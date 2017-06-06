/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.model.IModel;

/**
 * @author ueyudiud
 */
public interface IRecolorableModel extends IModel
{
	void registerColorMultiplier(BlockColors colors);
	
	void registerColorMultiplier(ItemColors colors);
}
