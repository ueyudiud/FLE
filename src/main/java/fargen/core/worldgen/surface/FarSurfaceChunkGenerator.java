package fargen.core.worldgen.surface;

import static nebula.common.data.Misc.AIR;

import java.util.List;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.biome.BiomeBase;
import nebula.common.util.Maths;
import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

public class FarSurfaceChunkGenerator implements IChunkGenerator
{
	private static final IBlockState ROCK = Blocks.STONE.getDefaultState();
	private static final IBlockState WATER = Blocks.WATER.getDefaultState();
	
	private final World world;
	private final FarSurfaceBiomeProvider biomeProvider;
	
	private final NoiseBase noise1;
	private final NoiseBase noise2;
	private final NoiseBase noise3;
	private final NoiseBase noise4;
	private final NoiseBase noise5;//Stone noise.
	
	private Random random = new Random();
	
	private BiomeBase[] biomes;
	
	private double[] cache1;
	private double[] cache2;
	private double[] cache3;
	private double[] cache4;
	private double[] cache5;
	
	private double[] cachea;
	
	public FarSurfaceChunkGenerator(World world, long seed)
	{
		this.world = world;
		this.biomeProvider = (FarSurfaceBiomeProvider) world.provider.getBiomeProvider();
		Random random = new Random(seed);
		this.noise1 = new NoisePerlin(random, 6, 2.0, 1.6, 1.6);
		this.noise2 = new NoisePerlin(random, 14, 4.0, 2.0, 2.0);
		this.noise3 = new NoisePerlin(random, 8, 3.0, 1.8, 1.8);
		this.noise4 = new NoisePerlin(random, 4, 1.0, 1.5, 1.8);
		this.noise5 = new NoisePerlin(random, 3, 2.5, 1.7, 1.9);
	}
	
	private static final byte
	size = 2, scale = size * 2 + 1;
	
	private static final float[] parabolicField;
	
	private static final DoubleUnaryOperator randomNoiseOperator = x -> {
		x = x * 1.5 - 0.5;
		if (x < 0)
		{
			x *= -0.3F;
		}
		x = x * 3.0 - 2.0;
		if (x > 0)
		{
			if (x > 1.0)
			{
				x = 1.0;
			}
			x *= 0.0625;
		}
		else
		{
			x *= 0.1;
		}
		return x;
	};
	
	private double[] initializeNoiseFieldHigh(double[] outArray, int xPos, int yPos, int zPos, int xSize, int ySize, int zSize)
	{
		if (outArray == null)
		{
			outArray = new double[xSize * ySize * zSize];
		}
		
		this.cache4 = this.noise4.noise(this.cache4, xSize, zSize, (double) xPos, (double) zPos, 1.0, 1.0);
		this.cache3 = this.noise3.noise(this.cache3, ySize, xSize, zSize, yPos, xPos, zPos, 0.4, 1.0, 1.0);
		
		this.cache1 = this.noise1.noise(this.cache1, ySize, xSize, zSize, yPos, xPos, zPos, 0.3, 1.0, 1.0);
		this.cache2 = this.noise2.noise(this.cache2, ySize, xSize, zSize, yPos, xPos, zPos, 0.3, 1.0, 1.0);
		
		int i1 = 0;
		int i2 = 0;
		for (int z = 0; z < zSize; z++)
		{
			for (int x = 0; x < xSize; x++)
			{
				float variation = 0.0F;
				float root = 0.0F;
				float total = 0.0F;
				BiomeBase baseBiome = this.biomes[(x + size + (z + size) * (xSize + scale))];
				for (int xR = -size; xR <= size; xR++)
				{
					for (int zR = -size; zR <= size; zR++)
					{
						BiomeBase blendBiome = this.biomes[(x + xR + size + (z + zR + size) * (xSize + scale))];
						float blendedHeight = parabolicField[(xR + size + (zR + size) * scale)];
						if (blendBiome.getBaseHeight() > baseBiome.getBaseHeight())
						{
							blendedHeight *= .5F;
						}
						variation += blendBiome.getHeightVariation() * blendedHeight;
						root += blendBiome.getBaseHeight() * blendedHeight;
						total += blendedHeight;
					}
				}
				variation /= total;
				root /= total;
				
				double scale = randomNoiseOperator.applyAsDouble(this.cache3[i2]);
				double d1 = root + scale * .2;
				d1 = 0.2 + d1 * ySize / 4.0F;
				double d2 = 3 + variation * scale;
				for (int y = 0; y < ySize; y++)
				{
					double output = Maths.lerp(this.cache1[i1], this.cache2[i1], this.cache3[i1]);
					double off = (d1 - y) / d2;
					if (off > 0.0)
					{
						off *= 4.0;
					}
					output += off;
					if (y > ySize - 4)
					{
						double var40 = (y - (ySize - 4)) / 3.0F;
						output = output * (1.0D - var40) + -10.0D * var40;
					}
					outArray[i1] = output;
					i1++;
				}
				i2++;
			}
		}
		return outArray;
	}
	
