/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import farcore.lib.model.block.statemap.BlockStateTileEntityWapper;
import farcore.lib.util.Direction;
import fle.api.client.CompactModel;
import fle.api.tile.IDitchTile;
import fle.core.tile.ditchs.TEDitch;
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
		//		qb.putCubeQuads(builder, f3, f9, f3, f4, f7, f4);
		this.qb.renderXNeg = this.qb.renderXPos = this.qb.renderZNeg = this.qb.renderZPos = true;
		this.qb.putCubeQuads(builder, f5, f9, f5, f3, f8, f3);
		this.qb.putCubeQuads(builder, f5, f9, f4, f3, f8, f6);
		this.qb.putCubeQuads(builder, f4, f9, f4, f6, f8, f6);
		this.qb.putCubeQuads(builder, f4, f9, f5, f6, f8, f3);
		switch (connectState & 0x03)//Z pos
		{
		case 0x02:
			this.qb.putCubeQuads(builder, f5, f9, f6, f3, f8, f11);
			this.qb.putCubeQuads(builder, f4, f9, f6, f6, f8, f11);
			this.qb.putCubeQuads(builder, f3, f9, f4, f4, f7, f11);
			break;
		case 0x01:
			this.qb.putCubeQuads(builder, f5, f9, f6, f3, f8, f2);
			this.qb.putCubeQuads(builder, f4, f9, f6, f6, f8, f2);
			this.qb.putCubeQuads(builder, f3, f9, f4, f4, f7, f2);
			break;
		case 0x00 :
			this.qb.putCubeQuads(builder, f3, f9, f4, f4, f8, f6);
			break;
		}
		switch (connectState & 0x0C)//X neg
		{
		case 0x08:
			this.qb.putCubeQuads(builder, f10, f9, f5, f5, f8, f3);
			this.qb.putCubeQuads(builder, f10, f9, f4, f5, f8, f6);
			this.qb.putCubeQuads(builder, f10, f9, f3, f3, f7, f4);
			break;
		case 0x04:
			this.qb.putCubeQuads(builder, f1, f9, f5, f5, f8, f3);
			this.qb.putCubeQuads(builder, f1, f9, f4, f5, f8, f6);
			this.qb.putCubeQuads(builder, f1, f9, f3, f3, f7, f4);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, f5, f9, f3, f3, f8, f4);
			break;
		}
		switch (connectState & 0x30)//Z neg
		{
		case 0x20:
			this.qb.putCubeQuads(builder, f5, f9, f10, f3, f8, f5);
			this.qb.putCubeQuads(builder, f4, f9, f10, f6, f8, f5);
			this.qb.putCubeQuads(builder, f3, f9, f10, f4, f7, f3);
			break;
		case 0x10:
			this.qb.putCubeQuads(builder, f5, f9, f1, f3, f8, f5);
			this.qb.putCubeQuads(builder, f4, f9, f1, f6, f8, f5);
			this.qb.putCubeQuads(builder, f3, f9, f1, f4, f7, f3);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, f3, f9, f5, f4, f8, f3);
			break;
		}
		switch (connectState & 0xC0)//X pos
		{
		case 0x80:
			this.qb.putCubeQuads(builder, f6, f9, f5, f11, f8, f3);
			this.qb.putCubeQuads(builder, f6, f9, f4, f11, f8, f6);
			this.qb.putCubeQuads(builder, f4, f9, f3, f11, f7, f4);
			break;
		case 0x40:
			this.qb.putCubeQuads(builder, f6, f9, f5, f2, f8, f3);
			this.qb.putCubeQuads(builder, f6, f9, f4, f2, f8, f6);
			this.qb.putCubeQuads(builder, f4, f9, f3, f2, f7, f4);
			break;
		case 0x00:
			this.qb.putCubeQuads(builder, f4, f9, f3, f6, f8, f4);
			break;
		}
		return builder.build();
	}
	
	@Override
	public boolean isGui3d()
	{
		return true;
	}
}