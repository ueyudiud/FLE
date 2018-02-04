/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.blocks.building;

import farcore.data.M;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.block.state.PropertyMaterial;
import farcore.lib.material.Mat;
import fle.core.FLE;
import nebula.base.Judgable;
import nebula.common.block.BlockBase;
import nebula.common.data.Misc;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BlockWindow1 extends BlockBase
{
	public static final Judgable<ISubTagContainer>	MATERIAL_FILTER	= Judgable.or(SubTags.WOOD, SubTags.ROCK);
	public static final PropertyMaterial			FRAME			= PropertyMaterial.create("frame", MATERIAL_FILTER);
	public static final IProperty<EnumFacing>		FACING			= Misc.PROP_FACING_HORIZONTALS;
	public static final IProperty<Boolean>			OPEN			= Properties.create("open");
	
	public BlockWindow1(Material materialIn)
	{
		super(FLE.MODID, "window", Materials.GLASS);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FRAME, OPEN);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(FRAME, M.oak).withProperty(OPEN, false);
	}
	
	@Override
	public Material getMaterial(IBlockState state)
	{
		Mat material = state.getValue(FRAME);
		return material.contain(SubTags.WOOD) ? Materials.WOOD : material.contain(SubTags.ROCK) ? Materials.ROCK : Materials.GLASS;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(FRAME).contain(SubTags.WOOD);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(world, pos, face) ? 12 : 0;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return state.getValue(OPEN) ? NULL_AABB : FULL_BLOCK_AABB;
	}
}
