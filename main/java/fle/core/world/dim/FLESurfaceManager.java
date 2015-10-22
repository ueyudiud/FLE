package fle.core.world.dim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import net.minecraft.world.gen.layer.IntCache;
import fle.core.world.biome.FLEBiome;
import fle.core.world.layer.FLELayer;

public class FLESurfaceManager extends WorldChunkManager
{
	protected World worldObj;
    protected BiomeCache biomeCache;
    protected List<BiomeGenBase> biomesToSpawnIn;
    protected long seed = 0L;
    protected WorldType type;
    protected GenLayer baseLayer;
    protected GenLayer lakeLayer;

	public FLESurfaceManager(World world)
	{
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		worldObj = world;
	}
	public FLESurfaceManager(long aSeed, WorldType aType)
	{
		this();
		seed = aSeed;
		type = aType;
		GenLayer layers[] = FLELayer.initializeAllBiomeGenerators(seed, type);
		lakeLayer = layers[0];
		baseLayer = layers[1];
	}
	public FLESurfaceManager()
	{
		biomeCache = new BiomeCache(this);
		biomesToSpawnIn = new ArrayList();
		
		biomesToSpawnIn.add(FLEBiome.warm_forest);
		biomesToSpawnIn.add(FLEBiome.warm_plains);
		biomesToSpawnIn.add(BiomeGenBase.jungle);
		biomesToSpawnIn.add(BiomeGenBase.jungleHills);
		biomesToSpawnIn.add(BiomeGenBase.taiga);
		biomesToSpawnIn.add(BiomeGenBase.taigaHills);
		biomesToSpawnIn.add(BiomeGenBase.desert);
	}
	
	@Override
	public List<BiomeGenBase> getBiomesToSpawnIn()
	{
		return biomesToSpawnIn;
	}
	
	@Override
	public float[] getRainfall(float[] array, int x,
			int z, int w, int h)
	{
		IntCache.resetIntCache();

        if (array == null || array.length < w * h)
        {
            array = new float[w * h];
        }

        int[] aint = baseLayer.getInts(x, z, w, h);

        for (int i1 = 0; i1 < w * h; ++i1)
        {
            try
            {
                float f = (float)BiomeGenBase.getBiome(aint[i1]).getIntRainfall() / 65536.0F;

                if (f > 1.0F)
                {
                    f = 1.0F;
                }

                array[i1] = f;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
                crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(array.length));
                crashreportcategory.addCrashSection("x", Integer.valueOf(x));
                crashreportcategory.addCrashSection("z", Integer.valueOf(z));
                crashreportcategory.addCrashSection("w", Integer.valueOf(w));
                crashreportcategory.addCrashSection("h", Integer.valueOf(h));
                throw new ReportedException(crashreport);
            }
        }

        return array;
	}
	
	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biome,
			int x, int z, int w, int h)
	{
		IntCache.resetIntCache();
		if ((biome == null) || (biome.length < w * h))
		{
			biome = new BiomeGenBase[w * h];
		}
		int[] var6 = lakeLayer.getInts(x, z, w, h);
		for (int var7 = 0; var7 < w * h; var7++)
		{
			biome[var7] = BiomeGenBase.getBiome(Math.max(var6[var7], 0));
		}
		return biome;
	}
	
	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biome,
			int x, int z, int w, int h,
			boolean flag)
	{
		if ((biome == null) || (biome.length < w * h))
		{
			biome = new BiomeGenBase[w * h];
		}
		if ((flag) && (w == 16) && (h == 16) && ((x & 0xF) == 0) && ((z & 0xF) == 0))
		{
			BiomeGenBase[] var9 = biomeCache.getCachedBiomes(x, z);
			System.arraycopy(var9, 0, biome, 0, w * h);
			return biome;
		}
		int[] var7 = baseLayer.getInts(x, z, w, h);
		for (int zCoord = 0; zCoord < w; zCoord++)
		{
			for (int xCoord = 0; xCoord < h; xCoord++)
			{
				int id = var7[(zCoord * w + xCoord)] != -1 ? var7[(zCoord * w + xCoord)] : 0;
				biome[(zCoord * w + xCoord)] = BiomeGenBase.getBiome(id);
			}
		}
		return biome;
	}
		  
	@Override
	public boolean areBiomesViable(int x, int y,
			int z, List aList)
	{
		IntCache.resetIntCache();
        int l = x - z >> 2;
        int i1 = y - z >> 2;
        int j1 = x + z >> 2;
        int k1 = y + z >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] is = lakeLayer.getInts(l, i1, l1, i2);

        try
        {
            for (int j2 = 0; j2 < l1 * i2; ++j2)
            {
                BiomeGenBase biomegenbase = BiomeGenBase.getBiome(is[j2]);

                if (!biomesToSpawnIn.contains(biomegenbase))
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
            crashreportcategory.addCrashSection("Layer", lakeLayer.toString());
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(y));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
            throw new ReportedException(crashreport);
        }
	}
	
	@Override
	public ChunkPosition findBiomePosition(int x, int z,
			int radius, List aList, Random rand)
	{
	    IntCache.resetIntCache();
	    int l = x - radius >> 2;
	    int i1 = z - radius >> 2;
	    int j1 = x + radius >> 2;
	    int k1 = z + radius >> 2;
	    int l1 = j1 - l + 1;
	    int i2 = k1 - i1 + 1;
	    int[] aint = baseLayer.getInts(l, i1, l1, i2);
	    ChunkPosition chunkposition = null;
	    int j2 = 0;
	    for (int k2 = 0; k2 < l1 * i2; k2++)
	    {
	    	int l2 = l + k2 % l1 << 2;
	    	int i3 = i1 + k2 / l1 << 2;
	    	BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);
	    	if ((aList.contains(biomegenbase)) && ((chunkposition == null) || (rand.nextInt(j2 + 1) == 0)))
	    	{
	    		chunkposition = new ChunkPosition(l2, 0, i3);
	    		j2++;
	    	}
	    }
	    return chunkposition;
	}
	
	@Override
	public BiomeGenBase getBiomeGenAt(int x, int z)
	{
		BiomeGenBase biome = biomeCache.getBiomeGenAt(x, z);
		return biome;
	}
	
	@Override
	public void cleanupCache()
	{
		biomeCache.cleanupCache();
	}
}