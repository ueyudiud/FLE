/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.Collection;
import java.util.function.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface INebulaModelPart
{
	Collection<String> getTextures();
	
	INebulaBakedModelPart bake(TRSRTransformation transformation, VertexFormat format, Function<String, TextureAtlasSprite> textureGetter);
}