package fargen.core.worldgen.surface;

import static nebula.common.data.Misc.AIR;

import java.util.List;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MP;
import farcore.data.V;
import farcore.lib.tree.ITreeGenerator;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.api.event.TreeGenEvent;
import fargen.core.biome.BiomeBase;
import fle.core.tree.TreeGenClassic;
import fle.core.tree.TreeGenJungle;
import nebula.common.base.WeightedRandomSelector;
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
import net.minecraftforge.common.MinecraftForge;

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
	private final NoiseBase noise6;//Tree noise.
	
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
		this.noise4 = new NoisePerlin(random, 6, 1.0, 1.6, 1.8);
		this.noise5 = new NoisePerlin(random, 3, 2.5, 1.7, 1.9);
		this.noise6 = new NoisePerlin(random, 3, 12.0, 2.5, 2.0);
	}
	
	private static final byte
	size = 3, scale = size * 2 + 1;
	
	private static final float[] parabolicField;
	
	private static final DoubleUnaryOperator randomNoiseOperator = x -> {
		x = x * 1.5 - 0.5;
		//[-0.5, 1.0]
		if (x < 0)
		{
			x *= -0.3F;
		}
		//[0.0, 1.0]
		x = x * 3.0 - 2.0;
		//[-2.0, 1.0]
		if (x > 0)
		{
			x *= 2.0;
		}
		else
		{
			x *= 0.5;
		}
		//[-0.2, 0.625]
		return x;
	};
	
	private double[] initializeNoiseFieldHigh(double[] outArray, final int xPos, final int yPos, final int zPos, final int xSize, final int ySize, final int zSize)
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
				//Biome height caculate.
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
				//Rescale
				double scale = randomNoiseOperator.applyAsDouble(this.cache4[i2]);
				double d1 = ySize * (root + 0.03125) / 4F;
				double d2 = 1.8 + 3 * variation * scale;
				if (d2 < 1.0F)
				{
					d2 = 1.0F + (d2 - 1.0F) * 0.3F;
				}
				//Height calculate
				for (int y = 0; y < ySize; y++)
				{
					//Range from [0, 1]
					double output = Maths.lerp(this.cache1[i1], this.cache2[i1], this.cache3[i1]) * 2;
					double off = 1.5 * (d1 - y) / d2;
					if (off > 0.0)
					{
						off *= 4.0;
					}
					output += off;
					if (y > ySize - 4)
					{
						double var40 = (y - (ySize - 4)) / 3.0F;
						output = output * (1.0D - var40) - var40;
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
	double yLerp = 0.125, xzLerp = 0.25;
	
	private void generateChunkData(ChunkPrimer primer, int chunkX, int chunkZ)
	{
		this.biomes = this.biomeProvider.getBiomesForGeneration(this.biomes, chunkX - size, chunkZ - size, 5 + scale, 5 + scale);
		this.cachea = initializeNoiseFieldHigh(this.cachea, chunkX, 0, chunkZ, xzSize, ySize, xzSize);
		for (int z = 0; z < subDivXZ; z++)
		{
			for (int x = 0; x < subDivXZ; x++)
			{
				for (int y = 0; y < subDivY; y++)
				{
					double noiseDL  =  this.cachea[(((z    ) * xzSize + x    ) * ySize + y)];
					double noiseUL  =  this.cachea[(((z + 1) * xzSize + x    ) * ySize + y)];
					double noiseDR  =  this.cachea[(((z    ) * xzSize + x + 1) * ySize + y)];
					double noiseUR  =  this.cachea[(((z + 1) * xzSize + x + 1) * ySize + y)];
					double noiseDLA = (this.cachea[(((z    ) * xzSize + x    ) * ySize + y + 1)] - noiseDL) * yLerp;
					double noiseULA = (this.cachea[(((z + 1) * xzSize + x    ) * ySize + y + 1)] - noiseUL) * yLerp;
					double noiseDRA = (this.cachea[(((z    ) * xzSize + x + 1) * ySize + y + 1)] - noiseDR) * yLerp;
					double noiseURA = (this.cachea[(((z + 1) * xzSize + x + 1) * ySize + y + 1)] - noiseUR) * yLerp;
					for (int y1 = 0; y1 < 8; y1++)
					{
						int Y = y1 | (y << 3) | 128;
						double var34 = noiseDL;
						double var36 = noiseUL;
						double var38 = (noiseDR - noiseDL) * xzLerp;
						double var40 = (noiseUR - noiseUL) * xzLerp;
						for (int x1 = 0; x1 < 4; x1++)
						{
							int X = x1 | (x << 2);
							
							double var49 = (var36 - var34) * xzLerp;
							double var47 = var34 - var49;
							for (int z1 = 0; z1 < 4; z1++)
							{
								int Z = z1 | (z << 2);
								
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
				for (; y >= 128; y --)
				{
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
				for (; y >= 0; y --)
				{
					if (y == 0)
					{
						primer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
						break;
					}
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
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		this.random.setSeed(x * 341873128712L ^ z * 132897987541L);
		
		ChunkPrimer primer = new ChunkPrimer();
		generateChunkData(primer, x << 2, z << 2);
		
		this.biomes = this.biomeProvider.getBiomes(this.biomes, x << 4, z << 4, 16, 16, true);
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
		this.random.setSeed(x * 341873128712L ^ z * 132897987541L);
		
		//Data provide.
		Chunk chunk = this.world.getChunkFromChunkCoords(x, z);
		MutableBlockPos pos = new MutableBlockPos(x << 4, 0, z << 4);
		BiomeBase biome = (BiomeBase) chunk.getBiome(pos.add(16, 0, 16), this.biomeProvider);
		IWorldPropProvider provider = WorldPropHandler.getWorldProperty(this.world);
		float temp = provider.getAverageTemperature(this.world, pos) / 4.0F;
		float rainfall = provider.getAverageHumidity(this.world, pos) / 4.0F;
		
		//MinX & MinZ coordinate.
		final int x1 = x << 4;
		final int z1 = z << 4;
		
		//Tree generation.
		{
			WeightedRandomSelector<ITreeGenerator> treeGenerationSelector = new WeightedRandomSelector<>();
			addVanillaTrees(biome, this.world, x, z, this.noise6, temp, rainfall, treeGenerationSelector);
			MinecraftForge.TERRAIN_GEN_BUS.post(new TreeGenEvent(this.world, x, z, temp, rainfall, treeGenerationSelector));
			
			if (treeGenerationSelector.weight() > 0 && biome.treePerChunkBase >= 0)
			{
				int count = (biome.treePerChunkBase + MathHelper.log2DeBruijn(treeGenerationSelector.weight()) / 4) / biome.treePerChunkDiv;
				for (int i = 0; i < count; ++i)
				{
					int x2 = x1 + this.random.nextInt(16) + 8;
					int z2 = z1 + this.random.nextInt(16) + 8;
					int y2 = this.world.getHeight(x2, z2);
					ITreeGenerator generator = treeGenerationSelector.next(this.random);
					V.generateState = true;
					if (generator.generateTreeAt(this.world, x2, y2, z2, this.random, null))
					{
						count --;
					}
					V.generateState = false;
				}
			}
		}
	}
	
	static final ITreeGenerator
	CEIBA1 = new TreeGenJungle(M.ceiba.getProperty(MP.property_tree), 0.01F),
	OAK1 = new TreeGenClassic(M.oak.getProperty(MP.property_tree), 0.03F),
	OAK2 = new TreeGenClassic(M.oak.getProperty(MP.property_tree), 0.025F),
	BIRCH = new TreeGenClassic(M.birch.getProperty(MP.property_tree), 0.03F);
	
	private static void addVanillaTrees(BiomeBase biome, World world, int x, int z, NoiseBase noise, float temp, float rain, WeightedRandomSelector<ITreeGenerator> selector)
	{
		double d1, d2;
		if (temp > 0.7F && rain > 0.7F)
		{
			d2 = noise.noise(x, 38274.0, z);
			d1 = temp > 0.8F ? 1.0F : (temp - 0.7F) * 10.0F;
			d1 *= rain > 0.8F ? 1.0F : (rain - 0.7F) * 10.0F;
			selector.add(CEIBA1, (int) (d1 * d2 * d2 * 128));
		}
		if (temp > 0.4F && rain > 0.4F && rain < 0.95F)
		{
			d2 = noise.noise(x, 17274.0, z);
			d1 = temp > 0.9F ? (1.0F - temp) * 10.0F : temp > 0.5F ? 1.0F : (temp - 0.4F) * 10.0F;
			d1 *= rain > 0.9F ? (1.0F - rain) * 20.0F : rain > 0.5F ? 1.0F : (rain - 0.5F) * 10.0F;
			selector.add(OAK1, (int) (d1 * d2 * d2 * 256));
		}
		if (temp > 0.5F && rain < 0.45F * temp)
		{
			d2 = noise.noise(x, 15628.0, z);
			d1 = temp > 0.6F ? 1.0F : (temp - 0.5F) * 10.0F;
			d1 *= rain > 0.35F * temp ? (rain - 0.35F * temp) * 10.0F / temp : 1.0F;
			selector.add(OAK2, (int) (d1 * d2 * d2 * 96));
		}
		if (temp > 0.5F && rain < 0.45F * temp)
		{
			d2 = noise.noise(x, 15628.0, z);
			d1 = temp > 0.6F ? 1.0F : (temp - 0.5F) * 10.0F;
			d1 *= rain > 0.35F * temp ? (rain - 0.35F * temp) * 10.0F / temp : 1.0F;
			selector.add(OAK2, (int) (d1 * d2 * d2 * 96));
		}
		if (temp < 0.9F && temp > 0.3F && rain > 0.4F && rain < 0.8F)
		{
			d2 = noise.noise(x, 47247.0, z);
			d1 = temp > 0.9F ? (1.0F - temp) * 10.0F : temp > 0.4F ? 1.0F : (temp - 0.3F) * 10.0F;
			d1 *= rain > 0.7F ? (1.0F - rain) * 10.0F : rain > 0.5F ? 1.0F : (rain - 0.4F) * 10.0F;
			selector.add(BIRCH, (int) (d1 * d2 * d2 * 384));
		}
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
				parabolicField[(x + size + (y + size) * scale)] = parabolaHeight;
			}
		}
		
		((TreeGenJungle) CEIBA1).setHeight(34, 8);
		((TreeGenClassic) OAK1).setHeight(4, 3);
		((TreeGenClassic) OAK2).setHeight(1, 2);
		((TreeGenClassic) BIRCH).setHeight(4, 3);
	}
}