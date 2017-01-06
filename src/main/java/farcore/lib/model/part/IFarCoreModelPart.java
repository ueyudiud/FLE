/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.part;

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
public interface IFarCoreModelPart
{
	Collection<String> getTextures();
	
	IFarCoreBakedModelPart bake(TRSRTransformation transformation, VertexFormat format, Function<String, TextureAtlasSprite> textureGetter);
}