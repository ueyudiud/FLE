package fla.api.util;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

/**
 * Get color from a picture, this used to load grass color, reed color, etc.
 * @author ueyudiud
 *
 */
public interface IColorMapManager extends IResourceManagerReloadListener
{
	public IColorMap getColorMap(IResourceManager manager, ResourceLocation locate);
}
