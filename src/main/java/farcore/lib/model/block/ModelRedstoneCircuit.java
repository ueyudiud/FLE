package farcore.lib.model.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.circuit.TECircuitBase;
import nebula.Log;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.ICustomItemRenderModel;
import nebula.client.model.INebulaCustomModelLoader;
import nebula.client.model.ModelBase;
import nebula.client.model.ModelHelper;
import nebula.client.model.flexible.ModelModifierByCoordTransformer;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.util.BakedQuadBuilder;
import nebula.common.util.L;
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
import net.minecraft.client.resources.IResourceManager;
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

@Deprecated
public class ModelRedstoneCircuit implements ModelBase, IRetexturableModel, IModelCustomData
{
	private static final float PLATE_HEIGHT = .125F;
	private static final IModel MODEL = new ModelRedstoneCircuit();
	
	public static enum RedstoneCircuitModelLoader implements INebulaCustomModelLoader
	{
		INSTANCE;
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			bakedQuads.clear();
		}
		
		@Override
		public String getModID()
		{
			return FarCore.INNER_RENDER;
		}
		
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
	
	/**
	 * For game was always crashing when I debugging.<br>
	 * It might takes too much memory to build quad, so I tried cached those quads.
	 * @author ueyudiud
	 */
	private static final Map<ResourceLocation, Map<Mat, Map<EnumFacing, List<BakedQuad>>>> bakedQuads = new HashMap<>();
	
	private IModel parent;
	private ResourceLocation layer;
	
