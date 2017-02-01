/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.client;

import java.util.Collection;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.client.model.BakedModelBase;
import nebula.client.model.ICustomItemRenderModel;
import nebula.client.model.ModelBase;
import nebula.client.model.ModelHelper;
import nebula.client.model.ModelQuadBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public abstract class CompactModel implements ModelBase, BakedModelBase, IPerspectiveAwareModel, ICustomItemRenderModel
{
	protected ModelQuadBuilder qb = ModelQuadBuilder.newInstance();
	private final ImmutableMap<TransformType, TRSRTransformation> transformMap;
	
	protected CompactModel()
	{
		this(ModelHelper.BLOCK_STANDARD_TRANSFORMS);
	}
	protected CompactModel(ImmutableMap<TransformType, TRSRTransformation> map)
	{
		this.transformMap = map;
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of();//Use other texture loader.
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		this.qb.setFormat(format);
		return this;
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, this.transformMap, cameraTransformType);
	}
}