package fle.core.render.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveState;
import net.minecraftforge.client.model.ModelLoader.UVLock;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

@Deprecated
public class Model implements IModelCustomData
{	
	private ModelType type;
	
	private static enum ModelType
	{
		ITEM,
		BLOCK;
	}
	
	private Model parent;
	private ResourceLocation locate;
	private List<ResourceLocation> textures;
	private List<Model> parts;
	private ModelState state;
	private ItemCameraTransforms ict;
	private IModelTextureResolver resolver;
	
	public Model(ResourceLocation parent)
	{
		
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		Set<ResourceLocation> set = new HashSet();
		if(parent != null && !parent.locate.getResourceDomain().equals("build")) set.add(parent.locate);
		for(Model part : parts)
		{
			set.addAll(part.getDependencies());
		}
		return set;
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		Set<ResourceLocation> set = new HashSet();
		if(parent != null) set.addAll(parent.getDependencies());
		for(Model part : parts)
		{
			set.addAll(part.getTextures());
		}
		set.addAll(textures);
		return set;
	}

	@Override
	public IFlexibleBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		boolean uvLock = false;
		if(state instanceof UVLock)
		{
			state = ((UVLock) state).getParent();
			uvLock = true;
		}
		ItemCameraTransforms camera = ict == null ? ItemCameraTransforms.DEFAULT : ict;
		IPerspectiveState state1 = state instanceof IPerspectiveState ? (IPerspectiveState) state : new IPerspectiveState.Impl(state, camera);
		switch(type)
		{
//		case ITEM : return new BakedItemModel(resolver, state1, state.apply(this), format, bakedTextureGetter, uvLock);
//		case BLOCK : return new BakedBlockModel(resolver, state1, state.apply(this), format, bakedTextureGetter, uvLock);
		default : return null;
		}
	}

	@Override
	public IModelState getDefaultState()
	{
		return ModelState.DEFAULT;
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData)
	{
		return this;
	}
}