/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import nebula.client.util.IIconCollection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public interface INebulaModelPart
{
	static INebulaModelPart VOID = new INebulaModelPart()
	{
		@Override
		public Collection<String> getResources()
		{
			return ImmutableList.of();
		}
		
		@Override
		public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
		{
			return (f, k)->ImmutableList.of();
		}
	};
	
	default Collection<ResourceLocation> getDependencies() { return ImmutableList.of(); }
	
	Collection<String> getResources();
	
	INebulaBakedModelPart bake(VertexFormat format,
			Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
			TRSRTransformation transformation);
}