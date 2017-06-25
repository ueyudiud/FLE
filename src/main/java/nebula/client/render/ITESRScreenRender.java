/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Use for rendering with screen, for used for projector to
 * project screen to other place.
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface ITESRScreenRender<T extends TileEntity>
{
	void renderScreen(T tile, double width, double height, float partialTicks);
}