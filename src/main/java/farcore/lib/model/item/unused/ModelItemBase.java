package farcore.lib.model.item.unused;

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

import farcore.FarCore;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.block.ModelBase;
import farcore.lib.model.item.unused.FarCoreItemModelLoader.ItemModelCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * FarCoreItemModelLoader compacted model.
 * @author ueyudiud
 *
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class ModelItemBase extends ModelBase
{
	/** The collection contain all textures might using in model. */
	private Map<String, ResourceLocation> textureCol = new HashMap();
	private Map<String, ResourceLocation> textureSet1 = new HashMap();
	private Map<String, Map<String, ResourceLocation>> textureSetOthers = new HashMap();
	/** The model layers. */
	private UnbakedModelLayer[] layerProviders;
	/** Particle icon. */
	private ResourceLocation particle;
	
	public ModelItemBase(FarCoreItemModelLoader loader, ItemModelCache cache)
	{
		//		this.layerProviders = cache.layers;
		this.textureCol.putAll(cache.textures);
		this.textureSet1 = cache.textures;
		for (Entry<String, ResourceLocation> entry : cache.multiTextures.entrySet())
		{
			Map<String, ResourceLocation> map = FarCoreItemTextureSetLoader.getStoredTextureSet(entry.getValue());
			this.textureCol.putAll(map);
			this.textureSetOthers.put(entry.getKey(), map);
		}
		//		for (UnbakedModelLayer layer : cache.layers)
		//		{
		//			this.textureCol.putAll(layer.locations);
		//		}
		this.particle = this.textureCol.get(FarCoreItemModelLoader.PARTICLE);
		if (this.particle == null)
		{
			this.particle = this.textureCol.get(FarCoreItemModelLoader.NORMAL);
		}
		if (this.particle == null)
		{
			this.particle = TextureMap.LOCATION_MISSING_TEXTURE;//Use missing texture if no possible texture detected.
		}
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return this.textureCol.values();
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		ModelLayer[] layers = new ModelLayer[this.layerProviders.length];
		for(int i = 0; i < layers.length; ++i)
		{
			this.layerProviders[i].loadTexturesAndSubmetaGetter(this.textureSet1, this.textureSetOthers);
			layers[i] = this.layerProviders[i].bake(format, bakedTextureGetter, optional);
		}
		return new BakedModelItemBase(layers, bakedTextureGetter.apply(this.particle));
	}
	
	@SideOnly(Side.CLIENT)
	public static class BakedModelItemBase implements ICustomItemRenderModel, IPerspectiveAwareModel
	{
		private boolean isCulled;
		private IBakedModel submodel;
		private ModelLayer[] layers;
		private TextureAtlasSprite particleIcon;
		
		public BakedModelItemBase(ModelLayer[] layers, TextureAtlasSprite particle)
		{
			this(null, layers, particle);
		}
		BakedModelItemBase(IBakedModel model, ModelLayer[] layers, TextureAtlasSprite particle)
		{
			if(model == null)
			{
				this.isCulled = false;
				this.submodel = new BakedModelItemBase(this, layers, particle);
			}
			else
			{
				this.isCulled = true;
				this.submodel = model;
			}
			this.particleIcon = particle;
			this.layers = layers;
		}
		
		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			if(facing != null) return ImmutableList.of();
			try
			{
				ArrayList<BakedQuad> list = new ArrayList();
				for (ModelLayer layer : this.layers)
				{
					List<BakedQuad> list1 = layer.getQuads(stack);
					if(list1 != null)
					{
						list.addAll(list1);
					}
				}
				return list;
			}
			catch (Exception exception)
			{
				if(FarCore.debug) throw exception;
				return ImmutableList.of();
			}
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) { return ImmutableList.of(); }
		
		@Override
		public boolean isAmbientOcclusion() { return true; }
		
		@Override
		public boolean isGui3d() { return false; }
		
		@Override
		public boolean isBuiltInRenderer() { return false; }
		
		/**
		 * This method is not suggested to use to get particle texture, use other
		 * texture instead.
		 */
		@Override
		public TextureAtlasSprite getParticleTexture() { return this.particleIcon; }
		
		@Override
		public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
		
		@Override
		public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
		{
			Pair<? extends IBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelHelper.ITEM_STANDARD_TRANSFORMS, type);
			if(type == TransformType.GUI && !this.isCulled && pair.getRight() == null)
				return Pair.of(this.submodel, null);
			else if(type != TransformType.GUI && this.isCulled)
				return Pair.of(this.submodel, pair.getRight());
			return pair;
		}
	}
}