package fargen.core.biome.layer.surface;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BLGSStandard extends BLGSurfaceBase
{
	public static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	public static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
	public static final IBlockState GRASS = Blocks.GRASS.getDefaultState();
	
	@Override
	protected int generateLayer(World world, Random rand, ChunkPrimer primer, int x, int y, int z, int submeta,
			double length, boolean underWater)
	{
		int l = (int) length;
		if(l == 0) return 0;
		int off = 0;
		primer.setBlockState(x, y, z, !underWater ? GRASS : DIRT);
		off++;
		for(; off < l; ++off)
		{
			--y;
			if(primer.getBlockState(x, y, z) == AIR)
				return off;
			else
			{
				if(off != l - 1)
				{
					primer.setBlockState(x, y, z, DIRT);
				}
				else
				{
					primer.setBlockState(x, y, z, GRAVEL);
				}
			}
		}
		return l;
	}
}