	private ModelRedstoneCircuit()
	{
		this.layer = new ResourceLocation(FarCore.ID, "blocks/void");
	}
	private ModelRedstoneCircuit(ResourceLocation layer, IModel parent)
	{
		this.parent = parent;
		this.layer = layer;
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		List<ResourceLocation> locations = new ArrayList<>();
		locations.add(this.layer);
		if(this.parent != null)
		{
			locations.addAll(this.parent.getTextures());
		}
		return locations;
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite missing = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
		Map<Mat, Map<EnumFacing, List<BakedQuad>>> map = bakedQuads.get(this.layer);
		if(map == null)
		{
			TextureAtlasSprite layer = bakedTextureGetter.apply(this.layer);
			ImmutableMap.Builder<EnumFacing, List<BakedQuad>> builder = ImmutableMap.builder();
			for(EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				Optional<TRSRTransformation> transformation = Optional.of(new TRSRTransformation(facing));
				builder.put(facing, buildQuads(missing, layer, format, transformation));
			}
			Map<EnumFacing, List<BakedQuad>> map1 = builder.build();
			ImmutableMap.Builder<Mat, Map<EnumFacing, List<BakedQuad>>> builder2 = ImmutableMap.builder();
			for(Mat material : Mat.filt(SubTags.ROCK))
			{
				TextureAtlasSprite sprite;
				try
				{
					sprite = MaterialTextureLoader.getIcon(material, MC.stone);
				}
				catch(NullPointerException exception)
				{
					sprite = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
				}
				ImmutableMap.Builder<EnumFacing, List<BakedQuad>> builder1 = ImmutableMap.builder();
				for(Entry<EnumFacing, List<BakedQuad>> entry : map1.entrySet())
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
			bakedQuads.put(this.layer, map = builder2.build());
		}
		IBakedModel model = this.parent != null ? this.parent.bake(state, format, bakedTextureGetter) : null;
		return new BakedRedstoneCiruitModel(map, model);
	}
	
	@Override
	public IModel retexture(ImmutableMap<String, String> textures)
	{
		IModel parent0 = this.parent;
		if(this.parent instanceof IRetexturableModel)
		{
			parent0 = ((IRetexturableModel) this.parent).retexture(textures);
		}
		return new ModelRedstoneCircuit(
				getRealLocationFromPathOrDefault("layer", textures, this.layer), parent0);
	}
	
	@Override
	public IModel process(ImmutableMap<String, String> customData)
	{
		IModel parent0;
		if(this.parent == null && customData.containsKey("mixed"))
		{
			String mixed = customData.get("mixed");
			ResourceLocation location = new ResourceLocation(mixed.substring(1, mixed.length() - 1));
			parent0 = NebulaModelLoader.getModel(new ResourceLocation(location.getResourceDomain() + ":block/" + location.getResourcePath()));
			if (parent0 == ModelLoaderRegistry.getMissingModel())
			{
				parent0 = NebulaModelLoader.getModel(location);
			}
		}
		else
		{
			parent0 = this.parent;
		}
		if(parent0 instanceof IModelCustomData)
		{
			parent0 = ((IModelCustomData) parent0).process(customData);
		}
		return new ModelRedstoneCircuit(this.layer, parent0);
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
			Map<EnumFacing, List<BakedQuad>> map = this.quads.get(rock);
			List<BakedQuad> list;
			if(map != null)
			{
				list = new ArrayList<>(map.getOrDefault(EnumFacing.NORTH, ImmutableList.of()));
			}
			else
			{
				list = new ArrayList<>();
			}
			if(this.model != null)
			{
				list.addAll(this.model.getQuads(null, facing, rand));
			}
			return list;
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			if(side != null) return ImmutableList.of();
			if(state instanceof BlockStateTileEntityWapper)
			{
				@SuppressWarnings("unchecked")
				BlockStateTileEntityWapper<? extends TECircuitBase> wapper =
				(BlockStateTileEntityWapper<? extends TECircuitBase>) state;
				Map<EnumFacing, List<BakedQuad>> map = this.quads.get(wapper.tile.material);
				List<BakedQuad> list;
				if(map != null)
				{
					list = new ArrayList<>(map.getOrDefault(wapper.tile.facing.of(), ImmutableList.of()));
				}
				else
				{
					list = new ArrayList<>();
				}
				if(this.model != null)
				{
					list.addAll(this.model.getQuads(state, side, rand));
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
		public TextureAtlasSprite getParticleTexture() { return this.model != null ? this.model.getParticleTexture() : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite(); }
		
		@Override
		public ItemCameraTransforms getItemCameraTransforms() { return this.model != null ? this.model.getItemCameraTransforms() : ItemCameraTransforms.DEFAULT; }
		
		@Override
		public ItemOverrideList getOverrides() { return this.model != null ? this.model.getOverrides() : ItemOverrideList.NONE; }
		
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
		ImmutableList.Builder<BakedQuad> builder2 = ImmutableList.builder();
		BakedQuadBuilder builder = new BakedQuadBuilder(format,
				new ModelModifierByCoordTransformer(transformation.or(TRSRTransformation.identity()), null), builder2::add);
		builder.switchTextureScale();
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
					float maxY = (L.toUnsignedInt(height[idx]) + 1) * PLATE_HEIGHT / 256F;
					if(u1 == 0 || L.minusUbyte(height[idx - 1], height[idx]) > 0)
					{
						minY = u1 == 0 ? 0 : value(height[idx - 1]) * PLATE_HEIGHT / 256F;
						builder.startQuad(u1 == 0 ? EnumFacing.WEST : null, -1, texture);
						builder.normal(-1, 0, 0);//WEST
						builder.pos(minX, minY, minZ, minZ, minY);
						builder.pos(minX, minY, maxZ, maxZ, minY);
						builder.pos(minX, maxY, maxZ, maxZ, maxY);
						builder.pos(minX, maxY, minZ, minZ, maxY);
						builder.endQuad();
					}
					if(u1 == u - 1 || L.minusUbyte(height[idx + 1], height[idx]) > 0)
					{
						minY = u1 == u - 1 ? 0 : value(height[idx + 1]) * PLATE_HEIGHT / 256F;
						builder.startQuad(u1 == u-1 ? EnumFacing.EAST : null, -1, texture);
						builder.normal(1, 0, 0);//EAST
						builder.pos(maxX, minY, maxZ, minZ, minY);
						builder.pos(maxX, minY, minZ, maxZ, minY);
						builder.pos(maxX, maxY, minZ, maxZ, maxY);
						builder.pos(maxX, maxY, maxZ, minZ, maxY);
						builder.endQuad();
					}
					if(v1 == 0 || L.minusUbyte(height[idx - u], height[idx]) > 0)
					{
						minY = v1 == 0 ? 0 : value(height[idx - u]) * PLATE_HEIGHT / 256F;
						builder.startQuad(v1 == 0 ? EnumFacing.NORTH : null, -1, texture);
						builder.normal(0, 0, -1);//NORTH
						builder.pos(minX, minY, minZ, minX, minY);
						builder.pos(minX, maxY, minZ, minX, maxY);
						builder.pos(maxX, maxY, minZ, maxX, maxY);
						builder.pos(maxX, minY, minZ, maxX, minY);
						builder.endQuad();
					}
					if(v1 == v - 1 || L.minusUbyte(height[idx + u], height[idx]) > 0)
					{
						minY = v1 == v - 1 ? 0 : value(height[idx + u]) * PLATE_HEIGHT / 256F;
						builder.startQuad(v1 == v - 1 ? EnumFacing.SOUTH : null, -1, texture);
						builder.normal(0, 0, 1);//SOUTH
						builder.pos(maxX, minY, maxZ, minX, minY);
						builder.pos(maxX, maxY, maxZ, minX, maxY);
						builder.pos(minX, maxY, maxZ, maxX, maxY);
						builder.pos(minX, minY, maxZ, maxX, minY);
						builder.endQuad();
					}
					builder.startQuad(null, -1, texture);
					builder.normal(0, 1, 0);//UP
					builder.pos(minX, maxY, maxZ, minX, maxZ);
					builder.pos(maxX, maxY, maxZ, maxX, maxZ);
					builder.pos(maxX, maxY, minZ, maxX, minZ);
					builder.pos(minX, maxY, minZ, minX, minZ);
					builder.endQuad();
				}
			}
			builder.startQuad(null, -1, texture);
			builder.normal(0, -1, 0);//DOWN
			builder.pos(0, 0, 0, 0, 1);
			builder.pos(1, 0, 0, 1, 1);
			builder.pos(1, 0, 1, 1, 0);
			builder.pos(0, 0, 1, 0, 0);
			builder.endQuad();
		}
		return builder2.build();
	}
	
	private static int value(byte value)
	{
		return value == 0 ? 0 : L.toUnsignedInt(value) + 1;
	}
}