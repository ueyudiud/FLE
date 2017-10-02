/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.worldgen.generator;

import com.google.common.base.Predicates;

import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.block.terria.BlockRock;
import farcore.lib.block.terria.BlockSand;
import farcore.lib.block.terria.BlockSoil;
import farcore.lib.world.gen.FarWorldGenerator;
import fargen.core.worldgen.surface.FarSurfaceChunkGenerator;
import nebula.common.data.Misc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author ueyudiud
 */
public class WorldGenStoneChip extends FarWorldGenerator
{
	@Override
	protected boolean generate()
	{
		IBlockState[][] rockData = ((FarSurfaceChunkGenerator) this.chunkGenerator).getRockLayer(this.pos.getX() >> 4, this.pos.getZ() >> 4);
		for (int i = 0; i < 4; ++i)
		{
			if (this.rand.nextInt(5) > i)
			{
				BlockPos pos1 = this.world.getTopSolidOrLiquidBlock(this.pos.add(this.rand.nextInt(16), 0, this.rand.nextInt(16)));
				IBlockState state2 = rockData[(pos1.getZ() & 0x4) << 4 | (pos1.getX() & 0x4)][0];
				if (canGenerateOn(pos1))
				{
					this.world.setBlockState(pos1, ((RockBehavior) ((BlockRock) state2.getBlock()).behavior).stonechip.getDefaultState());
				}
			}
		}
		return true;
	}
	
	protected boolean canGenerateOn(BlockPos pos)
	{
		IBlockState state = this.world.getBlockState(pos.down()), state2 = this.world.getBlockState(pos);
		return state2.getBlock().isReplaceableOreGen(state2, this.world, pos, Predicates.equalTo(Misc.AIR)) &&
				(state.getBlock() instanceof BlockRock || state.getBlock() instanceof BlockSoil ||
						(state.getBlock() instanceof BlockSand && state.isSideSolid(this.world, pos.down(), EnumFacing.UP)));
	}
}