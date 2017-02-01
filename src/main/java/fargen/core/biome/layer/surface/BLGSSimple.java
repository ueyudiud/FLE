package fargen.core.biome.layer.surface;

import java.util.Random;

import nebula.common.util.noise.NoisePerlin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BLGSSimple extends BLGSurfaceBase
{
	private static final NoisePerlin CLAY_NOISE = new NoisePerlin(0L, 4, 18L, 1.5F, 1.5F);

	private final boolean generateClay;
	private final IBlockState topBlock;
	private final IBlockState fillerBlock;
	private final IBlockState baseBlock;
	
	public BLGSSimple(IBlockState topBlock, IBlockState fillerBlock, IBlockState baseBlock, boolean generateClay)
	{
		this.topBlock = topBlock;
		this.fillerBlock = fillerBlock;
		this.baseBlock = baseBlock;
		this.generateClay = generateClay;
	}
	
	@Override
	protected int generateLayer(World world, Random rand, ChunkPrimer primer, int x, int y, int z, int submeta,
			double length, boolean underWater)
	{
		int i = 0;
		int l = (int) length;
		while(i < l)
		{
			if(i == 0 && !underWater)
			{
				primer.setBlockState(x, y, z, topBlock);
			}
			else
			{
				IBlockState state = primer.getBlockState(x, y, z);
				if(state == AIR) return i;
				if(i != l - 1)
				{
					primer.setBlockState(x, y, z, fillerBlock);
				}
				else
				{
					primer.setBlockState(x, y, z, baseBlock);
				}
			}
			++i;
			--y;
		}
		return i;
	}
}