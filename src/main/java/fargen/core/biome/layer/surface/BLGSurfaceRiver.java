/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.biome.layer.surface;

import static nebula.common.data.Misc.AIR;

import java.util.Random;

import farcore.data.EnumBlock;
import fargen.core.biome.BiomeBase;
import fargen.core.worldgen.surface.FarSurfaceDataGenerator;
import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

/**
 * @author ueyudiud
 */
public class BLGSurfaceRiver extends BLGSurfaceBase
{
	protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	
	private static NoiseBase noise = new NoisePerlin(43185173394951L, 2, 4.0, 2.0, 2.0);
	
	@Override
	protected void generateTopTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, int x1, int z1, BiomeBase biome, int height, float temperature, float rainfall, IBlockState[] cover, IBlockState[] rock)
	{
		int y = worldHeight;
		int c = -1, f = 0;
		int h = Math.round((float) (noise.noise(x1, y * 0.8, z1) * 3));
		
		for (; y >= arrayYHeight; y--)
		{
			IBlockState state = primer.getBlockState(x, y, z);
			if (state == AIR)
			{
				if (y > seaLevel)
				{
					c = -1;
				}
			}
			else if (state == WATER)
			{
				if (f++ == 0)
				{
					if (temperature < 0.0F)// Ice point
					{
						primer.setBlockState(x, y, z, EnumBlock.ice.block.getDefaultState());
					}
				}
			}
			else if (c == -1)
			{
				if (y < seaLevel && rand.nextInt((int) (noise.noise(x1, 4315791353.0, z1) * 252) + 73) == 0)
				{
					primer.setBlockState(x, y + 1, z, FarSurfaceDataGenerator.getMossy(rock[0]));
				}
				if (h >= 0)
				{
					state = GRAVEL;
					c = height;
				}
				else if (f <= 0)
				{
					state = cover[0];
					if (FarSurfaceDataGenerator.isGrass(state))
					{
						state = FarSurfaceDataGenerator.getGrassStateWithTemperatureAndRainfall(state, temperature, rainfall);
					}
					c = height;
				}
				else
				{
					state = cover[1];
					c = height;
				}
				primer.setBlockState(x, y, z, state);
			}
			else if (c > 0)
			{
				if (h >= 0)
				{
					state = GRAVEL;
					--h;
				}
				else if (c == 1)
				{
					state = cover[cover.length > 3 ? 3 : 2];
				}
				else if (cover.length > 3 && c < height / 2)
				{
					state = cover[2];
				}
				else
				{
					state = cover[1];
				}
				--c;
				primer.setBlockState(x, y, z, state);
			}
			else
				setRockType(height, primer, x, y, z, rock);
		}
	}
}
