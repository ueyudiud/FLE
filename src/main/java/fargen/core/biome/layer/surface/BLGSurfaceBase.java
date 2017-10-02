/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.biome.layer.surface;

import static fargen.core.worldgen.surface.FarSurfaceChunkGenerator.RAND_HEIGHT;
import static fargen.core.worldgen.surface.FarSurfaceChunkGenerator.ROCKS;
import static fargen.core.worldgen.surface.FarSurfaceChunkGenerator.SOILS;
import static nebula.common.data.Misc.AIR;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.lib.world.IWorldPropProvider;
import fargen.core.biome.BiomeBase;
import fargen.core.biome.layer.BiomeLayerGenerator;
import fargen.core.worldgen.surface.FarSurfaceDataGenerator;
import nebula.base.IPropertyMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class BLGSurfaceBase implements BiomeLayerGenerator
{
	protected static final IBlockState
	BEDROCK = Blocks.BEDROCK.getDefaultState(),
	LAVA = Blocks.LAVA.getDefaultState(),
	ICE = EnumBlock.ice.block.getDefaultState(),
	ROCK = Blocks.STONE.getDefaultState(),
	WATER = Blocks.WATER.getDefaultState();
	
	protected static final
	short worldHeight = 255, rockLayer1 = 120, rockLayer2 = 60, seaLevel = 144, arrayYHeight = 128;
	
	private BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, BiomeBase biome, IWorldPropProvider provider, IPropertyMap map)
	{
		this.pos.setPos(x, 0, z);
		
		int height = 5 + MathHelper.ceil(12.0 * map.get(RAND_HEIGHT));
		float temp = provider.getAverageTemperature(world, this.pos);
		float rainfall = provider.getAverageHumidity(world, this.pos);
		IBlockState[] coverLayer = map.get(SOILS);
		IBlockState[] rockLayer = map.get(ROCKS);
		
		generateTopTerrain(world, rand, primer, x & 0xF, z & 0xF, x, z, biome, height, temp, rainfall, coverLayer, rockLayer);
		generateBottomTerrain(world, rand, primer, x & 0xF, z & 0xF, x, z, biome, height, rockLayer);
	}
	
	protected void generateTopTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, int x1, int z1, BiomeBase biome, int height, float temperature, float rainfall, IBlockState[] cover, IBlockState[] rock)
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
				if (!biome.isOcean())
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
			else setRockType(height, primer, x, y, z, rock);
		}
	}
	
	protected void generateBottomTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, int x1, int z1, BiomeBase biome, int height, IBlockState[] rock)
	{
		int y = arrayYHeight;
		for (; y >= 0; y --)
		{
			if (y == 0)
			{
				primer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
				break;
			}
			else setRockType(height, primer, x, y, z, rock);
		}
	}
	
	protected void setRockType(int height, ChunkPrimer primer, int x, int y, int z, IBlockState[] rock)
	{
		if (rock.length == 1)
		{
			primer.setBlockState(x, y, z, rock[0]);
		}
		else if (y >= rockLayer1 + height * 2 / 3)
		{
			primer.setBlockState(x, y, z, rock[0]);
		}
		else if (y >= rockLayer2 + height / 3)
		{
			primer.setBlockState(x, y, z, rock[1]);
		}
		else
		{
			primer.setBlockState(x, y, z, rock[2]);
		}
	}
}