/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.biome.layer.surface;

import static nebula.common.data.Misc.AIR;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MP;
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
public class BLGSurfaceOcean extends BLGSurfaceBase
{
	protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	protected static final IBlockState SAND = M.sand.getProperty(MP.property_sand).block.getDefaultState();
	
	private static NoiseBase noise = new NoisePerlin(2479713561071595L, 2, 4.0, 2.0, 2.0);
	
	@Override
	protected void generateTopTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, int x1, int z1, BiomeBase biome,
			int height, float temperature, float rainfall, IBlockState[] cover, IBlockState[] rock)
	{
		int y = worldHeight;
		int c = -1, f = 0;
		
		for (; y >= arrayYHeight; y --)
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
				if (f == 0)
				{
					if (temperature < 0.0F)//Ice point
					{
						primer.setBlockState(x, y, z, EnumBlock.ice.block.getDefaultState());
					}
					f = 1;
				}
				else
				{
					f ++;
				}
			}
			else if (c == -1)
			{
				if (f <= 0)
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
					state = FarSurfaceDataGenerator.getWithWaterState(state);
					c = height;
				}
				primer.setBlockState(x, y, z, state);
			}
			else if (c > 0)
			{
				if (c == 1)
				{
					state = GRAVEL;
				}
				else if (cover.length > 3 && c < height / 2)
				{
					state = (noise.noise(x1, y, z1) * 3 - c) > 0 ? GRAVEL : SAND;
				}
				else
				{
					state = SAND;
				}
				--c;
				primer.setBlockState(x, y, z, state);
			}
			else setRockType(height, primer, x, y, z, rock);
		}
	}
}