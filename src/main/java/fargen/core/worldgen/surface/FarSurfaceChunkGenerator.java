/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.worldgen.surface;

import static fargen.api.event.FarGenerationEvent.TREE;
import static fargen.api.event.FarGenerationEvent.WILD_CROP;

import java.util.List;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.V;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.CropAccessSimulated;
import farcore.lib.crop.ICrop;
import farcore.lib.tree.ITreeGenerator;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.api.event.FarGenerationEvent;
import fargen.api.event.TreeGenEvent;
import fargen.core.biome.BiomeBase;
import nebula.base.HashPropertyMap;
import nebula.base.IPropertyMap;
import nebula.base.IPropertyMap.IProperty;
import nebula.base.function.WeightedRandomSelector;
import nebula.common.util.L;
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
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

public class FarSurfaceChunkGenerator implements IChunkGenerator
{
	private static final IBlockState ROCK = Blocks.STONE.getDefaultState();
	private static final IBlockState WATER = Blocks.WATER.getDefaultState();
	private static final IBlockState CROP = EnumBlock.crop.block.getDefaultState();
	
	protected void switchState(boolean flag)
	{
		V.generateState = FarCore.worldGenerationFlag = flag;
	}
	
	private final World world;
	private final FarSurfaceBiomeProvider biomeProvider;
	
