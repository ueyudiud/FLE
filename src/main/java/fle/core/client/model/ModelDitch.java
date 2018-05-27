/*
 * copyright 2016-2018 ueyudiud
 */

package fle.core.client.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import fle.api.client.CompactModel;
import fle.api.ditch.DitchBlockHandler;
import fle.core.tile.ditchs.TEDitch;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.util.CoordTransformer;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelDitch extends CompactModel
{
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (state instanceof BlockStateTileEntityWapper)
		{
			TEDitch ditch = ((BlockStateTileEntityWapper<TEDitch>) state).tile;
			byte s = 0x0;
			for (Direction facing : Direction.DIRECTIONS_2D)
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
		return genQuads(DitchBlockHandler.getFactory(material).getMaterialIcon(material), (byte) 0x11);
	}
	
	private static final float	fPixel	= 0.0625F;
	private static final float	fMin	= 0.0F;						// Block
																	// bound
																	// min.
	private static final float	fMax	= 1.0F;						// Block
																	// bound
																	// max.
	private static final float	fWN		= 0.375F;				// Ditch side
																// min.
	private static final float	fWP		= 0.625F;				// Ditch side
																// max.
	private static final float	fEN		= fWN - fPixel;		// Ditch
															// edge(state=0)
															// min.
	private static final float	fEP		= fWP + fPixel;		// Ditch
															// edge(state=0)
															// max.
	private static final float	fSP		= 0.25F;				// Ditch surface
																// height.
	private static final float	fW		= 0.5F;						// Ditch
																	// wall
																	// height.
	private static final float	fSN		= fSP - fPixel;		// Ditch bottom
															// height.
	// private static final float f10 = -0.375F;//Ditch edge(state=2) min.
	// private static final float f11 = 1.375F;//(Ditch edge(state=2) max.
	// private static final float f12 = -0.625F;
	// private static final float f13 = 1.625F;
	// private static final float f14 = -0.5F;
	
	private List<BakedQuad> genQuads(TextureAtlasSprite retexture, byte connectState)
	{
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		this.qb.setIcon(retexture);
		this.qb.setApplyDiffuseLighting(true);
		this.qb.setBound(fWN, fSP, fWN, fWP, fSP, fWP);
		builder.add(this.qb.bakeYPosFace());
		this.qb.setBound(fEN, fSN, fEN, fEP, fSN, fEP);
		builder.add(this.qb.bakeYNegFace());
		CoordTransformer transformer = this.qb.getTransformer();
		this.qb.resetOption();
		switch (connectState & 0x03)// Z pos
		{
		case 0x02:
			transformer.setTransform(0.0, 0.0, 1.0);
			this.qb.renderYPos = this.qb.renderZNeg = this.qb.renderZPos = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fMin, fEP, fW, fWN);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWN, fSP, fMin, fWP, fW, fWN);
			this.qb.renderOppisite = false;
			this.qb.renderYPos = this.qb.renderZPos = true;
			this.qb.renderXPos = this.qb.renderXNeg = this.qb.renderYNeg = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fMin, fWN, fW, fWN);
			this.qb.putCubeQuads(builder, fWP, fSN, fMin, fEP, fW, fWN);
			this.qb.setBound(fWN, fSN, fMin, fWP, fSP, fWN);
			builder.add(this.qb.bakeZPosFace());
			this.qb.resetOption();
		case 0x01:
			this.qb.renderYPos = this.qb.renderZNeg = this.qb.renderZPos = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fEP, fEP, fW, fMax);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWN, fSP, fWP, fWP, fW, fMax);
			this.qb.renderOppisite = false;
			this.qb.setBound(fEN, fSN, fWP, fWN, fW, fMax);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fWP, fSN, fEP, fEP, fW, fMax);
			builder.add(this.qb.bakeYPosFace());
			break;
		case 0x00:
			this.qb.setBound(fEN, fSN, fWP, fWP, fW, fEP);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fEN, fSN, fWP, fEP, fW, fEP);
			builder.add(this.qb.bakeZPosFace());
			this.qb.setBound(fWN, fSP, fWP, fWP, fW, fEP);
			builder.add(this.qb.bakeZNegFace());
			break;
		}
		this.qb.resetOption();
		switch (connectState & 0x0C)// X neg
		{
		case 0x08:
			transformer.setTransform(-1.0, 0.0, 0.0);
			this.qb.renderYPos = this.qb.renderXNeg = this.qb.renderXPos = false;
			this.qb.putCubeQuads(builder, fWP, fSN, fEN, fMax, fW, fEP);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWP, fSP, fWN, fMax, fW, fWP);
			this.qb.renderOppisite = false;
			this.qb.renderYPos = this.qb.renderXNeg = true;
			this.qb.renderZPos = this.qb.renderZNeg = this.qb.renderYNeg = false;
			this.qb.putCubeQuads(builder, fWP, fSN, fEN, fMax, fW, fWN);
			this.qb.putCubeQuads(builder, fWP, fSN, fWP, fMax, fW, fEP);
			this.qb.renderYNeg = true;
			this.qb.setBound(fWP, fSN, fWN, fMax, fSP, fWP);
			builder.add(this.qb.bakeXNegFace());
			this.qb.resetOption();
		case 0x04:
			this.qb.renderYPos = this.qb.renderXNeg = this.qb.renderXPos = false;
			this.qb.putCubeQuads(builder, fMin, fSN, fEN, fEN, fW, fEP);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fMin, fSP, fWN, fWN, fW, fWP);
			this.qb.renderOppisite = false;
			this.qb.setBound(fMin, fSN, fEN, fWN, fW, fWN);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fMin, fSN, fWP, fEN, fW, fEP);
			builder.add(this.qb.bakeYPosFace());
			break;
		case 0x00:
			this.qb.setBound(fEN, fSN, fEN, fWN, fW, fWP);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fEN, fSN, fEN, fWN, fW, fEP);
			builder.add(this.qb.bakeXNegFace());
			this.qb.setBound(fEN, fSP, fWN, fWN, fW, fWP);
			builder.add(this.qb.bakeXPosFace());
			break;
		}
		this.qb.resetOption();
		switch (connectState & 0x30)// Z neg
		{
		case 0x20:
			transformer.setTransform(0.0, 0.0, -1.0);
			this.qb.renderYPos = this.qb.renderZNeg = this.qb.renderZPos = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fWP, fEP, fW, fMax);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWN, fSP, fWP, fWP, fW, fMax);
			this.qb.renderOppisite = false;
			this.qb.renderYPos = this.qb.renderZNeg = true;
			this.qb.renderXPos = this.qb.renderXNeg = this.qb.renderYNeg = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fWP, fWN, fW, fMax);
			this.qb.putCubeQuads(builder, fWP, fSN, fWP, fEP, fW, fMax);
			this.qb.setBound(fWN, fSN, fWP, fWP, fSP, fMax);
			builder.add(this.qb.bakeZNegFace());
			this.qb.resetOption();
		case 0x10:
			this.qb.renderYPos = this.qb.renderZNeg = this.qb.renderZPos = false;
			this.qb.putCubeQuads(builder, fEN, fSN, fMin, fEP, fW, fEN);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWN, fSP, fMin, fWP, fW, fWN);
			this.qb.renderOppisite = false;
			this.qb.setBound(fEN, fSN, fMin, fWN, fW, fEN);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fWP, fSN, fMin, fEP, fW, fWN);
			builder.add(this.qb.bakeYPosFace());
			break;
		case 0x00:
			this.qb.setBound(fWN, fSN, fEN, fEP, fW, fWN);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fEN, fSN, fEN, fEP, fW, fWN);
			builder.add(this.qb.bakeZNegFace());
			this.qb.setBound(fWN, fSP, fEN, fWP, fW, fWN);
			builder.add(this.qb.bakeZPosFace());
			break;
		}
		this.qb.resetOption();
		switch (connectState & 0xC0)// X pos
		{
		case 0x80:
			transformer.setTransform(1.0, 0.0, 0.0);
			this.qb.renderYPos = this.qb.renderXNeg = this.qb.renderXPos = false;
			this.qb.putCubeQuads(builder, fMin, fSN, fEN, fWN, fW, fEP);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fMin, fSP, fWN, fWN, fW, fWP);
			this.qb.renderOppisite = false;
			this.qb.renderYPos = this.qb.renderXPos = true;
			this.qb.renderZPos = this.qb.renderZNeg = this.qb.renderYNeg = false;
			this.qb.putCubeQuads(builder, fMin, fSN, fEN, fWN, fW, fWN);
			this.qb.putCubeQuads(builder, fMin, fSN, fWP, fWN, fW, fEP);
			this.qb.renderYNeg = true;
			this.qb.setBound(fMin, fSN, fWN, fWN, fSP, fWP);
			builder.add(this.qb.bakeXPosFace());
			this.qb.resetOption();
		case 0x40:
			this.qb.renderYPos = this.qb.renderXNeg = this.qb.renderXPos = false;
			this.qb.putCubeQuads(builder, fEP, fSN, fEN, fMax, fW, fEP);
			this.qb.renderOppisite = true;
			this.qb.putCubeQuads(builder, fWP, fSP, fWN, fMax, fW, fWP);
			this.qb.renderOppisite = false;
			this.qb.setBound(fEP, fSN, fEN, fMax, fW, fWN);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fWP, fSN, fWP, fMax, fW, fEP);
			builder.add(this.qb.bakeYPosFace());
			break;
		case 0x00:
			this.qb.setBound(fWP, fSN, fWN, fEP, fW, fEP);
			builder.add(this.qb.bakeYPosFace());
			this.qb.setBound(fWP, fSN, fEN, fEP, fW, fEP);
			builder.add(this.qb.bakeXPosFace());
			this.qb.setBound(fWP, fSP, fWN, fEP, fW, fWP);
			builder.add(this.qb.bakeXNegFace());
			break;
		}
		return builder.build();
	}
}
