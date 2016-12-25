/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

import farcore.lib.model.block.ModelBase;
import farcore.lib.model.item.unused.FarCoreItemModelLoader.ItemModelCache;
import farcore.util.L;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class FarCoreItemModel extends ModelBase
{
	private static final EntryTransformer<String, ResourceLocation, Map<String, ResourceLocation>>
	TRANSFORMER = (String key, ResourceLocation location) -> FarCoreItemTextureSetLoader.getStoredTextureSet(location);
	
	/** Particle icon. */
	private ResourceLocation particle;
	/** Model name. */
	private @Nullable String name;
	/** Texture single. */
	private Map<String, ResourceLocation> texture1;
	/** Texture set. */
	private Map<String, Map<String, ResourceLocation>> texture2;
	private FarCoreItemLayerUnbaked[] layers;
	
	public FarCoreItemModel(ItemModelCache cache)
	{
		this.texture1 = ImmutableMap.copyOf(cache.textures);
		this.texture2 = Maps.transformEntries(cache.multiTextures, TRANSFORMER);
		this.layers = cache.layers;
		if(cache.particle == null)
		{
			this.particle = TextureMap.LOCATION_MISSING_TEXTURE;
		}
		else if(cache.particle.indexOf('.') != -1)
		{
			String key1 = cache.particle.substring(0, this.name.indexOf('.'));
			String key2 = cache.particle.substring(this.name.indexOf('.') + 1);
			if(this.texture2.containsKey(key1) && this.texture2.get(key1).containsKey(key2))
			{
				this.particle = this.texture2.get(key1).get(key2);
			}
			if(this.particle == null)
			{
				this.particle = this.texture1.getOrDefault(cache.particle, TextureMap.LOCATION_MISSING_TEXTURE);
			}
		}
		
		this.name = null;
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		Set<ResourceLocation> list = new HashSet(this.texture1.values());
		list.add(this.particle);
		L.executeAll(this.texture2.values(), map -> {list.addAll(map.values());});
		return list;
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		FarCoreItemLayer[] layers = new FarCoreItemLayer[this.layers.length];
		for(int i = 0; i < layers.length; ++i)
		{
			loadTextures(this.layers[i]);
			layers[i] = this.layers[i].bake(format, bakedTextureGetter, optional);
		}
		return new FarCoreItemModelBaked(layers, bakedTextureGetter.apply(this.particle));
	}
	
	private void loadTextures(FarCoreItemLayerUnbaked layer)
	{
		if(layer.texturePool == null)
		{
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			builder.putAll(this.texture1);
			for(Entry<String, Map<String, ResourceLocation>> entry : this.texture2.entrySet())
			{
				for(Entry<String, ResourceLocation> entry2 : entry.getValue().entrySet())
				{
					builder.put(entry.getKey() + "." + entry2.getKey(), entry2.getValue());
				}
			}
			layer.textureSets = builder.build();
		}
		else for(String string : layer.texturePool)
		{
			switch (string.charAt(0))
			{
			case '[' :
				String string2 = string.substring(1);
				if(this.texture2.containsKey(string2))
				{
					Map<String, ResourceLocation> map = this.texture2.get(string2);
					L.executeAll(map.entrySet(), entry ->
					{
						layer.textureSets.put(string2 + "." + entry.getKey(), entry.getValue());
					});
				}
				break;
			default:
				layer.textureSets.put(string, this.texture1.getOrDefault(string, TextureMap.LOCATION_MISSING_TEXTURE));
				break;
			}
		}
	}
}