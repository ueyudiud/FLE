/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import fle.api.client.CompactModel;
import fle.core.tile.tanks.TESmallRockyTank;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.common.tile.TEBase;
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
public class ModelSmallRockyTank extends CompactModel
{
	{
		this.qb.setApplyDiffuseLighting(true);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (state instanceof BlockStateTileEntityWapper)
		{
			TEBase tank = ((BlockStateTileEntityWapper<? extends TEBase>) state).tile;
			if (tank instanceof TESmallRockyTank) return genSmallRockyTankQuads(((TESmallRockyTank) tank).getMaterial(), side);
		}
		return ImmutableList.of();
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
	{
		switch (stack.getItemDamage())
		{
		case 0:
			return genSmallRockyTankQuads(Mat.getMaterialFromStack(stack, "material", M.stone), facing);
		default:
			return ImmutableList.of();
		}
	}
	
	private static final float a = 0.25F, b = 0.75F;
	
	private List<BakedQuad> genSmallRockyTankQuads(Mat material, EnumFacing side)
	{
		TextureAtlasSprite icon1 = getIcon(material, EnumRockType.brick_compacted), icon2 = getIcon(material, EnumRockType.smoothed);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		switch (Direction.of(side))
		{
		case W:
		case E:
		case S:
		case N:
		case D:
			this.qb.setIcon(icon1);
			this.qb.setBound(0, 0, 0, 1, 1, 1);
			switch (side)
			{
			case WEST:
				builder.add(this.qb.bakeXNegFace());
				break;
			case EAST:
				builder.add(this.qb.bakeXPosFace());
				break;
			default:// NORTH
				builder.add(this.qb.bakeZNegFace());
				break;
			case SOUTH:
				builder.add(this.qb.bakeZPosFace());
				break;
			case DOWN:
				builder.add(this.qb.bakeYNegFace());
				break;
			}
			break;
		case U:
			this.qb.setIcon(icon2);
			builder.add(this.qb.bakeFace(EnumFacing.UP, 0.0F, 1.0F, 0.0F, 0, 1, 0, 0, 1, a, 1, 1, a, 1, 1, 0, 0, 0, 1, a));
			builder.add(this.qb.bakeFace(EnumFacing.UP, 0.0F, 1.0F, 0.0F, 0, 1, b, 0, 1, 1, 1, 1, 1, 1, 1, b, 0, b, 1, 1));
			builder.add(this.qb.bakeFace(EnumFacing.UP, 0.0F, 1.0F, 0.0F, 0, 1, a, 0, 1, b, a, 1, b, a, 1, a, 0, a, a, b));
			builder.add(this.qb.bakeFace(EnumFacing.UP, 0.0F, 1.0F, 0.0F, b, 1, a, b, 1, b, 1, 1, b, 1, 1, a, b, a, 1, b));
			break;
		default:
			this.qb.setIcon(icon2);
			this.qb.renderOppisite = true;
			this.qb.renderYPos = false;
			this.qb.putCubeQuads(builder, a, a, a, b, 1, b);
			this.qb.renderOppisite = false;
			this.qb.renderYPos = true;
			break;
		}
		return builder.build();
	}
	
	private TextureAtlasSprite getIcon(Mat material, EnumRockType type)
	{
		return MaterialTextureLoader.getIcon(material, type.condition, type.variant);
	}
}