	private static final
	byte subDivXZ = 4, subDivY = 16, xzSize = subDivXZ + 1, ySize = 17;
	private static final
	short seaLevel = 144, arrayYHeight = 128;
	private static final
	double yLerp = 0.125D;
	
	private void generateChunkData(ChunkPrimer primer, int chunkX, int chunkZ)
	{
		this.biomes = this.biomeProvider.getBiomesForGeneration(this.biomes, chunkX - size, chunkZ - size, 5 + scale, 5 + scale);
		this.cachea = initializeNoiseFieldHigh(this.cachea, chunkX, 0, chunkZ, 5, 17, 5);
		for (int z = 0; z < subDivXZ; z++)
		{
			for (int x = 0; x < subDivXZ; x++)
			{
				for (int y = 0; y < subDivY; y++)
				{
					double noiseDL  = this.cachea[(((z + 0) * xzSize + x + 0) * ySize + y + 0)];
					double noiseUL  = this.cachea[(((z + 1) * xzSize + x + 0) * ySize + y + 0)];
					double noiseDR  = this.cachea[(((z + 0) * xzSize + x + 1) * ySize + y + 0)];
					double noiseUR  = this.cachea[(((z + 1) * xzSize + x + 1) * ySize + y + 0)];
					double noiseDLA = (this.cachea[(((z + 0) * xzSize + x + 0) * ySize + y + 1)] - noiseDL) * yLerp;
					double noiseULA = (this.cachea[(((z + 1) * xzSize + x + 0) * ySize + y + 1)] - noiseUL) * yLerp;
					double noiseDRA = (this.cachea[(((z + 0) * xzSize + x + 1) * ySize + y + 1)] - noiseDR) * yLerp;
					double noiseURA = (this.cachea[(((z + 1) * xzSize + x + 1) * ySize + y + 1)] - noiseUR) * yLerp;
					for (int var31 = 0; var31 < 8; var31++)
					{
						int Y = var31 | (y << 3) | 128;
						double xLerp = 0.25D;
						double var34 = noiseDL;
						double var36 = noiseUL;
						double var38 = (noiseDR - noiseDL) * xLerp;
						double var40 = (noiseUR - noiseUL) * xLerp;
						for (int var42 = 0; var42 < 4; var42++)
						{
							int X = var42 | (x << 2);
							
							double zLerp = 0.25D;
							double var49 = (var36 - var34) * zLerp;
							double var47 = var34 - var49;
							for (int var51 = 0; var51 < 4; var51++)
							{
								int Z = var51 | (z << 2);
								
								if ((var47 += var49) > 0.0D)
								{
									primer.setBlockState(X, Y, Z, ROCK);
								}
								else if (Y < seaLevel)
								{
									primer.setBlockState(X, Y, Z, WATER);
								}
							}
							
							var34 += var38;
							var36 += var40;
						}
						
						noiseDL += noiseDLA;
						noiseUL += noiseULA;
						noiseDR += noiseDRA;
						noiseUR += noiseURA;
					}
				}
			}
		}
	}
	
	private static final
	short worldHeight = 256, rockLayer1 = 120, rockLayer2 = 60;
	
