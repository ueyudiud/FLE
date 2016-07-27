package farcore.lib.model;

import java.util.Collection;
import java.util.Optional;

import javax.vecmath.Vector4f;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHelper
{
	private static final ResourceLocation LOCATION = new ResourceLocation("item/generated");

	public static void putBakeQuad(ImmutableList.Builder<BakedQuad> list, EnumFacing facing,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float x3, float y3, float z3, float x4, float y4, float z4,
			float u1, float v1, float u2, float v2, TextureAtlasSprite texture, VertexFormat format)
	{
		putBakeQuad(list, facing, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, texture, 0xFFFFFFFF, format);
	}
	public static void putBakeQuad(ImmutableList.Builder<BakedQuad> list, EnumFacing facing,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float x3, float y3, float z3, float x4, float y4, float z4,
			float u1, float v1, float u2, float v2, TextureAtlasSprite texture,
			int color, VertexFormat format)
	{
		putBakeQuad(list, facing, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, texture, color, color, color, color, Optional.empty(), format);
	}
	public static void putBakeQuad(ImmutableList.Builder<BakedQuad> list, EnumFacing facing,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float x3, float y3, float z3, float x4, float y4, float z4,
			float u1, float v1, float u2, float v2, TextureAtlasSprite texture,
			int color1, int color2, int color3, int color4, Optional<TRSRTransformation> transformation, VertexFormat format)
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		if(facing != null)
		{
			builder.setQuadOrientation(facing);
		}
		if(texture != null)
		{
			builder.setTexture(texture);
		}
		putVertex(format, builder, facing, x1, y1, z1, u1, v1, color1, transformation);
		putVertex(format, builder, facing, x2, y2, z2, u1, v2, color1, transformation);
		putVertex(format, builder, facing, x3, y3, z3, u2, v2, color1, transformation);
		putVertex(format, builder, facing, x4, y4, z4, u2, v1, color1, transformation);
		list.add(builder.build());
	}
	
	private static void putVertex(VertexFormat format,
			UnpackedBakedQuad.Builder builder,
			EnumFacing side, float x, float y, float z, float u, float v, int color, Optional<TRSRTransformation> transformation)
	{
		for(int e = 0; e < format.getElementCount(); e++)
		{
			switch(format.getElement(e).getUsage())
			{
			case POSITION:
				float[] data = new float[]{x, y, z, 1};
				if(transformation.isPresent() && transformation.get() != TRSRTransformation.identity())
				{
					Vector4f vec = new Vector4f(data);
					transformation.get().getMatrix().transform(vec);
					vec.get(data);
				}
				builder.put(e, data);
				break;
			case COLOR:
				builder.put(e,
						((color >> 16) & 0xFF) / 255f,
						((color >> 8) & 0xFF) / 255f,
						(color & 0xFF) / 255f,
						((color >> 24) & 0xFF) / 255f);
				break;
			case UV: if(format.getElement(e).getIndex() == 0)
			{
				builder.put(e, u, v, 0f, 1f);
				break;
			}
			case NORMAL:
				builder.put(e, side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ(), 0f);
				break;
			default:
				builder.put(e);
				break;
			}
		}
	}

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