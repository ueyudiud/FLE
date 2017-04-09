package nebula.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.Log;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public interface ModelBase extends IModel
{
	@Override
	default Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}
	
	@Override
	default IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
	
	default ResourceLocation getRealLocationFromPath(String key, ImmutableMap<String, String> map)
	{
		return getRealLocationFromPathOrDefault(key, map, TextureMap.LOCATION_MISSING_TEXTURE);
	}
	
	default ResourceLocation getRealLocationFromPathOrDefault(String key, ImmutableMap<String, String> map, ResourceLocation def)
	{
		String key1 = key;
		List<String> currents = new ArrayList<>();
		do
		{
			key1 = map.get(key1);
			if(currents.contains(key1))
			{
				Log.warn("The %s.%s texture path is circulated.", toString(), key);
				return def;
			}
			currents.add(key1);
			if(key1 == null) return def;
		}
		while (key1.charAt(0) == '#');
		return new ResourceLocation(key1);
	}
	
	static IModel makeWrapperModel(IModel model)
	{
		return new RetextureWrapperModel(model);
	}
	
	class RetextureWrapperModel implements IRetexturableModel
	{
		private IModel parent;
		private ImmutableMap<String, String> textures;
		
		private RetextureWrapperModel(IModel model)
		{
			this(model, ImmutableMap.of());
		}
		private RetextureWrapperModel(IModel model, ImmutableMap<String, String> textures)
		{
			parent = model;
			this.textures = textures;
		}
		
		@Override
		public Collection<ResourceLocation> getDependencies()
		{
			return parent.getDependencies();
		}
		
		@Override
		public Collection<ResourceLocation> getTextures()
		{
			return parent.getTextures();
		}
		
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			return parent.bake(state, format, bakedTextureGetter);
		}
		
		@Override
		public IModelState getDefaultState()
		{
			return parent.getDefaultState();
		}
		
		@Override
		public IModel retexture(ImmutableMap<String, String> textures)
		{
			Map<String, String> map = new HashMap<>(this.textures);
			map.putAll(textures);
			if(!(parent instanceof IRetexturableModel))
				return new RetextureWrapperModel(parent, ImmutableMap.copyOf(map));
			ImmutableMap<String, String> map1 = ImmutableMap.copyOf(map);
			IModel model = ((IRetexturableModel) parent).retexture(map1);
			if(model instanceof RetextureWrapperModel)
			{
				model = ((RetextureWrapperModel) model).parent;
			}
			return new RetextureWrapperModel(model, map1);
		}
	}
}