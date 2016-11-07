package farcore.lib.model.block;

import static farcore.util.U.L.moreThanSigned;
import static farcore.util.U.L.unsignedToInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.IC;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.item.ICustomItemRenderModel;
import farcore.lib.model.item.ItemTextureHelper;
import farcore.lib.tile.instance.circuit.TECircuitBase;
import farcore.lib.util.Log;
import farcore.lib.util.SubTag;
import farcore.util.U.Maths;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelRedstoneCircuit extends ModelBase implements IRetexturableModel, IModelCustomData
{
	private static final float PLATE_HEIGHT = .125F;
	private static final IModel MODEL = new ModelRedstoneCircuit();

	public static enum RedstoneCircuitModelLoader implements IFarCustomModelLoader
	{
		INSTANCE;

		@Override
		public String getLoaderPrefix()
		{
			return "redstone_circuit";
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			return MODEL;
		}
	}
	
	private IModel parent;
	private ResourceLocation layer;

	private ModelRedstoneCircuit()
	{
		layer = new ResourceLocation(FarCore.ID, "blocks/void");
	}
	private ModelRedstoneCircuit(ResourceLocation layer, IModel parent)
	{
		this.parent = parent;
		this.layer = layer;
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		List<ResourceLocation> locations = new ArrayList();
		locations.add(layer);
		if(parent != null)
		{
			locations.addAll(parent.getTextures());
		}
		return locations;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite missing = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
		TextureAtlasSprite layer = bakedTextureGetter.apply(this.layer);
		ImmutableMap.Builder<EnumFacing, List<BakedQuad>> builder = ImmutableMap.builder();
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			Optional<TRSRTransformation> transformation = Optional.of(new TRSRTransformation(facing));
			builder.put(facing, buildQuads(missing, layer, format, transformation));
		}
		Map<EnumFacing, List<BakedQuad>> map = builder.build();
		ImmutableMap.Builder<Mat, Map<EnumFacing, List<BakedQuad>>> builder2 = ImmutableMap.builder();
		for(Mat material : Mat.filt(SubTag.ROCK))
		{
			TextureAtlasSprite sprite;
			try
			{
				sprite = IC.ROCK_ICONS.get(material).get(RockType.resource);
			}
			catch(NullPointerException exception)
			{
				sprite = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
			}
			ImmutableMap.Builder<EnumFacing, List<BakedQuad>> builder1 = ImmutableMap.builder();
			for(Entry<EnumFacing, List<BakedQuad>> entry : map.entrySet())
			{
				ImmutableList.Builder<BakedQuad> builder3 = ImmutableList.builder();
				for(BakedQuad quad : entry.getValue())
				{
					builder3.add(new BakedQuadRetextured(quad, sprite));
				}
				builder1.put(entry.getKey(), builder3.build());
			}
			builder2.put(material, builder1.build());
		}
		IBakedModel model = null;
		if(parent != null)
		{
			model = parent.bake(state, format, bakedTextureGetter);
		}
		return new BakedRedstoneCiruitModel(builder2.build(), model);
	}
	
	@Override
	public IModel retexture(ImmutableMap<String, String> textures)
	{
		IModel parent0 = parent;
		if(parent instanceof IRetexturableModel)
		{
			parent0 = ((IRetexturableModel) parent).retexture(textures);
		}
		return new ModelRedstoneCircuit(
				getRealLocationFromPathOrDefault("layer", textures, layer), parent0);
	}
	
	@Override
	public IModel process(ImmutableMap<String, String> customData)
	{
		IModel parent0;
		if(parent == null && customData.containsKey("mixed"))
		{
			String mixed = customData.get("mixed");
			ResourceLocation location = new ResourceLocation(mixed.substring(1, mixed.length() - 1));
			parent0 = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(location.getResourceDomain() + ":block/" + location.getResourcePath()));
		}
		else
		{
			parent0 = parent;
		}
		if(parent0 instanceof IModelCustomData)
		{
			parent0 = ((IModelCustomData) parent0).process(customData);
		}
		return new ModelRedstoneCircuit(layer, parent0);
	}
	
	public static class BakedRedstoneCiruitModel implements IBakedModel, ICustomItemRenderModel, IPerspectiveAwareModel
	{
		private static final ImmutableMap<TransformType, TRSRTransformation> BLOCK_TRANSFORMTION;

		private Map<Mat, Map<EnumFacing, List<BakedQuad>>> quads;
		private IBakedModel model;
		
		BakedRedstoneCiruitModel(Map<Mat, Map<EnumFacing, List<BakedQuad>>> quads, IBakedModel model)
		{
			this.quads = quads;
			this.model = model;
		}

		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			if(facing != null) return ImmutableList.of();
			Mat rock = TECircuitBase.getRockType(stack);
			Map<EnumFacing, List<BakedQuad>> map = quads.get(rock);
			List<BakedQuad> list;
			if(map != null)
			{
				list = new ArrayList(map.getOrDefault(EnumFacing.NORTH, ImmutableList.of()));
			}
			else
			{
				list = new ArrayList();
			}
			if(model != null)
			{
				list.addAll(model.getQuads(null, facing, rand));
			}
			return list;
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			if(side != null) return ImmutableList.of();
			if(state instanceof BlockStateTileEntityWapper)
			{
				BlockStateTileEntityWapper<TECircuitBase> wapper = (BlockStateTileEntityWapper) state;
				Map<EnumFacing, List<BakedQuad>> map = quads.get(wapper.tile.material);
				List<BakedQuad> list;
				if(map != null)
				{
					list = new ArrayList(map.getOrDefault(wapper.tile.facing.of(), ImmutableList.of()));
				}
				else
				{
					list = new ArrayList();
				}
				if(model != null)
				{
					list.addAll(model.getQuads(state, side, rand));
				}
				return list;
			}
			return ImmutableList.of();
		}

		@Override
		public boolean isAmbientOcclusion() { return false; }

		@Override
		public boolean isGui3d() { return true; }

		@Override
		public boolean isBuiltInRenderer() { return false; }

		@Override
		public TextureAtlasSprite getParticleTexture() { return model != null ? model.getParticleTexture() : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite(); }

		@Override
		public ItemCameraTransforms getItemCameraTransforms() { return model != null ? model.getItemCameraTransforms() : ItemCameraTransforms.DEFAULT; }

		@Override
		public ItemOverrideList getOverrides() { return model != null ? model.getOverrides() : ItemOverrideList.NONE; }

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, BLOCK_TRANSFORMTION, cameraTransformType);
		}

		static
		{
			ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
			builder.put(TransformType.NONE, TRSRTransformation.identity());
			builder.put(TransformType.THIRD_PERSON_LEFT_HAND,  ModelHelper.transformation(0.23483497F, 0.60772145F, 0.5F,        0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, ModelHelper.transformation(0.23483497F, 0.60772145F, 0.5F,        0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.FIRST_PERSON_LEFT_HAND,  ModelHelper.transformation(0.78284276F, 0.5F,        0.5F,        0.0F,       0.9238796F, 0.0F,       -0.3826834F, 0.4F,   0.4F,   0.4F,   0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, ModelHelper.transformation(0.78284276F, 0.5F,        0.5F,        0.0F,       0.9238796F, 0.0F,       -0.3826834F, 0.4F,   0.4F,   0.4F,   0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.HEAD,                    ModelHelper.transformation(1.0F,        1.0F,        1.0F,        0.0F,       1.0F,       0.0F,        0.0F,       0.25F,  0.25F,  0.25F,  0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.GUI,                     ModelHelper.transformation(0.94194174F, 0.22936705F, 0.34375006F,-0.1F,       0.9F,       0.23911762F,-0.37F,      0.625F, 0.625F, 0.625F, 0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.GROUND,                  ModelHelper.transformation(0.375F,      0.25F,       0.375F,      0.0F,       0.0F,       0.0F,        1.0F,       0.25F,  0.25F,  0.25F,  0.0F, 0.0F, 0.0F, 1.0F));
			builder.put(TransformType.FIXED,                   ModelHelper.transformation(0.25F,       0.25F,       0.25F,       0.0F,       0.0F,       0.0F,        1.0F,       0.5F,   0.5F,   0.5F,   0.0F, 0.0F, 0.0F, 1.0F));
			BLOCK_TRANSFORMTION = builder.build();
		}
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
		for(int v1 = 0; v1 < v; v1++)
		{
			for(int u1 = 0; u1 < u; u1++)
			{
				int idx = (u1 + v1 * u);
				height[idx] = (byte) (pixels[idx] >> 24);
			}
		}
		for(int f = 0; f < layer.getFrameCount(); ++f)
		{
			for(int v1 = 0; v1 < v; v1++)
			{
				for(int u1 = 0; u1 < u; u1++)
				{
					int idx = (u1 + v1 * u);
					if(height[idx] == 0)
					{
						continue;
					}
					float minX = (float) u1 / (float) u;
					float maxX = (float) (u1 + 1) / (float) u;
					float minZ = (float) v1 / (float) v;
					float maxZ = (float) (v1 + 1) / (float) v;
					float minY;
					float maxY = (unsignedToInt(height[idx]) + 1) * PLATE_HEIGHT / 256F;
					float minU, maxU, minV, maxV;
					if(u1 == 0 || moreThanSigned(height[idx - 1], height[idx]))
					{
						minY = u1 == 0 ? 0 : value(height[idx - 1]) * PLATE_HEIGHT / 256F;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minZ);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxZ);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						builder.add(ItemTextureHelper.buildBlockQuad(format, transformation, u1 == 0 ? EnumFacing.WEST : null, EnumFacing.WEST, texture, -1,
								minX, minY, minZ, minU, minV,
								minX, minY, maxZ, maxU, minV,
								minX, maxY, maxZ, maxU, maxV,
								minX, maxY, minZ, minU, maxV));
					}
					if(u1 == u - 1 || moreThanSigned(height[idx + 1], height[idx]))
					{
						minY = u1 == u - 1 ? 0 : value(height[idx + 1]) * PLATE_HEIGHT / 256F;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minZ);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxZ);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						builder.add(ItemTextureHelper.buildBlockQuad(format, transformation, u1 == u - 1 ? EnumFacing.EAST : null, EnumFacing.EAST, texture, -1,
								maxX, minY, maxZ, minU, minV,
								maxX, minY, minZ, maxU, minV,
								maxX, maxY, minZ, maxU, maxV,
								maxX, maxY, maxZ, minU, maxV));
					}
					if(v1 == 0 || moreThanSigned(height[idx - u], height[idx]))
					{
						minY = v1 == 0 ? 0 : value(height[idx - u]) * PLATE_HEIGHT / 256F;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minX);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxX);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						builder.add(ItemTextureHelper.buildBlockQuad(format, transformation, v1 == 0 ? EnumFacing.NORTH : null, EnumFacing.NORTH, texture, -1,
								minX, minY, minZ, minU, minV,
								minX, maxY, minZ, minU, maxV,
								maxX, maxY, minZ, maxU, maxV,
								maxX, minY, minZ, maxU, minV));
					}
					if(v1 == v - 1 || moreThanSigned(height[idx + u], height[idx]))
					{
						minY = v1 == v - 1 ? 0 : value(height[idx + u]) * PLATE_HEIGHT / 256F;
						minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minX);
						maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxX);
						minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minY);
						maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxY);
						builder.add(ItemTextureHelper.buildBlockQuad(format, transformation, v1 == v - 1 ? EnumFacing.SOUTH : null, EnumFacing.SOUTH, texture, -1,
								maxX, minY, maxZ, minU, minV,
								maxX, maxY, maxZ, minU, maxV,
								minX, maxY, maxZ, maxU, maxV,
								minX, minY, maxZ, maxU, minV));
					}
					minU = Maths.lerp(texture.getMinU(), texture.getMaxU(), minX);
					maxU = Maths.lerp(texture.getMinU(), texture.getMaxU(), maxX);
					minV = Maths.lerp(texture.getMinV(), texture.getMaxV(), minZ);
					maxV = Maths.lerp(texture.getMinV(), texture.getMaxV(), maxZ);
					builder.add(ItemTextureHelper.buildBlockQuad(format, transformation, null, EnumFacing.UP, texture, -1,
							minX, maxY, maxZ, minU, maxV,
							maxX, maxY, maxZ, maxU, maxV,
							maxX, maxY, minZ, maxU, minV,
							minX, maxY, minZ, minU, minV));
				}
			}
			builder.add(ItemTextureHelper.buildQuad(format, transformation, EnumFacing.DOWN, texture, -1,
					0, 0, 0, texture.getMinU(), texture.getMaxV(),
					1, 0, 0, texture.getMaxU(), texture.getMaxV(),
					1, 0, 1, texture.getMaxU(), texture.getMinV(),
					0, 0, 1, texture.getMinU(), texture.getMinV()));
		}
		return builder.build();
	}

	private static int value(byte value)
	{
		return value == 0 ? 0 : unsignedToInt(value) + 1;
	}
}