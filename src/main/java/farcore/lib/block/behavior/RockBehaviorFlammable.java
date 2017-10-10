/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.behavior;

import java.util.Random;

import farcore.lib.block.terria.BlockRock;
import farcore.lib.material.Mat;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class RockBehaviorFlammable<B extends BlockRock> extends RockBehavior<B>
{
	public RockBehaviorFlammable(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		super(material, harvestLevel, hardness, explosionResistance);
	}
	
	@Override
	public boolean isFlammable(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	public int getFlammability(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 10;
	}
	
	@Override
	public int getFireEncouragement(B block, IBlockState state, World world, BlockPos pos)
	{
		return 5;
	}
	
	@Override
	public int getFireSpreadSpeed(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 2;
	}
	
	@Override
	public boolean onBurn(B block, IBlockState state, IModifiableCoord coord, float burnHardness,
			Direction direction)
	{
		return super.onBurn(block, state, coord, burnHardness, direction);
	}
	
	@Override
	public boolean onBurningTick(B block, IBlockState state, IModifiableCoord coord, Random rand,
			Direction fireSourceDir, IBlockState fireState)
	{
		return super.onBurningTick(block, state, coord, rand, fireSourceDir, fireState);
	}
}