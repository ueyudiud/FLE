/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The icon register.
 * @author ueyudiud
 * @see nebula.client.render.IIconLoader
 * @see nebula.client.NebulaTextureHandler
 * @see net.minecraft.client.renderer.texture.TextureMap
 */
@SideOnly(Side.CLIENT)
public interface IIconRegister
{
	TextureAtlasSprite registerIcon(String domain, String path);
	
	TextureAtlasSprite registerIcon(ResourceLocation location);
	
	TextureAtlasSprite registerIcon(String key);
}