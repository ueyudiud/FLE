package fargen.core.worldgen.v;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoiseCoherent;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

public class FarVoidChunkGenerator implements IChunkGenerator
{
	public static final IBlockState	BEDROCK	= Blocks.BEDROCK.getDefaultState();
	public static final IBlockState	END		= Blocks.END_STONE.getDefaultState();
	
	private static final float	OFFSET	= 1E-8F;
	private static final float	CHANCE	= 1.5F;
	
	protected World		world;
	/** Height noise. */
	protected NoiseBase	noise1;
	/** Generate noise. */
	protected NoiseBase	noise2;
	protected double[]	cache1;
	protected double[]	cache2;
	
	protected Random random;
	
	public FarVoidChunkGenerator(World world, long seed)
	{
		this.world = world;
		random = new Random(seed);
		noise1 = new NoiseCoherent(random.nextLong(), 1.2F);
		noise2 = new NoisePerlin(random, 10, 16D, 2.5D, 1.8D);
	}
	
	protected void replaceChunk(int x, int z, ChunkPrimer primer)
	{
		cache1 = noise1.noise(cache1, 16, 128, 16, x, 0, z);
		cache2 = noise2.noise(cache2, 16, 16, x, z);
		for (int i = 0; i < 16; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				if (x == 0 && z == 0 && i == 0 && j == 0)
				{
					primer.setBlockState(0, 127, 0, BEDROCK);
					continue;
				}
				int height = (int) (cache2[j << 1 | i] * 128);
				if (height > 128)
				{
					height = 128;
				}
				double disSq = (x + i) * (x + i) + (z + j) * (z + j);
				for (int k = 0; k < height; ++k)
				{
					disSq += OFFSET;
					double val = 1D + Math.log1p(Math.sqrt(disSq));
					val = val * cache1[j << 11 | k << 4 | i];
					if (val <= CHANCE)
					{
						primer.setBlockState(i, k, j, END);
					}
				}
			}
		}
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		random.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer primer = new ChunkPrimer();
		replaceChunk(x << 4, z << 4, primer);
		return new Chunk(world, primer, x, z);
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
