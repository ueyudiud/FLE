package farcore.lib.model.block;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.lib.model.item.ItemTextureHelper;
import farcore.lib.util.Log;
import farcore.util.U.Maths;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelRedstoneCircuit extends ModelBase implements IRetexturableModel
{
	private static final float PLATE_HEIGHT = .125F;
	private static final ModelRedstoneCircuit MODEL = new ModelRedstoneCircuit(TextureMap.LOCATION_MISSING_TEXTURE, new ResourceLocation(FarCore.ID, "blocks/void"));
	
	public static enum RedstoneCircuitModelLoader implements ICustomModelLoader
	{
		INSTANCE;
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) { }
		
		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return FarCore.INNER_RENDER.equals(modelLocation.getResourceDomain()) &&
					modelLocation.getResourcePath().startsWith("redstone_circuit");
		}
		
		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			return MODEL;
		}
	}

	private ResourceLocation texture;
	private ResourceLocation layer;
	
	private ModelRedstoneCircuit(ResourceLocation texture, ResourceLocation layer)
	{
		this.texture = texture;
		this.layer = layer;
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of(texture, layer);
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite layer = bakedTextureGetter.apply(this.layer);
		TextureAtlasSprite sprite = bakedTextureGetter.apply(texture);
		return new BakedRedstoneCiruitModel(sprite, buildQuads(sprite, layer, format, Optional.of(TRSRTransformation.identity())));
	}

	@Override
	public IModel retexture(ImmutableMap<String, String> textures)
	{
		return new ModelRedstoneCircuit(
				getRealLocationFromPathOrDefault("texture", textures, texture),
				getRealLocationFromPathOrDefault("layer", textures, texture));
	}

	public static class BakedRedstoneCiruitModel implements IBakedModel
	{
		private TextureAtlasSprite sprite;
		private List<BakedQuad> quads;

		BakedRedstoneCiruitModel(TextureAtlasSprite sprite, List<BakedQuad> quads)
		{
			this.sprite = sprite;
			this.quads = quads;
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			return quads;
		}
		
		@Override
		public boolean isAmbientOcclusion() { return false; }
		
		@Override
		public boolean isGui3d() { return true; }
		
		@Override
		public boolean isBuiltInRenderer() { return false; }
		
		@Override
		public TextureAtlasSprite getParticleTexture() { return sprite; }
		
		@Override
		public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
		
		@Override
		public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
	}

	public static List<BakedQuad> buildQuads(TextureAtlasSprite texture, TextureAtlasSprite layer, VertexFormat format, Optional<TRSRTransformation> transformation)
	{
		if(texture.getIconWidth() != layer.getIconWidth() || texture.getIconHeight() != layer.getIconHeight())
		{
			Log.warn("The icon {%s, %s} pixel size is not same, plase use same size icon for uses, because it need take height render.", texture.getIconName(), layer.getIconName());
			return ImmutableList.of();
		}
		int u = texture.getIconWidth();
		int v = texture.getIconHeight();
		byte[] height = new byte[u * v];
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		int[] pixels = layer.getFrameTextureData(0)[0];//Only provide first frame for height.
		for(int v1 = 0; v < v; v1++)
		{
			for(int u1 = 0; u < u; u1++)
			{
				int idx = (u1 + v1 * u);
				height[idx] = (byte) (pixels[idx] >> 24 & 0xFF);
			}
		}
		for(int f = 0; f < layer.getFrameCount(); ++f)
		{
			for(int v1 = 0; v < v; v1++)
			{
				for(int u1 = 0; u < u; u1++)
				{
					int idx = (u1 + v1 * u);
					if(pixels[idx] == 0)
					{
						continue;
					}
					float minX = (float) u1 / (float) u;
					float maxX = (float) (u1 + 1) / (float) u;
					float minZ = (float) u1 / (float) u;
					float maxZ = (float) (u1 + 1) / (float) u;
					float minY;
					float maxY = pixels[idx] / PLATE_HEIGHT;
					float minU, maxU, minV, maxV;
					if(u1 == 0 || pixels[idx - 1] > pixels[idx])
					{
						minY = u1 == 0 ? 0 : pixels[idx - 1] / PLATE_HEIGHT;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minZ);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxZ);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						ItemTextureHelper.buildQuad(format, transformation, u1 == 0 ? EnumFacing.WEST : null, texture, -1,
								minX, minY, minZ, minU, minV,
								minX, minY, maxZ, maxU, minV,
								minX, maxY, maxZ, maxU, maxV,
								minX, maxY, minZ, minU, maxV);
					}
					if(u1 == u - 1 || pixels[idx + 1] > pixels[idx])
					{
						minY = u1 == u - 1 ? 0 : pixels[idx + 1] / PLATE_HEIGHT;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minZ);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxZ);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						ItemTextureHelper.buildQuad(format, transformation, u1 == u - 1 ? EnumFacing.EAST : null, texture, -1,
								maxX, minY, maxZ, minU, minV,
								maxX, minY, minZ, maxU, minV,
								maxX, maxY, minZ, maxU, maxV,
								maxX, maxY, maxZ, minU, maxV);
					}
					if(v1 == 0 || pixels[idx - u] > pixels[idx])
					{
						minY = v1 == 0 ? 0 : pixels[idx - u] / PLATE_HEIGHT;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minX);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxX);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						ItemTextureHelper.buildQuad(format, transformation, v1 == 0 ? EnumFacing.NORTH : null, texture, -1,
								minX, minY, minZ, minU, minV,
								maxX, minY, minZ, maxU, minV,
								maxX, maxY, minZ, maxU, maxV,
								minX, maxY, minZ, minU, maxV);
					}
					if(v1 == v - 1 || pixels[idx + u] > pixels[idx])
					{
						minY = v1 == v - 1 ? 0 : pixels[idx + u] / PLATE_HEIGHT;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minX);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxX);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						ItemTextureHelper.buildQuad(format, transformation, v1 == v - 1 ? EnumFacing.SOUTH : null, texture, -1,
								maxX, minY, minZ, minU, minV,
								minX, minY, minZ, maxU, minV,
								minX, maxY, minZ, maxU, maxV,
								maxX, maxY, minZ, minU, maxV);
					}
					ItemTextureHelper.buildQuad(format, transformation, EnumFacing.DOWN, texture, -1,
							0, 0, 0, texture.getMinU(), texture.getMaxV(),
							1, 0, 0, texture.getMaxU(), texture.getMaxV(),
							1, 0, 1, texture.getMaxU(), texture.getMinV(),
							0, 0, 1, texture.getMinU(), texture.getMinV());
				}
			}
		}
		return builder.build();
	}
}