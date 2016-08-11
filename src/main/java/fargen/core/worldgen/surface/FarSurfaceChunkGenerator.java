package fargen.core.worldgen.surface;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.data.EnumTerrain;
import farcore.lib.util.NoiseBase;
import farcore.lib.util.NoisePerlin;
import farcore.util.U.L;
import fargen.core.biome.BiomeBase;
import fargen.core.util.LayerProp;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fluids.BlockFluidBase;

public class FarSurfaceChunkGenerator implements IChunkGenerator
{
	protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
	protected static final IBlockState OCEAN = EnumBlock.water.block.getDefaultState().withProperty(BlockFluidBase.LEVEL, 15);
	private static final int smoothOffset = 4;
	private static final int smoothSize = 9;
	private static final int smoothListLength = 14;
	private static final float[] parabolicField = new float[smoothSize * smoothSize];

	static
	{
		for (int j = -smoothOffset; j <= smoothOffset; ++j)
		{
			for (int k = -smoothOffset; k <= smoothOffset; ++k)
			{
				float f = 10.0F / MathHelper.sqrt_float(j * j + k * k + 1F);
				parabolicField[j + smoothOffset + (k + smoothOffset) * smoothSize] = f;
			}
		}
	}
	protected World world;
	protected WorldType type;
	protected Random random;
	/** The base height noise. */
	protected NoiseBase noise1;
	/** The max random height noise. */
	protected NoiseBase noise2;
	/** The minimum random height noise. */
	protected NoiseBase noise3;
	/** The random random height noise. */
	protected NoiseBase noise4;
	/** The surface height noise. */
	protected NoiseBase noise5;
	/** Stone height noise. */
	protected NoiseBase noise6;
	private int[] biomes;
	
	protected double[] cache1;
	protected double[] cache2;
	protected double[] cache3;
	protected double[] cache4;
	protected double[] cache5;
	/** Last height cache. */
	protected double[] heightMap;
	protected double[] cache6;
	private LayerProp layers;
	
	public FarSurfaceChunkGenerator(World world, long seed, boolean enableFeatures)
	{
		this.world = world;
		type = world.getWorldInfo().getTerrainType();
		random = new Random(seed);
		noise1 = new NoisePerlin(random, 16, 1D, 2D, 2D);
		noise2 = new NoisePerlin(random, 16, 1D, 2D, 2D);
		noise3 = new NoisePerlin(random, 16, 1D, 2D, 2D);
		noise4 = new NoisePerlin(random, 8, 1D, 2D, 2D);
		noise5 = new NoisePerlin(random, 4, 1D, 2D, 2D);
		noise6 = new NoisePerlin(random, 4, 12D, 2D, 2D);
		heightMap = new double[825];
		layers = ((FarSurfaceBiomeProvider) world.getBiomeProvider()).layers;
	}

