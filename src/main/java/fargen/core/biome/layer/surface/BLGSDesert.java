package fargen.core.biome.layer.surface;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BLGSDesert extends BLGSurfaceBase
{
	public static final IBlockState DUNEFUL_SAND = Blocks.SAND.getDefaultState();
	public static final IBlockState YELLOW_SAND = Blocks.SAND.getDefaultState();
	public static final IBlockState SAND_GRAVEL = Blocks.SAND.getDefaultState();
	public static final IBlockState FIRM_SAND_GRAVEL = Blocks.SAND.getDefaultState();
	public static final IBlockState FIRM_SANDSTONE = Blocks.SANDSTONE.getDefaultState();
	public static final IBlockState FIRM_SANDSTONE_DIFERROUS = Blocks.SANDSTONE.getDefaultState();

	@Override
	protected int generateLayer(World world, Random rand, ChunkPrimer primer, int x, int y, int z,
			int submeta, double length, boolean underWater)
	{
		int off = 0;
		float l = (float) length;
		int k1 = submeta;
		IBlockState base = (submeta & 0x4) == 0 ? FIRM_SANDSTONE : FIRM_SANDSTONE_DIFERROUS;
		for(; off < l * 2 + 18 || primer.getBlockState(x, y, z) == AIR; ++off)
		{
			if(off == 0)
			{
				if((submeta & 0x3) == 0 && l >= 0)
				{
					primer.setBlockState(x, y, z, DUNEFUL_SAND);
				}
				else if((submeta & 0x3) == 1)
				{
					primer.setBlockState(x, y, z, SAND_GRAVEL);
				}
				else if((submeta & 0x3) == 2)
				{
					primer.setBlockState(x, y, z, base);
				}
			}
			else if((submeta & 0x3) == 0 && off < l + 4)
			{
				primer.setBlockState(x, y, z, YELLOW_SAND);
			}
			else if((submeta & 0x3) < 2 && off < l * 2 + 5)
			{
				primer.setBlockState(x, y, z, SAND_GRAVEL);
			}
			else if(off < l * 3 + 8)
			{
				primer.setBlockState(x, y, z, FIRM_SAND_GRAVEL);
			}
			else
			{
				primer.setBlockState(x, y, z, base);
			}
			--y;
		}
		return off;
	}
}