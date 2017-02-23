package fargen.core.worldgen.surface;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import fargen.core.instance.Layers;
import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FarSurfaceChunkGenerator implements IChunkGenerator
{
	private final World world;
	
	private final NoiseBase noise1;
	private final NoiseBase noise2;
	private final NoiseBase noise3;
	private final NoiseBase noise4;
	
	private double[] cache1;
	private double[] cache2;
	private double[] cache3;
	private double[] cache4;
	
	private double[]
			cachea = new double[256],
			cacheb = new double[256];
	
	private GenLayer[] layer;
	
	public FarSurfaceChunkGenerator(World world, long seed)
	{
		this.world = world;
		Random random = new Random(seed);
		this.noise1 = new NoisePerlin(random, 16, 4.0, 1.5, 1.6);
		this.noise2 = new NoisePerlin(random, 7, 0.8, 2.0, 1.8);
		this.noise3 = null;
		this.noise4 = null;
		this.layer = Layers.wrapSurface(seed);
	}
	
	private double[] generateChunkData(int x, int z, int[] biomes)
	{
		this.cache1 = this.noise1.noise(this.cache1, 16, 16, x, z);
		this.cache2 = this.noise2.noise(this.cache2, 16, 16, x, z);
		for (int i = 0; i < 16; ++i)
			for (int j = 0; j < 16; ++j)
			{
				int idx = i << 4 | j;
				this.cachea[idx] = 1.0 - (1.0 - this.cache1[idx]) * this.cache1[idx] * 4.0;
				this.cachea[idx] = this.cachea[idx] * this.cache2[idx];
			}
		return this.cachea;
	}
	
	protected float getHeight(int x, int y, int[] biomes)
	{
		float height = 0.0F;
		for (int i1 = 0; i1 < 7; ++i1)
			for (int j1 = 0; j1 < 7; ++j1)
			{
				Biome biome = Biome.getBiome(biomes[(x + i1) * 10 + y + j1]);
				if (biome.getBaseHeight() > 0.01F)
				{
					height += 1F;
				}
				else
				{
					height -= 1F;
				}
			}
		return height / 49.0F;
	}
	
	private void replaceBaseBlockFromChunkData(ChunkPrimer primer, double[] data)
	{
		for (int i = 0; i < 16; ++i)
			for (int j = 0; j < 16; ++j)
			{
				int h1 = (int) (data[i << 4 | j] * 146) + 64;
				primer.setBlockState(j, h1, i, Blocks.GRASS.getDefaultState());
				primer.setBlockState(j, h1 - 1, i, Blocks.DIRT.getDefaultState());
				primer.setBlockState(j, h1 - 2, i, Blocks.DIRT.getDefaultState());
				primer.setBlockState(j, h1 - 3, i, Blocks.DIRT.getDefaultState());
				for (h1 = h1 - 3; h1 > 0; --h1)
				{
					primer.setBlockState(j, h1, i, Blocks.STONE.getDefaultState());
				}
				primer.setBlockState(j, 0, i, Blocks.BEDROCK.getDefaultState());
			}
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		IntCache.resetIntCache();
		int[] biomes = this.layer[0].getInts((x << 2) - 3, (z << 2) - 3, 11, 11);
		
		ChunkPrimer primer = new ChunkPrimer();
		replaceBaseBlockFromChunkData(primer, generateChunkData(x << 4, z << 4, biomes));
		Chunk chunk = new Chunk(this.world, primer, x, z);
		chunk.generateSkylightMap();
		byte[] array = chunk.getBiomeArray();
		for (int i = 0; i < 256; ++i)
		{
			array[i] = (byte) biomes[i];
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
}