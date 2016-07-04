package fle.core.world.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumBiome;
import farcore.interfaces.ICustomTempGenerate;
import farcore.lib.collection.IntArray;
import farcore.lib.world.biome.BiomeBase;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoisePerlin;
import fle.core.world.climate.Climate;
import fle.core.world.climate.IClimateHandler;
import fle.core.world.layer.LayerBase;
import fle.core.world.layer.climate.LayerClimateSurface;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FleSurfaceManager extends WorldChunkManager implements ICustomTempGenerate, IClimateHandler
{
	private double[] cacheRainfall;
	
	public NoisePerlin tempNoise;
	public NoisePerlin rainfallNoise;
	
	protected DoubleCache tempCache = new DoubleCache();
	protected DoubleCache rainfallCache = new DoubleCache();
	
    protected GenLayer genBiomes;
    /** A GenLayer containing the indices into BiomeBase.biomeList[] */
    protected GenLayer biomeIndexLayer;
    protected LayerClimateSurface climateLayer;
    /** The BiomeCache object for this world. */
    protected BiomeCache biomeCache;
	protected ClimateCache climateCache;
    /** A list of biomes that the player can spawn in. */
    protected List<BiomeGenBase> biomesToSpawnIn;
    
	public FleSurfaceManager(World world)
	{
		super(world);

		Random random = new Random(world.getSeed());
		
        biomeCache = new BiomeCache(this);
        climateCache = new ClimateCache(this);
        biomesToSpawnIn = new ArrayList();
        biomesToSpawnIn.add(EnumBiome.plain.biome());
        
        GenLayer[] agenlayer = LayerBase.wrapSuface(world.getSeed(), world.getWorldInfo().getTerrainType());
        climateLayer = (LayerClimateSurface) agenlayer[3];
        tempNoise = (NoisePerlin) climateLayer.noise3;
        rainfallNoise = (NoisePerlin) climateLayer.noise2;
        agenlayer = getModdedBiomeGenerators(world.getWorldInfo().getTerrainType(), world.getSeed(), agenlayer);
        genBiomes = agenlayer[0];
        biomeIndexLayer = agenlayer[1];
	}

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
	@Override
    public List getBiomesToSpawnIn()
    {
        return biomesToSpawnIn;
    }

    /**
     * Returns the BiomeBase related to the x, z position on the world.
     */
	@Override
    public BiomeGenBase getBiomeGenAt(int x, int z)
    {
        return biomeCache.getBiomeGenAt(x, z);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
	@Override
    public float[] getRainfall(float[] list, int x, int z, int w, int h)
    {
        IntCache.resetIntCache();

        if (list == null || list.length < w * h)
        {
            list = new float[w * h];
        }

        cacheRainfall = rainfallNoise.noise(cacheRainfall, w, h, x, z);

        for (int i1 = 0; i1 < w * h; ++i1)
        {
            try
            {
                float f = (float) cacheRainfall[i1];
                if (f > 1.0F)
                {
                    f = 1.0F;
                }

                list[i1] = f;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(list.length));
                crashreportcategory.addCrashSection("x", Integer.valueOf(x));
                crashreportcategory.addCrashSection("z", Integer.valueOf(z));
                crashreportcategory.addCrashSection("w", Integer.valueOf(w));
                crashreportcategory.addCrashSection("h", Integer.valueOf(h));
                throw new ReportedException(crashreport);
            }
        }

        return list;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float temp, int y)
    {
        return temp;
    }

    /**
     * Returns an array of biomes for the location input.
     */
	@Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int w, int h)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < w * h)
        {
            biomes = new BiomeGenBase[w * h];
        }

        int[] aint = genBiomes.getInts(x, z, w, h);

        try
        {
            for (int i1 = 0; i1 < w * h; ++i1)
            {
                biomes[i1] = BiomeBase.getBiome(aint[i1]);
            }

            return biomes;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id.");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("w", Integer.valueOf(w));
            crashreportcategory.addCrashSection("h", Integer.valueOf(h));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
	@Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int y, int w, int h)
    {
        return getBiomeGenAt(biomes, x, y, w, h, true);
    }

    public Climate[] getClimateAt(Climate[] climates, int x, int z, int w, int h, boolean ignoreCache)
    {
        IntCache.resetIntCache();

        if (climates == null || climates.length < w * h)
        {
            climates = new Climate[w * h];
        }

        if (ignoreCache && w == 16 && h == 16 && (x & 0xF) == 0 && (z & 0xF) == 0)
        {
            Climate[] abiomegenbase1 = climateCache.getClimatesAt(x, z);
            System.arraycopy(abiomegenbase1, 0, climates, 0, 256);
            return climates;
        }
        else
        {
            int[] aint = climateLayer.getInts(x, z, w, h);

            for (int i1 = 0; i1 < w * h; ++i1)
            {
                climates[i1] = Climate.climates.get(aint[i1]);
            }

            return climates;
        }
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
	@Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int z, int w, int h, boolean ignoreCache)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < w * w)
        {
            biomes = new BiomeGenBase[w * w];
        }

        if (ignoreCache && w == 16 && h == 16 && (x & 0xF) == 0 && (z & 0xF) == 0)
        {
            BiomeGenBase[] abiomegenbase1 = biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase1, 0, biomes, 0, 256);
            return biomes;
        }
        else
        {
            int[] aint = biomeIndexLayer.getInts(x, z, w, h);

            for (int i1 = 0; i1 < w * h; ++i1)
            {
                biomes[i1] = BiomeBase.getBiome(aint[i1]);
            }

            return biomes;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
	@Override
    public boolean areBiomesViable(int x, int y, int z, List list)
    {
        IntCache.resetIntCache();
        int l = x - z >> 2;
        int i1 = y - z >> 2;
        int j1 = x + z >> 2;
        int k1 = y + z >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = genBiomes.getInts(l, i1, l1, i2);

        try
        {
            for (int j2 = 0; j2 < l1 * i2; ++j2)
            {
                BiomeBase biomegenbase = BiomeBase.getBiome(aint[j2]);

                if (!list.contains(biomegenbase))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(y));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
            crashreportcategory.addCrashSection("allowed", list);
            throw new ReportedException(crashreport);
        }
    }

	@Override
    public ChunkPosition findBiomePosition(int x, int z, int p, List list, Random rand)
    {
        IntCache.resetIntCache();
        int l = x - p >> 2;
        int i1 = z - p >> 2;
        int j1 = x + p >> 2;
        int k1 = z + p >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        ChunkPosition chunkposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2)
        {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            BiomeBase biomegenbase = BiomeBase.getBiome(aint[k2]);

            if (list.contains(biomegenbase) && (chunkposition == null || rand.nextInt(j2 + 1) == 0))
            {
                chunkposition = new ChunkPosition(l2, 0, i3);
                ++j2;
            }
        }

        return chunkposition;
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
	@Override
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
        this.climateCache.cleanupCache();
        this.tempCache.reset();
        this.rainfallCache.reset();
    }

	@Override
	public Climate getClimateAt(int x, int z)
	{
		return climateCache.getClimateAt(x, z);
	}
	
	@Override
	public float getBaseTemperature(int x, int z)
	{
		return (float) tempCache.cache(x, z, tempNoise) * 2F - .5F;
	}

	@Override
	public float getBaseRainfall(int x, int z)
	{
		return (float) rainfallCache.cache(x, z, rainfallNoise) * 1.5F;
	}
	
	private static class ClimateCache
	{
	    /** Reference to the WorldChunkManager */
	    private final FleSurfaceManager chunkManager;
	    /** The last time this BiomeCache was cleaned, in milliseconds. */
	    private long lastCleanupTime;
	    /** The map of keys to BiomeCacheBlocks. Keys are based on the chunk x, z coordinates as (x | z << 32). */
	    private LongHashMap cacheMap = new LongHashMap();
	    /** The list of cached BiomeCacheBlocks */
	    private List cache = new ArrayList();

	    public ClimateCache(FleSurfaceManager manager)
	    {
	        this.chunkManager = manager;
	    }

	    /**
	     * Returns a biome cache block at location specified.
	     */
	    public Block getClimateCacheBlock(int x, int z)
	    {
	        x >>= 4;
	        z >>= 4;
	        long k = (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
	        Block block = (Block) this.cacheMap.getValueByKey(k);

	        if (block == null)
	        {
	            block = new Block(x, z);
	            this.cacheMap.add(k, block);
	            this.cache.add(block);
	        }

	        block.lastAccessTime = MinecraftServer.getSystemTimeMillis();
	        return block;
	    }

	    /**
	     * Returns the BiomeGenBase related to the x, z position from the cache.
	     */
	    public Climate getClimateAt(int x, int z)
	    {
	        return this.getClimateCacheBlock(x, z).getClimateAt(x, z);
	    }

	    /**
	     * Removes BiomeCacheBlocks from this cache that haven't been accessed in at least 30 seconds.
	     */
	    public void cleanupCache()
	    {
	        long i = MinecraftServer.getSystemTimeMillis();
	        long j = i - this.lastCleanupTime;

	        if (j > 7500L || j < 0L)
	        {
	            this.lastCleanupTime = i;

	            for (int k = 0; k < this.cache.size(); ++k)
	            {
	                BiomeCache.Block block = (BiomeCache.Block)this.cache.get(k);
	                long l = i - block.lastAccessTime;

	                if (l > 30000L || l < 0L)
	                {
	                    this.cache.remove(k--);
	                    long i1 = (long)block.xPosition & 4294967295L | ((long)block.zPosition & 4294967295L) << 32;
	                    this.cacheMap.remove(i1);
	                }
	            }
	        }
	    }

	    /**
	     * Returns the array of cached biome types in the BiomeCacheBlock at the given location.
	     */
	    public Climate[] getClimatesAt(int x, int z)
	    {
	        return this.getClimateCacheBlock(x, z).biomes;
	    }

	    public class Block
	    {
	        /** An array of chunk rainfall values saved by this cache. */
	        public float[] rainfallValues = new float[256];
	        /** The array of biome types stored in this BiomeCacheBlock. */
	        public Climate[] biomes = new Climate[256];
	        /** The x coordinate of the BiomeCacheBlock. */
	        public int xPosition;
	        /** The z coordinate of the BiomeCacheBlock. */
	        public int zPosition;
	        /** The last time this BiomeCacheBlock was accessed, in milliseconds. */
	        public long lastAccessTime;

	        public Block(int x, int z)
	        {
	            this.xPosition = x;
	            this.zPosition = z;
	            ClimateCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
	            ClimateCache.this.chunkManager.getClimateAt(this.biomes, x << 4, z << 4, 16, 16, false);
	        }

	        /**
	         * Returns the BiomeGenBase related to the x, z position from the cache block.
	         */
	        public Climate getClimateAt(int x, int z)
	        {
	            return this.biomes[x & 15 | (z & 15) << 4];
	        }
	    }
	}
	
	private static class DoubleCache
	{
		private IntArray array = new IntArray(2);
		private Map<IntArray, double[]> cache = new HashMap();
		
		public double cache(int x, int z, NoiseBasic noise)
		{
			array.array[0] = x >> 4;
			array.array[1] = z >> 4;
            double[] values = cache.get(array);
			if(values != null)
			{
				return values[(z & 0xF) << 4 | (x & 0xF)];
			}
			else
			{
				values = noise.noise(null, 16, 16, x >> 4, z >> 4);
				cache.put(array.copy(), values);
				return values[(z & 0xF) << 4 | (x & 0xF)];
			}
		}
		
		public void reset()
		{
			array = new IntArray(2);
			cache.clear();
		}
	}
}