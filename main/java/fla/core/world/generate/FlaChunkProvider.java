package fla.core.world.generate;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.world.biome.FlaBiome;
import fla.core.world.biome.FlaBiome.BiomeBlockGen;

public class FlaChunkProvider implements IChunkProvider
{
	private final long seed;
	
    private boolean mapFeaturesEnabled;
	protected World worldObj;
    private Random rand;
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    
    public FlaChunkProvider(World world, long seed, boolean features)
    {
    	this.worldObj = world;
    	this.mapFeaturesEnabled = features;
    	this.rand = new Random(seed);
    	this.seed = seed;
    }

	@Override
	public boolean chunkExists(int x, int z) 
	{
		return false;
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        byte[] biome = new byte[256];
        this.generateBlockAndBiomeOnWorld(x, z, ablock, biome);
        Chunk chunk = new Chunk(this.worldObj, ablock, abyte, x, z);
        chunk.setBiomeArray(biome);
		return chunk;
	}

	protected void generateBlockAndBiomeOnWorld(int x, int z, Block[] ablock, byte[] biomes) 
	{
		int[] aHeight = FlaWorldGenerateHelper.getHeight(worldObj.getSeed(), x, z);
        for (int x1 = 0; x1 < 16; ++x1)
        {
            for (int z1 = 0; z1 < 16; ++z1)
            {
            	FlaBiome biome = FlaBiome.getBiome(worldObj.getSeed(), aHeight[x1 * 16 + z1], new BlockPos(worldObj, x * 16 + x1, 0, z * 16 + z1));
            	biomes[x1 * 16 + z1] = (byte) biome.biomeID;
            	BiomeBlockGen gen = biome.getBlockGen();
            	int[] biomeGenBlock = gen.getFullHeight(rand);
            	int i = 0;
            	for(int i1 = 0; i1 < biomeGenBlock.length; ++i1)
            	{
            		i += biomeGenBlock[i1];
            	}
            	int height = 256;
            	int biomeHeight = aHeight[x1 * 16 + z1];
            	while(height > biomeHeight)
            	{
            		if(height < FlaValue.SEALEVEL)
            		{
            			ablock[x1 * 4096 | z1 * 256 | height] = Blocks.water;
            		}
            		else
            		{
            			ablock[x1 * 4096 | z1 * 256 | height] = Blocks.air;
            		}
            		--height;
            	}
            	for(; height > 0; --height)
            	{
            		if(height == 0)
            		{
            			ablock[x1 * 4096 | z1 * 256 | height] = Blocks.bedrock;
            			continue;
            		}
            		boolean flag = false;
            		int j = 0;
            		for(int q = 0; q < biomeGenBlock.length; ++q)
            		{
            			j += biomeGenBlock[q];
            			if(height > biomeHeight - j)
            			{
                    		ablock[x1 * 4096 | z1 * 256 | height] = gen.getBlockMap().get(q);
                    		flag = true;
                    		break;
            			}
            		}
            		if(!flag) ablock[x1 * 4096 | z1 * 256 | height] = Blocks.stone;
            	}
            }
        }
	}

	@Override
	public Chunk loadChunk(int x, int z)
	{
		return this.provideChunk(x, z);
	}

	@Override
	public void populate(IChunkProvider provider, int x, int z) 
	{
        BlockFalling.fallInstantly = true;
        int k = x * 16;
        int l = z * 16;
        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
        FlaBiome biomegenbase = FlaBiome.getFlaBiome(biome);
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * i1 + (long)z * j1 ^ this.worldObj.getSeed());
        boolean flag = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, worldObj, rand, x, z, flag));

        if (this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, x, z);
        }

        int k1;
        int l1;
        int i2;

        if (biomegenbase.generateLake() && !flag && this.rand.nextInt(4) == 0
            && TerrainGen.populate(provider, worldObj, rand, x, z, flag, LAKE))
        {
            k1 = k + this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(128) + 32;
            i2 = l + this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, k1, l1, i2);
        }

        if (TerrainGen.populate(provider, worldObj, rand, x, z, flag, LAVA) && !flag && this.rand.nextInt(8) == 0)
        {
            k1 = k + this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(this.rand.nextInt(56) + 8);
            i2 = l + this.rand.nextInt(16) + 8;

            if (l1 < 63 || this.rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, k1, l1, i2);
            }
        }

        boolean doGen = TerrainGen.populate(provider, worldObj, rand, x, z, flag, DUNGEON);
        for (k1 = 0; doGen && k1 < 8; ++k1)
        {
            l1 = k + this.rand.nextInt(16) + 8;
            i2 = this.rand.nextInt(40) + 8;
            int j2 = l + this.rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.worldObj, this.rand, l1, i2, j2);
        }

        biomegenbase.decorate(this.worldObj, this.rand, k, l);
        if (TerrainGen.populate(provider, worldObj, rand, x, z, flag, ANIMALS))
        {
        	SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
        }
        k += 8;
        l += 8;

        doGen = TerrainGen.populate(provider, worldObj, rand, x, z, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1)
        {
            for (l1 = 0; l1 < 16; ++l1)
            {
                i2 = this.worldObj.getPrecipitationHeight(k + k1, l + l1);

                if (this.worldObj.isBlockFreezable(k1 + k, i2 - 1, l1 + l))
                {
                    this.worldObj.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(k1 + k, i2, l1 + l, true))
                {
                    this.worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, worldObj, rand, x, z, flag));

        BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) 
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
		return "RandomLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType type,
			int x, int y, int z) 
	{
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x, z);
        return type == EnumCreatureType.monster ? new ArrayList() : biomegenbase.getSpawnableList(type);
	}

	@Override
	public ChunkPosition func_147416_a(World world, String tag, int x, int y, int z) 
	{
		return null;
	}

	@Override
	public int getLoadedChunkCount() 
	{
		return 0;
	}

	@Override
	public void recreateStructures(int x, int z) { }

	@Override
	public void saveExtraData() {	}
}