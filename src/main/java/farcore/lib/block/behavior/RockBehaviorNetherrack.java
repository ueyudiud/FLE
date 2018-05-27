/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.block.behavior;

import farcore.blocks.terria.BlockRock;
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
public class RockBehaviorNetherrack extends RockBehavior<BlockRock>
{
	public RockBehaviorNetherrack(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		super(material, harvestLevel, hardness, explosionResistance);
	}
	
	@Override
	public boolean isFireSource(BlockRock block, IBlockState state, World world, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	
	@Override
	public boolean isFlammable(BlockRock block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return false;
	}
	
	@Override
	public boolean onBurn(BlockRock block, IBlockState state, IModifiableCoord coord, float burnHardness, Direction direction)
	{
		return false;
	}
}
