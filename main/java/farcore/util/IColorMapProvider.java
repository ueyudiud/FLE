package farcore.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The color map provider, use to provider color map during loading.
 * Register this to far land era proxy (Color map handler).
 * @author ueyudiud
 * @see farcore.FarCore
 */
@SideOnly(Side.CLIENT)
public interface IColorMapProvider
{
	void registerColorMap(IColorMapHandler handler);
}