/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import nebula.client.model.part.INebulaModelPart;
import nebula.common.util.L;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
public class FlexibleBlockModelUnbaked implements ModelBase
{
	/** Is model enable ambient occlusion. */
	private boolean ao;
	/** The used texture locations. */
	private Set<ResourceLocation> locations;
	/** The particle location. */
	private ResourceLocation particle;
	/** The model part collection. */
	private List<INebulaModelPart> parts;
	
	FlexibleBlockModelUnbaked(FlexibleBlockModelCache cache)
	{
		this.ao = cache.ambientOcclusion;
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
		cache.parts.forEach(part -> part.getTextures().forEach(loc -> builder.add(new ResourceLocation(loc))));
		if (this.particle != null)
		{
			builder.add(this.particle);
		}
		this.particle = new ResourceLocation(cache.particleLocation);
		this.locations = builder.build();
		this.parts = ImmutableList.copyOf(cache.parts);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return this.locations;
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		TRSRTransformation transformation = L.get(state.apply(Optional.absent()), TRSRTransformation.identity());
		List<INebulaBakedModelPart> list = new ArrayList();
		java.util.function.Function<String, TextureAtlasSprite> function1 = s -> bakedTextureGetter.apply(new ResourceLocation(s));
		list.addAll(Lists.transform(this.parts, part -> part.bake(transformation, format, function1)));
		return new FlexibleBlockModel(this.ao, list, bakedTextureGetter.apply(this.particle));
	}
}