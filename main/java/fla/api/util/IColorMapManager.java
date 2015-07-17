package fla.api.util;

import net.minecraft.util.ResourceLocation;

/**
 * Get color from a picture, this used to load grass color, reed color, etc.
 * The location name like assets/ + fileName + /texture/colormap/ + textureName.
 * @author ueyudiud
 *
 */
public interface IColorMapManager 
{
	public IColorMap getColorMap(ResourceLocation locate);
}
