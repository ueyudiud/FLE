package farcore.lib.model.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelItemBase implements IRetexturableModel
{
	private IModelLayerProvider provider;
	private Map<String, IModelIconApplier> appliers;
	private String particle;
	private Map<String, Integer> tint;
	private Map<String, ResourceLocation> textures;
	private ItemOverrideList overrides;
	
	ModelItemBase(IModelLayerProvider provider, Map<String, IModelIconApplier> appliers,
			String particle, Map<String, Integer> tint,
			Map<String, ResourceLocation> textures, ItemOverrideList overrides)
	{
		this.provider = provider;
		this.appliers = appliers;
		this.particle = particle;
		this.tint = tint;
		this.textures = textures;
		this.overrides = overrides;
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return textures.values();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TextureAtlasSprite icon = null;
		ImmutableMap.Builder<String, List<BakedQuad>> quads = ImmutableMap.builder();
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		for(Entry<String, ResourceLocation> entry : textures.entrySet())
		{
			TextureAtlasSprite icon1 = bakedTextureGetter.apply(entry.getValue());
			if(particle.equals(entry.getKey()))
			{
				icon = icon1;
			}
			quads.put(entry.getKey(), ItemLayerModel.getQuadsForSprite(tint.getOrDefault(entry.getKey(), -1), icon, format, optional));
		}
		if(icon == null)
		{
			icon = bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
		}
		Map<String, List<BakedQuad>> map;
		return new BakedModelItemBase(provider, appliers, map = quads.build(), icon, map.get(particle), overrides);
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	@Override
	public IModel retexture(ImmutableMap<String, String> textures)
	{
		ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		for(Entry<String, ResourceLocation> entry : this.textures.entrySet())
		{
			if(textures.containsKey(entry.getKey()))
			{
				builder.put(entry.getKey(), new ResourceLocation(textures.get(entry.getKey())));
			}
		}
		return new ModelItemBase(provider, appliers, particle, tint, builder.build(), overrides);
	}
}