	private void replaceChunkBlock(ChunkPrimer primer, int chunkX, int chunkZ, Random random)
	{
		IWorldPropProvider propProvider = WorldPropHandler.getWorldProperty(this.world);
		int x1 = chunkX << 4, z1 = chunkZ << 4;
		this.cache5 = this.noise5.noise(this.cache5, 16, 16, (double) x1, (double) z1, 0.125, 0.125);
		
		IBlockState[][] rockLayer = this.biomeProvider.dataGenerator.getRockLayer(chunkX, chunkZ);
		IBlockState[][] coverLayer = this.biomeProvider.dataGenerator.getCoverLayer(chunkX, chunkZ);
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = 0; x < 16; ++x)
			for (int z = 0; z < 16; ++z)
			{
				int index = z << 4 | x;
				BiomeBase biome = this.biomes[index];
				final int height = 5 + MathHelper.ceil(12.0 * this.cache5[index]);
				boolean hasWind = false;
				float temp = propProvider.getAverageTemperature(this.world, pos.setPos(x1 | x, 0, z1 | z));
				float rainfall = propProvider.getAverageHumidity(this.world, pos);
				int c = -1, f = 0;
				int y = 255;
				for (; y >= 0; y --)
				{
					if (y == 0)
					{
						primer.setBlockState(x, y, z, AIR);
						break;
					}
					IBlockState state = primer.getBlockState(x, y, z);
					if (state == AIR)
					{
						if (y <= seaLevel)
						{
						}
						else
						{
							c = -1;
						}
					}
					else if (state == WATER)
					{
						if (biome.isOcean())
						{
						}
						else
						{
							if (f == 0)
							{
								if (temp < 0.0F)//Ice point
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
							state = coverLayer[index][0];
							if (FarSurfaceDataGenerator.isGrass(state))
							{
								state = FarSurfaceDataGenerator.getGrassStateWithTemperatureAndRainfall(state, temp, rainfall);
							}
							c = height;
						}
						else
						{
							state = coverLayer[index][1];
							state = FarSurfaceDataGenerator.getWithWaterState(state);
							c = height;
						}
						primer.setBlockState(x, y, z, state);
					}
					else if (c > 0)
					{
						if (c == 1)
						{
							state = coverLayer[index][coverLayer[index].length > 3 ? 3 : 2];
						}
						else if (coverLayer[index].length > 3 && c < height / 2)
						{
							state = coverLayer[index][2];
						}
						else
						{
							state = coverLayer[index][1];
						}
						--c;
						primer.setBlockState(x, y, z, state);
					}
					else
					{
						if (rockLayer[index].length == 1)
						{
							primer.setBlockState(x, y, z, rockLayer[index][0]);
						}
						else if (y >= rockLayer1 + height * 2 / 3)
						{
							primer.setBlockState(x, y, z, rockLayer[index][0]);
						}
						else if (y >= rockLayer2 + height / 3)
						{
							primer.setBlockState(x, y, z, rockLayer[index][1]);
						}
						else
						{
							primer.setBlockState(x, y, z, rockLayer[index][2]);
						}
					}
				}
			}
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		this.random.setSeed(x * 341873128712L ^ z * 132897987541L);
		
		ChunkPrimer primer = new ChunkPrimer();
		generateChunkData(primer, x << 2, z << 2);
		
		this.biomes = this.biomeProvider.getBiomes(this.biomes, x, z, 16, 16, true);
		replaceChunkBlock(primer, x << 4, z << 4, this.random);
		Chunk chunk = new Chunk(this.world, primer, x, z);
		chunk.generateSkylightMap();
		byte[] array = chunk.getBiomeArray();
		for (int i = 0; i < 256; ++i)
		{
			array[i] = (byte) this.biomes[i].biomeID;
		}
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
		generateStructures(chunkIn, x, z);
	}
	
	static
	{
		parabolicField = new float[scale * scale];
		for (int x = -size; x <= size; x++)
		{
			for (int y = -size; y <= size; y++)
			{
				float parabolaHeight = 10.0F / MathHelper.sqrt(x * x + y * y + 0.2F);
				parabolicField[(x + 2 + (y + 2) * scale)] = parabolaHeight;
			}
		}
	}
}