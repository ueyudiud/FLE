/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import nebula.client.model.FlexibleItemModelCache.NebulaItemModelLayerCache;
import nebula.client.model.FlexibleItemSubmetaGetterLoader.SubmetaGetter;
import nebula.client.model.part.INebulaModelPart;
import nebula.common.util.L;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Unbaked item model.
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FlexibleItemModelUnbaked implements ModelBase
{
	static final float LAYER_OFFSET = 1E-3F;
	
	/**
	 * Create a new model with texture location.
	 * @param location
	 * @return
	 */
	public static IModel createNewModel(ResourceLocation location)
	{
		return new FlexibleItemModelUnbaked(location);
	}
	
	final Item item;
	Map<String, ResourceLocation> textures;
	Collection<ResourceLocation> particles;
	FlexibleItemModelLayerUnbaked[] layers;
	
	FlexibleItemModelUnbaked(ResourceLocation location)
	{
		this.item = null;
		this.textures = ImmutableMap.of(NebulaItemModelLoader.NORMAL, location);
		this.particles = ImmutableList.of(location);
		this.layers = new FlexibleItemModelLayerUnbaked[]{ new FlexibleItemModelLayerUnbaked(location) };
	}
	
	/**
	 * Cached model wrapper.
	 * @param cache
	 */
	FlexibleItemModelUnbaked(Item item, FlexibleItemModelCache cache)
	{
		this.item = item;//Item marker
		this.textures = cache.textureCol.buildTextureMap();
		this.particles = cache.textureCol.buildVariantMap(cache.particle).values();
		this.layers = new FlexibleItemModelLayerUnbaked[cache.caches.length];
		for(int i = 0; i < this.layers.length; ++i)
		{
			this.layers[i] = new FlexibleItemModelLayerUnbaked(cache.textureCol, cache.caches[i]);
		}
	}
	
	/**
	 * Get texture collection apply by model.<br>
	 * Still have something to do.
	 * TODO
	 */
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return this.textures.values();
	}
	
	/**
	 * Bake model.
	 */
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		TRSRTransformation transformation = optional.isPresent() ? optional.get() : TRSRTransformation.identity();
		java.util.function.Function<ItemStack, List<BakedQuad>>[] layers1 = new java.util.function.Function[this.layers.length];
		for(int i = 0; i < this.layers.length; ++i)
		{
			layers1[i] = this.layers[i].bake(transformation, format, bakedTextureGetter);
		}
		TextureAtlasSprite particleIcon;
		if(this.particles.size() != 1)
		{
			particleIcon = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
		}
		else
		{
			particleIcon = bakedTextureGetter.apply(this.particles.iterator().next());
		}
		return new FlexibleItemModel(layers1, particleIcon);
	}
	
	/**
	 * Unbaked model layer.
	 * @author ueyudiud
	 *
	 */
	class FlexibleItemModelLayerUnbaked implements INebulaModelPart
	{
		int layer;
		float zOffset;
		int baseColor;
		List<String> allowedVariants;
		List<String> convertsAllowTextures;
		SubmetaGetter variantApplier;
		ToIntFunction<ItemStack> colorMultiplier;
		
		byte layerType = 0;
		SubmetaGetter convertApplier = null;
		
		FlexibleItemModelLayerUnbaked(FlexibleTextureSet set, NebulaItemModelLayerCache cache)
		{
			this.layer = cache.index;
			this.zOffset = cache.zOffset;
			this.baseColor = cache.baseColor;
			if(cache.allowedVariants != null)
			{
				ImmutableList.Builder<String> builder = ImmutableList.builder();
				for(String variant : cache.allowedVariants)
				{
					builder.addAll(set.buildVariantMap(variant).keySet());
				}
				this.allowedVariants = builder.build();
			}
			if(cache.convertsAllowTextures != null)
			{
				ImmutableList.Builder<String> builder = ImmutableList.builder();
				for(String variant : cache.convertsAllowTextures)
				{
					builder.addAll(set.buildVariantMap(variant).keySet());
				}
				this.convertsAllowTextures = builder.build();
			}
			this.variantApplier = cache.variantApplier;
			this.colorMultiplier = cache.colorMultiplier;
			
			this.layerType = cache.layerType;
			this.convertApplier = cache.convertApplier;
		}
		
		/**
		 * Create new layer for single icon model
		 * @param location
		 */
		FlexibleItemModelLayerUnbaked(ResourceLocation location)
		{
			this.layer = 1;
			this.zOffset = 0.5F;
			this.baseColor = NebulaItemModelLoader.NORMAL_COLOR;
			this.allowedVariants = ImmutableList.of(NebulaItemModelLoader.NORMAL);
			this.convertsAllowTextures = ImmutableList.of();
			this.variantApplier = NebulaItemModelLoader.NORMAL_FUNCTION;
			this.colorMultiplier = NebulaItemModelLoader.NORMAL_MULTIPLIER;
		}
		
		@Override
		public Collection<String> getTextures()
		{
			ImmutableList.Builder<String> builder = ImmutableList.builder();
			builder.addAll(this.allowedVariants);
			builder.addAll(this.convertsAllowTextures);
			return builder.build();
		}
		
		@Override
		public INebulaBakedModelPart bake(TRSRTransformation transformation, VertexFormat format,
				java.util.function.Function<String, TextureAtlasSprite> textureGetter)
		{
			return INebulaBakedModelPart.bake(
					bake(transformation, format, (Function<ResourceLocation, TextureAtlasSprite>) location -> textureGetter.apply(location.toString())));
		}
		
		/**
		 * Wrap model layer as a Baked Quad applier, use the
		 * <code>ItemStack->List</code> logic.
		 * @param transformation
		 * @param format
		 * @param bakedTextureGetter
		 * @return
		 */
		public java.util.function.Function<ItemStack, List<BakedQuad>> bake(TRSRTransformation transformation, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			switch (this.layerType)
			{
			case 0 :
				Map<String, List<BakedQuad>> map = new HashMap();
				Collection<String> variants = this.allowedVariants != null ? this.allowedVariants : FlexibleItemModelUnbaked.this.textures.keySet();
				for(String variant : variants)
				{
					ResourceLocation location = FlexibleItemModelUnbaked.this.textures.get(variant);
					if(location == null) location = new ResourceLocation(variant);
					TextureAtlasSprite icon = bakedTextureGetter.apply(location);
					map.put(variant, ItemTextureHelper.getQuadsForSprite(this.layer, icon, format, transformation, this.zOffset + LAYER_OFFSET * this.layer, this.baseColor));
				}
				return this.variantApplier.andThen(L.toFunction(ImmutableMap.copyOf(map), ImmutableList.of()));
			case 1 :
				map = new HashMap();
				variants = this.allowedVariants != null ? this.allowedVariants : FlexibleItemModelUnbaked.this.textures.keySet();
				for(String variant : variants)
				{
					ResourceLocation location = FlexibleItemModelUnbaked.this.textures.get(variant);
					if(location == null) location = new ResourceLocation(variant);
					TextureAtlasSprite icon = bakedTextureGetter.apply(location);
					map.put(variant, ItemTextureHelper.getSurfaceQuadsForSprite(this.layer, format, transformation, this.zOffset + LAYER_OFFSET * this.layer, icon, this.baseColor));
				}
				return this.variantApplier.andThen(L.toFunction(ImmutableMap.copyOf(map), ImmutableList.of()));
			case 2 :
				if(this.convertsAllowTextures.size() == 1)
				{
					TextureAtlasSprite convert = bakedTextureGetter.apply(FlexibleItemModelUnbaked.this.textures.getOrDefault(this.convertsAllowTextures.get(0), new ResourceLocation(this.convertsAllowTextures.get(0))));
					map = new HashMap();
					variants = this.allowedVariants != null ? this.allowedVariants : FlexibleItemModelUnbaked.this.textures.keySet();
					for(String variant : variants)
					{
						ResourceLocation location = FlexibleItemModelUnbaked.this.textures.get(variant);
						if(location == null) location = new ResourceLocation(variant);
						TextureAtlasSprite icon = bakedTextureGetter.apply(location);
						ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
						builder.addAll(ItemTextureHelper.convertTexture(format, transformation, convert, icon, (8F - this.zOffset - LAYER_OFFSET * this.layer) / 16F, EnumFacing.NORTH, this.layer, this.baseColor));
						builder.addAll(ItemTextureHelper.convertTexture(format, transformation, convert, icon, (8F + this.zOffset + LAYER_OFFSET * this.layer) / 16F, EnumFacing.SOUTH, this.layer, this.baseColor));
						map.put(variant, builder.build());
					}
					return this.variantApplier.andThen(L.toFunction(ImmutableMap.copyOf(map), ImmutableList.of()));
				}
				else
				{
					ImmutableTable.Builder<String, String, List<BakedQuad>> builder = ImmutableTable.builder();
					for(String column : this.convertsAllowTextures)
					{
						TextureAtlasSprite icon1 = bakedTextureGetter.apply(FlexibleItemModelUnbaked.this.textures.getOrDefault(column, new ResourceLocation(column)));
						for(String row : this.allowedVariants)
						{
							TextureAtlasSprite icon = bakedTextureGetter.apply(FlexibleItemModelUnbaked.this.textures.getOrDefault(row, new ResourceLocation(row)));
							ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
							builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, icon1, icon, (8F - this.zOffset - LAYER_OFFSET * this.layer) / 16F, EnumFacing.NORTH, this.layer, this.baseColor));
							builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, icon1, icon, (8F + this.zOffset + LAYER_OFFSET * this.layer) / 16F, EnumFacing.SOUTH, this.layer, this.baseColor));
							builder.put(row, column, builder1.build());
						}
					}
					ImmutableTable<String, String, List<BakedQuad>> table = builder.build();
					return stack -> L.getOrDefault(table, this.variantApplier.apply(stack), this.convertApplier.apply(stack), ImmutableList.of());
				}
			default : //Missing layer type.
				return stack -> ImmutableList.of();
			}
		}
	}
}