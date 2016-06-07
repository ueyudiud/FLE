package fle.cwg.world;

import static fle.core.world.layer.LayerBase.drawImage;
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
import fle.core.world.layer.LayerAdd;
import fle.core.world.layer.LayerAreaAdd;
import fle.core.world.layer.LayerBase;
import fle.core.world.layer.LayerFuzzyZoom;
import fle.core.world.layer.LayerLinkMainland;
import fle.core.world.layer.LayerRing;
import fle.core.world.layer.LayerStart;
import fle.core.world.layer.LayerZoom;
import fle.core.world.layer.surface.LayerBeach;
import fle.core.world.layer.surface.LayerFloor;
import fle.core.world.layer.surface.LayerRiver;
import fle.core.world.layer.surface.LayerSort;
import fle.core.world.layer.surface.LayerTerrainBase;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.IntCache;

public class FCWGSurfaceManager extends WorldChunkManager implements ICustomTempGenerate
{
	private double[] cacheRainfall;
	public FCWGWorldInfo info;
	
	public NoisePerlin tempNoise;
	public NoisePerlin rainfallNoise;
	
	protected DoubleCache tempCache = new DoubleCache();
	protected DoubleCache rainfallCache = new DoubleCache();
	
    protected GenLayer genBiomes;
    /** A GenLayer containing the indices into BiomeBase.biomeList[] */
    protected GenLayer biomeIndexLayer;
    /** The BiomeCache object for this world. */
    protected BiomeCache biomeCache;
    /** A list of biomes that the player can spawn in. */
    protected List<BiomeGenBase> biomesToSpawnIn;
    
	public FCWGSurfaceManager(World world, FCWGWorldInfo info)
	{
		super(world);

		Random random = new Random(world.getSeed());
		tempNoise = new NoisePerlin(random, info.tempNoiseOctave, info.tempNoiseSize, 3D, 2D);
		rainfallNoise = new NoisePerlin(random, info.rainfallNoiseOctave, info.rainfallNoiseSize, 2.6D, 2D);
		
        biomeCache = new BiomeCache(this);
        biomesToSpawnIn = new ArrayList();
        biomesToSpawnIn.add(EnumBiome.plain.biome());
        biomesToSpawnIn.add(EnumBiome.low_hill.biome());
        
        GenLayer[] agenlayer = wrapSuface(world.getSeed(), info);
        agenlayer = getModdedBiomeGenerators(world.getWorldInfo().getTerrainType(), world.getSeed(), agenlayer);
        genBiomes = agenlayer[0];
        biomeIndexLayer = agenlayer[1];
	}
	
	public static GenLayer[] wrapSuface(long seed, FCWGWorldInfo info2)
	{
		LayerBase orLayer = new LayerStart(info2.startLandChance, 1L);
		drawImage(256, orLayer, "layer1.0");
		orLayer = new LayerFuzzyZoom(3L, orLayer);
		drawImage(256, orLayer, "layer1.1");
		orLayer = new LayerAdd(1, info2.secondLandChance, 2L, orLayer);
		drawImage(256, orLayer, "layer1.2");
		orLayer = new LayerFuzzyZoom(3, 8L, orLayer);
		drawImage(256, orLayer, "layer1.3");
		orLayer = new LayerLinkMainland(3, 381L, orLayer);
		drawImage(256, orLayer, "layer2");
		orLayer = new LayerZoom(9031L, orLayer);
		orLayer = new LayerLinkMainland(1, 381L, orLayer);
		orLayer = new LayerZoom(5, 47L, orLayer);
		drawImage(256, orLayer, "layer3");
//		LayerTemp tempLayer = new LayerTemp(new NoisePerlin(1L, 8, 0.81D, 2D, 3D), 2839L);
		LayerTerrainBase terrainLayer = new LayerTerrainBase(new NoisePerlin(101L, info2.terrainNoiseIteration, info2.terrainNoiseSize, 0.48D, 1.8D), 38917L);
//		LayerRainfall rainfallLayer = new LayerRainfall(new NoisePerlin(201L, 7, 0.32D, 1.4D, 1.5D), 37292L, terrainLayer);
		orLayer = new LayerFloor(718L, orLayer, terrainLayer);
//		drawImage(256, orLayer, "layer4");
		if(info2.enableRiver)
		{
			LayerBase riverLayer = new LayerStart(9, 901L);
			drawImage(256, riverLayer, "layer4.0");
			riverLayer = new LayerFuzzyZoom(927L, riverLayer);
			riverLayer = new LayerAreaAdd(2, 30, 2.5F, 272L, riverLayer);
			drawImage(256, riverLayer, "layer4.1");
			riverLayer = new LayerFuzzyZoom(127L, riverLayer);
			riverLayer = new LayerAreaAdd(3, 25, 1.2F, 274L, riverLayer);
			drawImage(256, riverLayer, "layer4.2");
			riverLayer = new LayerFuzzyZoom(127L, riverLayer);
			riverLayer = new LayerAreaAdd(3, 20, 0.6F, 279L, riverLayer);
			drawImage(256, riverLayer, "layer4.3");
			riverLayer = new LayerFuzzyZoom(3, 127L, riverLayer);
			riverLayer = new LayerRing(2874L, riverLayer);
			drawImage(256, riverLayer, "layer4.4");
			orLayer = new LayerRiver(5819L, new LayerFuzzyZoom(2, 27L, orLayer), riverLayer);
		}
		else
		{
			orLayer = new LayerZoom(2, 27L, orLayer);
		}
		drawImage(256, orLayer, "layer4.5");
//		orLayer = new LayerEdge(508L, orLayer);
		orLayer = new LayerBeach(71628L, orLayer);
		drawImage(256, orLayer, "layer5");
		orLayer = new LayerZoom(3, 384918L, orLayer);
//		orLayer = new LayerSort(381L, orLayer, new LayerZoom(27048L, new LayerRing(381L, new LayerZoom(4, 283L, new LayerAdd(2, 3, 29L, new LayerZoom(2, 2L, new LayerStart(3, -2L)))))));
		drawImage(256, orLayer, "layer6");
		GenLayer out1 = new GenLayerSmooth(289471L, orLayer);
		GenLayer out2 = new GenLayerVoronoiZoom(94719L, out1);
		out2.initWorldGenSeed(seed);
		orLayer.setScale(1);
		return new GenLayer[]{out1, out2, out1};
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
        this.tempCache.reset();
        this.rainfallCache.reset();
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