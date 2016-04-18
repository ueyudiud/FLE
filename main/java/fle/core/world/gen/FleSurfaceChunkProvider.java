package fle.core.world.gen;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.util.ChunkBuilder;
import fle.api.block.ItemSubstance;
import fle.core.block.BlockRock;
import fle.core.tile.statics.TileEntityRock;
import fle.core.world.rock.RockLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class FleSurfaceChunkProvider implements IChunkProvider
{
	private static final int smoothSize = 4;
	private static final int smoothLength = 9;
	private static final int smoothBiomeSize = 14;
    private static final float[] parabolicField = new float[smoothLength * smoothLength];
    
    static
    {
        for (int j = -smoothSize; j <= smoothSize; ++j)
        {
            for (int k = -smoothSize; k <= smoothSize; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                parabolicField[j + smoothSize + (k + smoothSize) * smoothLength] = f;
            }
        }
    }
	
	protected World world;
	protected WorldType type;
	protected final ChunkBuilder builder = new ChunkBuilder();
	protected Random random;
	/** The base height noise. */
	protected NoiseGeneratorOctaves noise1;
	/** The max random height noise. */
	protected NoiseGeneratorOctaves noise2;
	/** The minimum random height noise. */
	protected NoiseGeneratorOctaves noise3;
	/** The random random height noise. */
	protected NoiseGeneratorOctaves noise4;
	/** The surface height noise. */
	protected NoiseGeneratorOctaves noise5;
	/** Stone height noise. */
    protected NoiseGeneratorPerlin noise6;
	/** Stone ph noise. */
    protected NoiseGeneratorPerlin noise7;
	/** Stone deepness noise. */
    protected NoiseGeneratorPerlin noise8;

	private BiomeGenBase[] biomes;
	
	protected double[] cache1;
	protected double[] cache2;
	protected double[] cache3;
	protected double[] cache4;
	/** Last height cache. */
	protected double[] cache5;
	protected double[] cache6;
	protected double[] cache7;
	protected double[] cache8;
	
	public FleSurfaceChunkProvider(World world, long seed, boolean enableFeatures)
	{
		this.world = world;
		this.type = world.getWorldInfo().getTerrainType();
		this.random = new Random(seed);
		this.noise1 = new NoiseGeneratorOctaves(random, 16);
		this.noise2 = new NoiseGeneratorOctaves(random, 16);
		this.noise3 = new NoiseGeneratorOctaves(random, 16);
		this.noise4 = new NoiseGeneratorOctaves(random, 8);
//		this.noise5 = new NoiseGeneratorOctaves(random, 6);
		this.noise6 = new NoiseGeneratorPerlin(random, 4);
		this.noise7 = new NoiseGeneratorPerlin(random, 6);
		this.noise8 = new NoiseGeneratorPerlin(random, 6);
		this.cache5 = new double[825];
	}

	@Override
	public boolean chunkExists(int x, int z)
	{
		return true;
	}
	
	protected void gen1(int x, int z, BiomeGenBase[] biomes)
	{
		cache1 = noise1.generateNoiseOctaves(cache1, x, z, 5, 5, 100D, 100D, 100D);
		cache2 = noise2.generateNoiseOctaves(cache2, x, 0, z, 5, 33, 5, 200D, 128D, 200D);
		cache3 = noise3.generateNoiseOctaves(cache3, x, 0, z, 5, 33, 5, 200D, 128D, 200D);
		cache4 = noise4.generateNoiseOctaves(cache4, x, 0, z, 5, 33, 5, 8D, 4D, 8D);
//		cache5 = noise5.generateNoiseOctaves(cache5, x, z, 5, 5, 32D, 4D, 32D);
		
		int id1 = 0, id2 = 0;
		BiomeGenBase biome, biome1;
		
		for (int j1 = 0; j1 < 5; ++j1)
		{
			for (int k1 = 0; k1 < 5; ++k1)
			{
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;
				biome = biomes[j1 + smoothSize + (k1 + smoothSize) * smoothBiomeSize];
				
				for (int l1 = -smoothSize; l1 <= smoothSize; ++l1)
				{
					for (int i2 = -smoothSize; i2 <= smoothSize; ++i2)
					{
						biome1 = biomes[j1 + l1 + smoothSize + (k1 + i2 + smoothSize) * smoothBiomeSize];
						float f3 = biome1.rootHeight;
						float f4 = biome1.heightVariation;
//						if (type == WorldType.AMPLIFIED && f3 > 0.0F)
//						{
//							f3 = 1.0F + f3 * 2.0F;
//							f4 = 1.0F + f4 * 4.0F;
//						}						
						float f5 = parabolicField[l1 + smoothSize + (i2 + smoothSize) * smoothLength] / (f3 + 2.0F);
						if (biome1.rootHeight > biome.rootHeight)
						{
							f5 *= 0.5F;
						}						
						f += f4 * f5;
						f1 += f3 * f5;
						f2 += f5;
					}
				}
				
				f /= f2;
				f1 /= f2;
				f = f * 0.9F + 0.1F;
				f1 = (f1 * 4.0F - 1.0F) / 8.0F;
				
				double d1 = cache1[id1] / 8000D;//Provide base height coefficient.
				if (d1 < 0.0D)
		        {
		            d1 = -d1 * 0.5D;
		        }
		        d1 = d1 * 3.0D - 2.0D;
		        if (d1 < 0.0D)
		        {
		            d1 /= 2.0D;
		            if (d1 < -1.0D)
		            {
		                d1 = -1.0D;
		            }
		            d1 /= 4.0D;
		        }
		        else
		        {
		            if (d1 > 1.0D)
		            {
		                d1 = 1.0D;
		            }
		            d1 /= 4.0D;
		        }

                ++id1;
                double d2 = (double)f1;
                double d3 = (double)f;
                d2 += d1 * 0.2D;
                d2 *= 9.5D / 8.0D;
                double d5 = 16.5D + d2 * 4.0D;//Base height.

                for (int j2 = 0; j2 < 33; ++j2)
                {
                    double d6 = ((double)j2 - d5) * 12.0D * .5D / d3;

                    if (d6 < 0.0D)
                    {
                        d6 *= 4.0D;
                    }

                    double d7 = cache2[id2] / 512.0D;
                    double d8 = cache3[id2] / 512.0D;
                    double d9 = (cache4[id2] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

                    if (j2 > 29)
                    {
                        double d11 = (double)((float)(j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) - 10.0D * d11;
                    }

                    cache5[id2] = d10;
                    ++id2;
                }
			}
		}
	}
	
	protected void gen2(Block[] blocks, byte[] metas)
	{
		byte b0 = 127;
		for (int k = 0; k < 4; ++k)
		{
			int l = k * 5;
			int i1 = (k + 1) * 5;
			
			for (int j1 = 0; j1 < 4; ++j1)
			{
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;
				
				for (int k2 = 0; k2 < 32; ++k2)
				{
					double d0 = 0.125D;
					double d1 = cache5[k1 + k2];
					double d2 = cache5[l1 + k2];
					double d3 = cache5[i2 + k2];
					double d4 = cache5[j2 + k2];
					double d5 = (cache5[k1 + k2 + 1] - d1) * d0;
					double d6 = (cache5[l1 + k2 + 1] - d2) * d0;
					double d7 = (cache5[i2 + k2 + 1] - d3) * d0;
					double d8 = (cache5[j2 + k2 + 1] - d4) * d0;
					
					for (int l2 = 0; l2 < 8; ++l2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;
						
						for (int i3 = 0; i3 < 4; ++i3)
						{
							int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
							short short1 = 256;
							j3 -= short1;
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;	
							
							for (int k3 = 0; k3 < 4; ++k3)
							{
								if ((d15 += d16) > 0.0D)
								{
									blocks[j3 += short1] = Blocks.stone;
								}
								else if (k2 * 8 + l2 < b0)
								{
									blocks[j3 += short1] = EnumBlock.water.block();
									metas[j3] = 15;
								}
								else
								{
									blocks[j3 += short1] = null;
								}
							}
							
							d10 += d12;
							d11 += d13;
						}
						
						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	protected void gen3(int x, int z, Block[] blocks, byte[] metas, BiomeGenBase[] biomes2)
    {
        ReplaceBiomeBlocks event = new ReplaceBiomeBlocks(this, x, z, blocks, metas, biomes, world);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        double d0 = 0.03125D;
        cache6 = noise6.func_151599_a(cache6, (double)(x << 4), (double)(z << 4), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                biomes[l + k * 16].genTerrainBlocks(world, random, blocks, metas, x * 16 + k, z * 16 + l, cache6[l + k * 16]);
            }
        }
    }
    
	protected void gen4(int x, int z, ChunkBuilder builder)
	{
		cache7 = noise7.func_151599_a(cache7, x * 4, z * 4, 5, 5, .2D, .2D, .2D);
		cache8 = noise8.func_151599_a(cache8, x * 4, z * 4, 5, 5, .2D, .2D, .2D);
		double d1, d2;
		int l = 0;
		for(int i = 0; i < 5; ++i)
			for(int j = 0; j < 5; ++j)
			{
				d1 = cache7[l] / 20D;
				d2 = cache8[l] / 20D;
				if(d1 > 1D)
				{
					d1 = 1D;
				}
				else if(d1 < -1D)
				{
					d1 = -1D;
				}
				d1 += 1D;
				d1 *= 4D;
				if(d2 < 0D)
				{
					d2 = -d2 * .5D;
				}
				d2 = d2 * 1.2D + 0.1D;
				if(d2 < .2D)
				{
					d2 += .4D;
					d2 /= 4D;
				}
				else if(d2 < .5D)
				{
					d2 = .5D;
				}
				else
				{
					d2 -= .5D;
					d2 *= .8D;
					d2 += .5D;
				}
				cache5[l] = d1;
				cache5[l + 25] = d2;
				l++;
			}
		double d3, d4, d5, d6, d7, d8, d9, d10, d11, d12;
		for(int i = 0; i < 4; ++i)
			for(int j = 0; j < 4; ++j)
			{
				d1 = cache5[j * 5 + i];
				d2 = cache5[j * 5 + i + 1];
				d3 = cache5[(j + 1) * 5 + i];
				d4 = cache5[(j + 1) * 5 + i + 1];
				d3 = (d3 - d1) * .25D;
				d4 = (d4 - d2) * .25D;

				d7 = cache5[j * 5 + i];
				d8 = cache5[j * 5 + i + 1];
				d9 = cache5[(j + 1) * 5 + i];
				d10 = cache5[(j + 1) * 5 + i + 1];
				d9 = (d9 - d7) * .25D;
				d10 = (d10 - d8) * .25D;
				for(int j1 = 0; j1 < 4; ++j1)
				{
					d5 = d1;
					d6 = (d2 - d1) * .25D;
					d11 = d7;
					d12 = (d8 - d7) * .25D;
					for(int i1 = 0; i1 < 5; ++i1)
					{
						for(int k = 0; k < 255; ++k)
						{
							if(builder.get(i * 4 + i1, k, j * 4 + j1) == Blocks.stone)
							{
								builder.add(i * 4 + i1, k, j * 4 + j1, RockLayer.provide(d5, (double) k * d11), 0);
							}
						}
						d5 += d6;
						d11 += d12;
					}
					d1 += d3;
					d2 += d4;
					d7 += d9;
					d8 += d10;
				}
			}
	}
	
	@Override
	public synchronized Chunk provideChunk(int x, int z)
	{
		builder.reset();
		biomes = world.getWorldChunkManager().getBiomesForGeneration(biomes, (x << 2) - smoothSize, (z << 2) - smoothSize, smoothBiomeSize, smoothBiomeSize);
		gen1(x << 2, z << 2, biomes);//Generate height.
		gen2(builder.getBlocks(), builder.getMetas());//Generate base.
		biomes = world.getWorldChunkManager().loadBlockGeneratorData(biomes, x << 4, z << 4, 16, 16);
		builder.setBiomes(biomes);
		gen3(x, z, builder.getBlocks(), builder.getMetas(), biomes);//Replace block by biome.
		gen4(x, z, builder);//Initialize rock layer.
		return builder.build(world, x, z);
	}

	@Override
	public Chunk loadChunk(int x, int z)
	{
		return provideChunk(x, z);
	}

	@Override
	public void populate(IChunkProvider provider, int x, int z)
	{
        BlockFalling.fallInstantly = true;
        int k = x << 4;
        int l = z << 4;
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords(k + 16, l + 16);
        random.setSeed(world.getSeed());
        long i1 = this.random.nextLong() / 2L * 2L + 1L;
        long j1 = this.random.nextLong() / 2L * 2L + 1L;
        random.setSeed((long)x * i1 + (long)z * j1 ^ this.world.getSeed());
        boolean flag = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, world, random, x, z, flag));

//        if (this.mapFeaturesEnabled)
//        {
//            this.mineshaftGenerator.generateStructuresInChunk(this.world, this.rand, x, z);
//            flag = this.villageGenerator.generateStructuresInChunk(this.world, this.rand, x, z);
//            this.strongholdGenerator.generateStructuresInChunk(this.world, this.rand, x, z);
//            this.scatteredFeatureGenerator.generateStructuresInChunk(this.world, this.rand, x, z);
//        }

        int k1;
        int l1;
        int i2;

//        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && this.random.nextInt(4) == 0
//            && TerrainGen.populate(provider, world, random, x, z, flag, LAKE))
//        {
//            k1 = k + this.random.nextInt(16) + 8;
//            l1 = this.random.nextInt(256);
//            i2 = l + this.random.nextInt(16) + 8;
//            (new WorldGenLakes(Blocks.water)).generate(this.world, this.random, k1, l1, i2);
//        }
//
//        if (TerrainGen.populate(provider, world, random, x, z, flag, LAVA) && !flag && this.random.nextInt(8) == 0)
//        {
//            k1 = k + this.random.nextInt(16) + 8;
//            l1 = this.random.nextInt(this.random.nextInt(248) + 8);
//            i2 = l + this.random.nextInt(16) + 8;
//
//            if (l1 < 63 || this.random.nextInt(10) == 0)
//            {
//                (new WorldGenLakes(Blocks.lava)).generate(this.world, this.random, k1, l1, i2);
//            }
//        }

        boolean doGen = TerrainGen.populate(provider, world, random, x, z, flag, DUNGEON);
        for (k1 = 0; doGen && k1 < 8; ++k1)
        {
            l1 = k + this.random.nextInt(16) + 8;
            i2 = this.random.nextInt(256);
            int j2 = l + this.random.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.world, this.random, l1, i2, j2);
        }

        biomegenbase.decorate(this.world, this.random, k, l);
        if (TerrainGen.populate(provider, world, random, x, z, flag, ANIMALS))
        {
        	SpawnerAnimals.performWorldGenSpawning(this.world, biomegenbase, k + 8, l + 8, 16, 16, this.random);
        }
        k += 8;
        l += 8;

        doGen = TerrainGen.populate(provider, world, random, x, z, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1)
        {
            for (l1 = 0; l1 < 16; ++l1)
            {
                i2 = world.getPrecipitationHeight(k + k1, l + l1);

                if (world.isBlockFreezable(k1 + k, i2 - 1, l1 + l))
                {
                    world.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
                }

                if (world.func_147478_e(k1 + k, i2, l1 + l, true))
                {
                    world.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, world, random, x, z, flag));

        BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate update)
	{
		return true;
	}

	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public boolean canSave()
	{
		return true;
	}

	@Override
	public String makeString()
	{
		return "FleSurfaceRandomLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z)
	{
		return null;
	}

	@Override
	public ChunkPosition func_147416_a(World world, String type, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}

	@Override
	public void recreateStructures(int x, int z)
	{
		
	}

	@Override
	public void saveExtraData()
	{
		
	}
}