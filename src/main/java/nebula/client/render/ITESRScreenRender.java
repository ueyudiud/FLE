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
	/**
	 * Render screen to world.<p>
	 * The (0, 0, 0) postion is at left down side of screen.
	 * @param tile the render tile enenty.
	 * @param lineScale the line scale, for consider with viewer distance and screen display style.
	 * @param width width of screen
	 * @param height height of screen.
	 * @param partialTicks the render partial ticks.
	 */
	void renderScreen(T tile, float lineScale, double width, double height, float partialTicks);
}