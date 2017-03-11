/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import nebula.client.model.part.INebulaModelPart;
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
	private boolean ao;
	private Set<ResourceLocation> locations;
	private ResourceLocation particle;
	private List<INebulaModelPart> parts;
	
	FlexibleBlockModelUnbaked(FlexibleBlockModelCache cache)
	{
		this.ao = cache.ambientOcclusion;
		Set<ResourceLocation> set = new HashSet<>();
		cache.parts.forEach(part->part.getTextures().forEach(loc->set.add(new ResourceLocation(loc))));
		this.particle = new ResourceLocation(cache.particleLocation);
		set.add(this.particle);
		this.locations = ImmutableSet.copyOf(set);
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
		FlexibleBlockModel model = new FlexibleBlockModel();
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		TRSRTransformation transformation = optional.isPresent() ? optional.get() : TRSRTransformation.identity();
		model.ao = this.ao;
		ImmutableList.Builder<INebulaBakedModelPart> builder = ImmutableList.builder();
		this.parts.forEach(part->builder.add(part.bake(transformation, format, s->bakedTextureGetter.apply(new ResourceLocation(s)))));
		model.parts = builder.build();
		model.particle = bakedTextureGetter.apply(this.particle);
		return model;
	}
}