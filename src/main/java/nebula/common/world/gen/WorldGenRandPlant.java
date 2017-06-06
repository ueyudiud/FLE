/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.world.gen;

import java.util.Random;

import nebula.base.function.Selector;
import nebula.common.block.IBlockStayabilityCheck;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * @author ueyudiud
 */
public class WorldGenRandPlant extends WorldGenerator
{
	protected int minHeight;
	protected Selector<IBlockState> selector;
	
	public WorldGenRandPlant(int minHeight, Selector<IBlockState> selector)
	{
		this.minHeight = minHeight;
		this.selector = selector;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (IBlockState state = worldIn.getBlockState(position);
				(state.getBlock().isAir(state, worldIn, position) ||
						state.getBlock().isLeaves(state, worldIn, position)) && position.getY() >= this.minHeight;
				state = worldIn.getBlockState(position))
		{
			position = position.down();
		}
		
		if (position.getY() < this.minHeight) return false;
		
		for (int i = 0; i < 128; ++i)
		{
			BlockPos pos1 = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			IBlockState state = this.selector.next(rand);
			
			if (worldIn.isAirBlock(pos1) &&
					(state.getBlock() instanceof IBlockStayabilityCheck ?
							((IBlockStayabilityCheck) state.getBlock()).canBlockStayAt(worldIn, pos1, state) :
								state.getBlock().canPlaceBlockAt(worldIn, pos1)))
			{
				worldIn.setBlockState(pos1, state, 2);
			}
		}
		
		return true;
	}
}