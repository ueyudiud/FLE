package farcore.lib.model;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHelper
{
	private static final ResourceLocation LOCATION = new ResourceLocation("item/generated");

	public static IModel makeItemModel(String textureName)
	{
		return new ModelSurface(new ResourceLocation(textureName));
	}

	public static IModel makeItemModel(ResourceLocation location)
	{
		return new ModelSurface(location);
	}

	private static class ModelSurface implements IModel
	{
		private ResourceLocation location;
		
		public ModelSurface(ResourceLocation location)
		{
			this.location = location;
		}

		@Override
		public Collection<ResourceLocation> getTextures()
		{
			return ImmutableList.of(location);
		}
		
		@Override
		public Collection<ResourceLocation> getDependencies()
		{
			return ImmutableList.of();
		}
		
		@Override
		public IModelState getDefaultState()
		{
			return TRSRTransformation.identity();
		}
		
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			IModel model = ModelLoaderRegistry.getModelOrMissing(LOCATION);
			if(model instanceof IRetexturableModel)
			{
				model = ((IRetexturableModel) model).retexture(ImmutableMap.of("layer0", location.toString()));
			}
			return model.bake(state, format, bakedTextureGetter);
		}
	}
}