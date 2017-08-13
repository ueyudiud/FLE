/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.Log;
import nebula.client.model.ModelBase;
import nebula.client.util.IIconCollection;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class FlexibleModel implements ModelBase, IRetexturableModel, IRecolorableModel
{
	private List<INebulaModelPart> parts;
	private boolean gui3D;
	private boolean builtIn;
	private Item item;
	ImmutableMap<TransformType, TRSRTransformation> transforms;
	java.util.function.Function<ItemStack, String>[] itemDataGen;
	java.util.function.Function<IBlockState, String>[] blockDataGen;
	int[] itemLoadingData;
	int[] blockLoadingData;
	ToIntFunction<ItemStack>[] itemColors;
	ToIntFunction<IBlockState>[] blockColors;
	
	private Map<String, String> retextures;
	
	public FlexibleModel(Item item, ImmutableMap<TransformType, TRSRTransformation> transforms,
			List<INebulaModelPart> parts, boolean gui3D, boolean builtIn)
	{
		this.item = item;
		this.transforms = transforms;
		this.parts = parts;
		this.gui3D = gui3D;
		this.builtIn = builtIn;
	}
	
	private Map<String, IIconCollection> resources;
	private Collection<ResourceLocation> textures;
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		List<ResourceLocation> list = new ArrayList<>();
		this.parts.forEach(part->list.addAll(part.getDependencies()));
		return list;
	}
	
	public void loadResources()
	{
		Set<String> keys = new HashSet<>();
		keys.add("#particle");
		this.textures = new HashSet<>();
		this.parts.forEach(part-> {
			keys.addAll(part.getResources());
			if (part instanceof INebulaDirectResourcesModelPart)
			{
				this.textures.addAll(((INebulaDirectResourcesModelPart) part).getDirectResources());
			}
		});
		this.resources = new HashMap<>();
		keys.forEach(key-> {
			IIconCollection handler = getIconHandler(key);
			this.textures.addAll(handler.resources());
			this.resources.put(key, handler);
		});
		this.textures = ImmutableList.copyOf(this.textures);
		this.resources = ImmutableMap.copyOf(this.resources);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		if (this.textures == null) { loadResources(); }
		return this.textures;
	}
	
	public IIconCollection getIconHandler(String key)
	{
		return $getIconHandler(key, new ArrayList<>());
	}
	
	private IIconCollection $getIconHandler(String key, List<String> keys)
	{
		if (key.charAt(0) == '#')
		{
			key = key.substring(1);
			if (keys.contains(key))
			{
				Log.warn("The '{}' is looped loading, use missingno texture instead.", key);
				return NebulaModelLoader.ICON_HANDLER_MISSING;
			}
			keys.add(key);
			if (this.retextures == null || !this.retextures.containsKey(key))
				return NebulaModelLoader.ICON_HANDLER_MISSING;
			return $getIconHandler(this.retextures.get(key), keys);
		}
		else
		{
			return NebulaModelLoader.loadIconHandler(key);
		}
	}
	
	/**
	 * Bake model.
	 * @param state Unused.
	 */
	@Override
	public FlexibleBakedModel bake(@Nullable IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TRSRTransformation transformation = state.apply(Optional.absent()).or(TRSRTransformation.identity());
		ImmutableList.Builder<INebulaBakedModelPart> builder = ImmutableList.builder();
		this.parts.forEach(part->builder.add(
				part.bake(format, L.toFunction(this.resources, NebulaModelLoader.ICON_HANDLER_MISSING), bakedTextureGetter::apply, transformation)));
		IIconCollection particleSource = getIconHandler("#particle");
		TextureAtlasSprite particle = bakedTextureGetter.apply(particleSource.build().getOrDefault(NebulaModelLoader.NORMAL, TextureMap.LOCATION_MISSING_TEXTURE));
		return new FlexibleBakedModel(this.transforms, builder.build(), particle,
				this.gui3D, this.builtIn, this.itemDataGen, this.blockDataGen,
				this.itemLoadingData, this.blockLoadingData);
	}
	
	@Override
	public FlexibleModel retexture(ImmutableMap<String, String> textures)
	{
		if (textures == null || textures.isEmpty()) return this;
		Map<String, String> builder = new HashMap<>();
		if (this.retextures != null)
			builder.putAll(this.retextures);
		builder.putAll(textures);
		FlexibleModel model = new FlexibleModel(this.item, this.transforms, this.parts, this.gui3D, this.builtIn);
		model.retextures = ImmutableMap.copyOf(builder);
		return model;
	}
	
	@Override
	public void registerColorMultiplier(BlockColors colors)
	{
		if (this.item != null && this.blockColors != null)
		{
			final ToIntFunction<IBlockState>[] functions = this.blockColors;
			colors.registerBlockColorHandler((state, worldIn, pos, tintIndex)->
			tintIndex >= functions.length || tintIndex < 0 ? -1 : functions[tintIndex].applyAsInt(state), Block.getBlockFromItem(this.item));
		}
	}
	
	@Override
	public void registerColorMultiplier(ItemColors colors)
	{
		if (this.item != null && this.itemColors != null)
		{
			final ToIntFunction<ItemStack>[] functions = this.itemColors;
			colors.registerItemColorHandler((stack, tintIndex)->
			tintIndex >= functions.length || tintIndex < 0 ? -1 : functions[tintIndex].applyAsInt(stack), this.item);
		}
	}
}