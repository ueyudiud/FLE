/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.function.Function;

import nebula.client.util.IIconCollection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelPartItemLayerConvert extends ModelPartItemLayer
{
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		//TODO
		return super.bake(format, iconHandlerGetter, bakedTextureGetter, transformation);
	}
}