	protected void generateTerrainHeight(int x, int z)
	{
		cache1 = noise1.noise(cache1, 5, 5, (double)x, (double)z, 3000D, 3000D);
		//		cache2 = noise2.noise(cache2, 5, 5, 33, x, z, 0, 1.7, 1.7, 1.0);
		cache2 = layers.terrainHeight(cache2, x, z, 5, 5);
		cache3 = noise3.noise(cache3, 5, 5, 33, x, z, 0, 1.7, 1.7, 1.0);
		cache4 = noise4.noise(cache4, 5, 5, 33, x, z, 0, 1.7, 1.7, 1.0);
		cache5 = noise5.noise(cache5, 5, 5, 33, x, z, 0, 1.7, 1.7, 1.0);
		int[] terrains = layers.terrain(x - smoothOffset, z - smoothOffset, smoothListLength, smoothListLength);
		int count1 = 0;
		for(int i = 0; i < 5; ++i)
		{
			for(int j = 0; j < 5; ++j)
			{
				float baseHeight = 0;
				float randHeight = 0;
				float divide = 0;
				EnumTerrain terrain = EnumTerrain.values()[terrains[(i + smoothOffset) * smoothListLength + (j + smoothOffset)]];
				for(int i1 = -smoothOffset; i1 <= smoothOffset; ++i1)
				{
					for(int j1 = -smoothOffset; j1 <= smoothOffset; ++j1)
					{
						EnumTerrain terrain2 = EnumTerrain.values()[terrains[(i + i1 + smoothOffset) * smoothListLength + (j + j1 + smoothOffset)]];
						float baseHeight2 = terrain2.root;
						float randHeight2 = terrain2.rand;
						float effect = parabolicField[(i1 + smoothOffset) * smoothSize + (j1 + smoothOffset)];
						if(terrain2.root > terrain.root)
						{
							effect *= terrain2.highWeight;
						}
						else if(terrain2.root < terrain.root)
						{
							effect *= terrain2.lowWeight;
						}
						baseHeight += baseHeight2 * effect;
						randHeight += randHeight2 * effect;
						divide += effect;
					}
				}
				baseHeight /= divide;
				randHeight /= divide;
				double multiply = cache1[count1] * 2D - 1D;
				double base = Math.abs(cache2[count1] * 2 - 1);
				double baseFix = cache3[count1] * 0.2F + 0.9F;
				double surfaceRand = cache5[count1] * 2D - 1D;
				double fixed = (1D + base) * baseFix * 0.666;
				fixed = L.range(0.6, 1.4, fixed);
				surfaceRand /= 32D;
				heightMap[count1] = fixed * baseHeight + randHeight * multiply + surfaceRand;
				count1++;
			}
		}
	}
	
	public void setBlocksInChunk(int x, int z, ChunkPrimer primer)
	{
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				double d1 = heightMap[i * 5 + j];
				double d2 = heightMap[(i + 1) * 5 + j];
				double d3 = heightMap[i * 5 + j + 1];
				double d4 = heightMap[(i + 1) * 5 + j + 1];

				double d5 = (d2 - d1) / 4D;
				double d6 = (d4 - d3) / 4D;
				for(int i1 = 0; i1 < 4; ++i1)
				{
					double d7 = d1;
					double d8 = (d3 - d1) / 4D;
					for(int j1 = 0; j1 < 4; ++j1)
					{
						int height = 128 + (int) (d7 * 32);
						int k = 0;
						for(; k <= height; ++k)
						{
							primer.setBlockState(j * 4 + j1, k, i * 4 + i1, STONE);
						}
						for(; k < 128; ++k)
						{
							primer.setBlockState(j * 4 + j1, k, i * 4 + i1, OCEAN);
						}
						d7 += d8;
					}
					d1 += d5;
					d3 += d6;
				}
			}
		}
	}
	
	protected void replaceByBiomes(int x, int z, ChunkPrimer primer)
	{
		cache6 = noise6.noise(cache6, 16, 16, x, z);
		for(int i = 0; i < 16; ++i)
		{
			for(int j = 0; j < 16; ++j)
			{
				int biomeID = biomes[j * 16 + i];
				BiomeBase biome = BiomeBase.getBiomeFromID(biomeID);
				biome.genTerrainBlocks(world, random, primer, x + i, z + j, cache6[j * 16 + i], biomeID >> 8);
			}
		}
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
		random.setSeed(x * 341873128712L + z * 132897987541L);
		generateTerrainHeight(x << 2, z << 2);
		ChunkPrimer primer = new ChunkPrimer();
		setBlocksInChunk(x, z, primer);
		biomes = ((FarSurfaceBiomeProvider) world.getBiomeProvider()).loadBlockGeneratorData(biomes, x * 16, z * 16, 16, 16);
		replaceByBiomes(x << 4, z << 4, primer);
		Chunk chunk = new Chunk(world, primer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i)
		{
			abyte[i] = (byte) (biomes[i] & 0xFF);
		}

		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public void populate(int x, int z)
	{
	}
	
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}
	
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return ImmutableList.of();
	}
	
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
	{
		return null;
	}
	
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}
}