	private final NoiseBase noise1;
	private final NoiseBase noise2;
	private final NoiseBase noise3;
	private final NoiseBase noise4;
	private final NoiseBase noise5;//Stone noise.
	private final NoiseBase noise6;//Plants noise.
	
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
		this.noise1 = new NoisePerlin(random, 12, 1.0, 2.0, 2.0);
		this.noise2 = new NoisePerlin(random, 12, 1.0, 2.0, 2.0);
		this.noise3 = new NoisePerlin(random, 6, 3.0, 1.8, 1.8);
		this.noise4 = new NoisePerlin(random, 5, 0.3, 1.8, 1.8);
		this.noise5 = new NoisePerlin(random, 3, 2.5, 1.7, 1.9);
		this.noise6 = new NoisePerlin(random, 3, 8.0, 2.5, 2.0);
	}
	
	private static final byte
	size = 3, scale = size * 2 + 1;
	
	private static final float[] parabolicField;
	
	private static final DoubleUnaryOperator randomNoiseOperator = x -> {
		//[0.0, 1.0]
		x = x * 1.5 - 0.5;
		//[-0.5, 1.0]
		if (x < 0)
		{
			x *= -0.5F;
		}
		//[-0.25, 1.0]
		x = x * 4.0 - 2.0;
		//[-1.0, 2.0]
		if (x > 0)
		{
			x *= 2;
		}
		//[-1.0, 4.0]
		return (x - 1.5) * 0.2;
		//[-0.5, 0.5]
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
				//Rescaled
				double d1 = (ySize - 1) * (root * 0.15625 + 0.125 + 0.03125);
				double d2 = 1.0 + variation * randomNoiseOperator.applyAsDouble(this.cache4[i2]);
				if (d2 < 0.5F) d2 = 0.5F + (d2 - 0.5F) * 0.3F;
				//Height calculate
				for (int y = 1; y <= ySize; y++)
				{
					double off = (d1 - y) / d2;
					if (off >= 0.0)
						off *= 4.0;
					double output = off + Maths.lerp(this.cache1[i1], this.cache2[i1], this.cache3[i1]);
					if (y > ySize - 4)
						output = Maths.lerp(output, -1.0, (ySize - y) / 3.0F);
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
						int Y = y1 | (y << 3) | arrayYHeight;
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
	
	public static final IProperty<IBlockState[]> ROCKS = IPropertyMap.IProperty.to(FarSurfaceDataGenerator.ROCK_DEFAULT);
	public static final IProperty<IBlockState[]> SOILS = IPropertyMap.IProperty.to(FarSurfaceDataGenerator.SOIL_DEFAULT);
	public static final IProperty<Double> RAND_HEIGHT = IPropertyMap.IProperty.to(0.0D);
	
	private static IPropertyMap compact(IPropertyMap map, double randHeight, IBlockState[] rocks, IBlockState[] covers)
	{
		map.put(ROCKS, rocks);
		map.put(SOILS, covers);
		map.put(RAND_HEIGHT, randHeight);
		return map;
	}
	
	private void replaceChunkBlock(ChunkPrimer primer, int chunkX, int chunkZ, Random random)
	{
		IWorldPropProvider propProvider = WorldPropHandler.getWorldProperty(this.world);
		this.cache5 = this.noise5.noise(this.cache5, 16, 16, (double) chunkX, (double) chunkZ, 0.125, 0.125);
		
		IBlockState[][] rockLayer = getRockLayer(chunkX >> 4, chunkZ >> 4);
		IBlockState[][] coverLayer = this.biomeProvider.dataGenerator.getCoverLayer(chunkX >> 4, chunkZ >> 4);
		HashPropertyMap map = new HashPropertyMap(2, 1.0F);
		for (int x = 0; x < 16; ++x)
			for (int z = 0; z < 16; ++z)
			{
				int index = z << 4 | x;
				this.biomes[index].genTerrainBlocks(this.world, random, primer, chunkX | x, chunkZ | z, propProvider, compact(map, this.cache5[index], rockLayer[index], coverLayer[index]));
				map.clear();
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
		float temp = (provider.getAverageTemperature(this.world, pos) + 5.0F) / 9.0F;
		float rainfall = provider.getAverageHumidity(this.world, pos) / 4.0F;
		
		//MinX & MinZ coordinate.
		final int x1 = x << 4;
		final int z1 = z << 4;
		
		//Tree generation.
		if (!MinecraftForge.TERRAIN_GEN_BUS.post(new FarGenerationEvent(TREE, this.world, x, z, this)))
		{
			WeightedRandomSelector<ITreeGenerator> treeGenerationSelector = new WeightedRandomSelector<>();
			FarSurfaceDataGenerator.addVanillaTrees(biome, this.world, x, z, this.noise6, temp, rainfall, treeGenerationSelector);
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
					switchState(true);
					if (generator.generateTreeAt(this.world, x2, y2, z2, this.random, null))
					{
						count --;
					}
					switchState(false);
				}
			}
		}
		
		//Wild Crop generation.
		if (!MinecraftForge.TERRAIN_GEN_BUS.post(new FarGenerationEvent(WILD_CROP, this.world, x, z, this)))
		{
			WeightedRandomSelector<ICrop> cropGenerationSelector = new WeightedRandomSelector<>();
			FarSurfaceDataGenerator.addVanillaCrops(x, z, this.random, this.noise6, temp, rainfall, cropGenerationSelector);
			
			if (cropGenerationSelector.weight() > 0 && biome.cropPerChunkBase > 0)
			{
				switchState(true);
				int count = 1 + biome.cropPerChunkBase + L.nextInt(biome.cropPerChunkRand, this.random) + MathHelper.log2DeBruijn(cropGenerationSelector.weight());
				ICrop crop;
				do
				{
					crop = cropGenerationSelector.next(this.random);
					count--;
				}
				while((crop == null || crop == ICrop.VOID) && count > 0);
				for (int i = 0; i < count; ++i)
				{
					int x2 = x1 + this.random.nextInt(16);
					int z2 = z1 + this.random.nextInt(16);
					int y2 = this.world.getHeight(x2, z2);
					pos.setPos(x2, y2, z2);
					if (crop.canPlantAt(new CropAccessSimulated(this.world, pos, crop, null, true)))
					{
						BlockCrop.CROP_THREAD.set(crop);
						this.world.setBlockState(pos, CROP);
					}
				}
				switchState(false);
			}
		}
		
		MinecraftForge.TERRAIN_GEN_BUS.post(new DecorateBiomeEvent.Pre(this.world, this.random, pos));
		if (biome.decorator != null)
		{
			biome.decorator.decorate(this.world, this.random, pos, this);
		}
		MinecraftForge.TERRAIN_GEN_BUS.post(new DecorateBiomeEvent.Post(this.world, this.random, pos));
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
	
	public IBlockState[][] getRockLayer(int chunkX, int chunkZ)
	{
		return this.biomeProvider.dataGenerator.getRockLayer(chunkX, chunkZ);
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
	}
}