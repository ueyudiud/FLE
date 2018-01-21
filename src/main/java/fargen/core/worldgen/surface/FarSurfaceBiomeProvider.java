/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.worldgen.surface;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import fargen.api.terrain.Terrain;
import fargen.core.FarGenBiomes;
import fargen.core.biome.BiomeBase;
import fargen.core.instance.Layers;
import fargen.core.layer.surface.LayerSurfaceTerrain;
import fargen.core.util.DataCacheCoord;
import nebula.Log;
import nebula.common.util.L;
import nebula.common.world.IBiomeRegetter;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;

public class FarSurfaceBiomeProvider extends BiomeProvider implements IBiomeRegetter
{
	/** The biome cache. */
	protected final DataCacheCoord<BiomeBase> biomeCache;
	
	public final FarSurfaceDataGenerator	dataGenerator;
	protected final GenLayer[]				layers;
	// protected final BiomeCache biomeCache;
	
	public FarSurfaceBiomeProvider(WorldInfo info)
	{
		this.biomeCache = new DataCacheCoord<>((x, z) -> getBiomes(null, x, z, 16, 16, false), 4);
		this.dataGenerator = new FarSurfaceDataGenerator(this, info.getSeed());
		// this.biomeCache = new BiomeCache(this);
		allowedBiomes.add(FarGenBiomes.boreal_forest);
		allowedBiomes.add(FarGenBiomes.subtropical_broadleaf_forest);
		allowedBiomes.add(FarGenBiomes.subtropical_coniferous_forest);
		allowedBiomes.add(FarGenBiomes.sequoia_forest);
		allowedBiomes.add(FarGenBiomes.tropical_monsoon_forest);
		allowedBiomes.add(FarGenBiomes.tropical_rainforest);
		allowedBiomes.add(FarGenBiomes.tropical_thorny_forest);
		this.layers = Layers.wrapSurface(info.getSeed());
	}
	
	@Override
	public BiomeBase getBiome(BlockPos pos, Biome defaultBiome)
	{
		if (defaultBiome == Biomes.PLAINS)
		{
			defaultBiome = FarGenBiomes.grassland;
		}
		if (defaultBiome != null && !(defaultBiome instanceof BiomeBase))
		{
			Log.warn("The biome should be extended BiomeBase.", new IllegalStateException());
			defaultBiome = BiomeBase.DEBUG;
		}
		return this.biomeCache.getBodyOrDefault(pos.getX(), pos.getZ(), (BiomeBase) defaultBiome);
	}
	
	public BiomeBase getBiome(int x, int z)
	{
		return this.biomeCache.getBody(x, z);
	}
	
	/**
	 * Gets biomes to use for the blocks and loads the other data like
	 * temperature and humidity onto the WorldChunkManager.
	 */
	public int[] loadBlockGeneratorData(@Nullable int[] listToReuse, int x, int z, int width, int length)
	{
		IntCache.resetIntCache();
		
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new int[width * length];
		}
		
		int[] aint = this.layers[1].getInts(x, z, width, length);
		System.arraycopy(aint, 0, listToReuse, 0, width * length);
		Arrays.fill(listToReuse, 0);
		return listToReuse;
	}
	
	/**
	 * Gets biomes to use for the blocks and loads the other data like
	 * temperature and humidity onto the WorldChunkManager.
	 */
	@Override
	public BiomeBase[] getBiomes(Biome[] oldBiomeList, int x, int z, int width, int depth)
	{
		return getBiomes(oldBiomeList, x, z, width, depth, true);
	}
	
	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
	{
		if (biomes == null || biomes.length < width * height)
		{
			biomes = new BiomeBase[width * height];
		}
		return biomes;
	}
	
	/**
	 * Returns an array of biomes for the location input.
	 */
	public Terrain[] getTerrainForGeneration(Terrain[] terrains, int x, int z, int width, int height)
	{
		IntCache.resetIntCache();
		
		if (terrains == null || terrains.length < width * height)
		{
			terrains = new Terrain[width * height];
		}
		
		int[] aint = this.layers[0].getInts(x, z, width, height);
		
		for (int i = 0; i < width * height; ++i)
		{
			terrains[i] = LayerSurfaceTerrain.TERRAIN_TABLE[aint[i]];
		}
		
		return terrains;
	}
	
	@Override
	public BiomeBase[] getBiomes(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
	{
		if (listToReuse == null || !(listToReuse instanceof BiomeBase[]) || listToReuse.length < width * length)
		{
			listToReuse = new BiomeBase[width * length];
		}
		
		if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
		{
			System.arraycopy(this.biomeCache.get(x, z), 0, listToReuse, 0, width * length);
			return (BiomeBase[]) listToReuse;
		}
		else
		{
			IntCache.resetIntCache();
			
			int[] aint = this.layers[1].getInts(x, z, width, length);
			for (int i = 0; i < width * length; ++i)
			{
				listToReuse[i] = BiomeBase.getBiomeFromID(aint[i] & 0xFF);
			}
			
			return (BiomeBase[]) listToReuse;
		}
	}
	
	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
	{
		IntCache.resetIntCache();
		int i = x - radius >> 2;
			int j = z - radius >> 2;
		int k = x + radius >> 2;
		int l = z + radius >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		int[] aint = this.layers[1].getInts(i, j, i1, j1);
		try
		{
			for (int k1 = 0; k1 < i1 * j1; ++k1)
			{
				Biome biome = BiomeBase.getBiomeFromID(aint[k1] & 0xFF);
				if (!allowed.contains(biome)) return false;
			}
			return true;
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
			crashreportcategory.addCrashSection("Layer", this.layers[0].toString());
			crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			crashreportcategory.addCrashSection("z", Integer.valueOf(z));
			crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
			crashreportcategory.addCrashSection("allowed", allowed);
			throw new ReportedException(crashreport);
		}
	}
	
	@Override
	@Nullable
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
	{
		IntCache.resetIntCache();
		int r = (range >> 2);
		int[] aint = this.layers[1].getInts(x - r, z - r, 1 + (range >> 1), 1 + (range >> 1));
		int k1 = 1;
		BlockPos pos = null;
		for (int i = 0; i < r * r; ++i)
		{
			if (biomes.contains(getBiome(aint[i], null)) && L.nextInt(k1, random) == 0)
			{
				pos = new BlockPos(x + i % r, 0, z + i / r);
			}
		}
		return pos;
	}
	
	@Override
	public Biome getBiome(int saveID, @Nullable BlockPos pos)
	{
		return BiomeBase.getBiomeFromID(saveID);
	}
	
	@Override
	public void cleanupCache()
	{
		this.biomeCache.clean();
		this.dataGenerator.cleanCache();
	}
}
