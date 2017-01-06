/*
 * copyright© 2016 ueyudiud
 */

package fle.core.client.model;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import farcore.lib.model.BakedModelBase;
import farcore.lib.model.ModelBase;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.ModelQuadBuilder;
import farcore.lib.model.block.statemap.BlockStateTileEntityWapper;
import farcore.lib.model.item.ICustomItemRenderModel;
import farcore.lib.util.Direction;
import fle.api.tile.IDitchTile;
import fle.core.tile.ditchs.TEDitch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelDitch implements ModelBase, BakedModelBase, IPerspectiveAwareModel, ICustomItemRenderModel
{
	ModelQuadBuilder qb = ModelQuadBuilder.newInstance();
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of();//Use material texture loader textures.
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
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelHelper.BLOCK_STANDARD_TRANSFORMS, cameraTransformType);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(state instanceof BlockStateTileEntityWapper)
		{
			TEDitch ditch = ((BlockStateTileEntityWapper<TEDitch>) state).tile;
			byte s = 0x0;
			for(Direction facing : Direction.DIRECTIONS_2D)
			{
				s |= (ditch.getLinkState(facing) << (facing.horizontalOrdinal * 2));
			}
			return genQuads(ditch.getMaterialIcon(), s);
		}
		return ImmutableList.of();
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
	{
		Mat material = Mat.material(stack.getItemDamage());
		return genQuads(IDitchTile.DitchBlockHandler.getFactory(material).getMaterialIcon(material), (byte) 0x11);
	}
	
	private List<BakedQuad> genQuads(TextureAtlasSprite retexture, byte connectState)
	{
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		this.qb.setIcon(retexture);
		//Block face pad.
		this.qb.renderZNeg = (connectState & 0x30) == 0;
		this.qb.renderZPos = (connectState & 0x03) == 0;
		this.qb.renderXNeg = (connectState & 0x0C) == 0;
		this.qb.renderXPos = (connectState & 0xC0) == 0;
		this.qb.putCubeQuads(builder, f5, f9, f5, f6, f7, f6);//Render bottom.
		//		this.qb.putCubeQuads(builder, this.f3, this.f9, this.f3, this.f4, this.f7, this.f4);
		this.qb.putCubeQuads(builder, this.f5, this.f9, this.f5, this.f3, this.f8, this.f3);
		this.qb.putCubeQuads(builder, this.f5, this.f9, this.f4, this.f3, this.f8, this.f6);
		this.qb.putCubeQuads(builder, this.f4, this.f9, this.f4, this.f6, this.f8, this.f6);
		this.qb.putCubeQuads(builder, this.f4, this.f9, this.f5, this.f6, this.f8, this.f3);
		this.qb.renderXNeg = this.qb.renderXPos = this.qb.renderZNeg = this.qb.renderZPos = true;
		switch (connectState & 0x03)//Z pos
		{
		case 0x02:
			this.qb.putCubeQuads(builder, this.f5, this.f9, this.f6, this.f3, this.f8, this.f11);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f6, this.f6, this.f8, this.f11);
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f4, this.f4, this.f7, this.f11);
			break;
		case 0x01:
			this.qb.putCubeQuads(builder, this.f5, this.f9, this.f6, this.f3, this.f8, this.f2);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f6, this.f6, this.f8, this.f2);
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f4, this.f4, this.f7, this.f2);
			break;
		case 0x00 :
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f4, this.f4, this.f8, this.f6);
			break;
		}
		switch (connectState & 0x0C)//X neg
		{
		case 0x08:
			this.qb.putCubeQuads(builder, this.f10, this.f9, this.f5, this.f5, this.f8, this.f3);
			this.qb.putCubeQuads(builder, this.f10, this.f9, this.f4, this.f5, this.f8, this.f6);
			this.qb.putCubeQuads(builder, this.f10, this.f9, this.f3, this.f3, this.f7, this.f4);
			break;
		case 0x04:
			this.qb.putCubeQuads(builder, this.f1, this.f9, this.f5, this.f5, this.f8, this.f3);
			this.qb.putCubeQuads(builder, this.f1, this.f9, this.f4, this.f5, this.f8, this.f6);
			this.qb.putCubeQuads(builder, this.f1, this.f9, this.f3, this.f3, this.f7, this.f4);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, this.f5, this.f9, this.f3, this.f3, this.f8, this.f4);
			break;
		}
		switch (connectState & 0x30)//Z neg
		{
		case 0x20:
			this.qb.putCubeQuads(builder, this.f5, this.f9, this.f10, this.f3, this.f8, this.f5);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f10, this.f6, this.f8, this.f5);
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f10, this.f4, this.f7, this.f3);
			break;
		case 0x10:
			this.qb.putCubeQuads(builder, this.f5, this.f9, this.f1, this.f3, this.f8, this.f5);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f1, this.f6, this.f8, this.f5);
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f1, this.f4, this.f7, this.f3);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, this.f3, this.f9, this.f5, this.f4, this.f8, this.f3);
			break;
		}
		switch (connectState & 0xC0)//X pos
		{
		case 0x80:
			this.qb.putCubeQuads(builder, this.f6, this.f9, this.f5, this.f11, this.f8, this.f3);
			this.qb.putCubeQuads(builder, this.f6, this.f9, this.f4, this.f11, this.f8, this.f6);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f3, this.f11, this.f7, this.f4);
			break;
		case 0x40:
			this.qb.putCubeQuads(builder, this.f6, this.f9, this.f5, this.f2, this.f8, this.f3);
			this.qb.putCubeQuads(builder, this.f6, this.f9, this.f4, this.f2, this.f8, this.f6);
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f3, this.f2, this.f7, this.f4);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, this.f4, this.f9, this.f3, this.f6, this.f8, this.f4);
			break;
		}
		return builder.build();
	}
	
	private static final float f   = 0.0625F;//Pixel length.
	private static final float f1  = 0.0F;//Block bound min.
	private static final float f2  = 1.0F;//Block bound max.
	private static final float f3  = 0.375F;//Ditch side min.
	private static final float f4  = 0.625F;//Ditch side max.
	private static final float f5  = f3 - f;//Ditch edge(state=0) min.
	private static final float f6  = f4 + f;//Ditch edge(state=0) max.
	private static final float f7  = 0.25F;//Ditch surface height.
	private static final float f8  = 0.5F;//Ditch wall height.
	private static final float f9  = f7 - f;//Ditch bottom height.
	private static final float f10 = -0.375F;//Ditch edge(state=2) min.
	private static final float f11 = 1.375F;//(Ditch edge(state=2) max.
	//	private static final float f12 = -0.625F;
	//	private static final float f13 = 1.625F;
	//	private static final float f14 = -0.5F;
}