package fargen.core.worldgen.surface;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.lib.world.IBiomeRegetter;
import fargen.core.FarGenBiomes;
import fargen.core.biome.BiomeBase;
import fargen.core.instance.Layers;
import fargen.core.util.LayerProp;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;

/**
 * Now just for debugging.
 * @author ueyudiud
 *
 */
public class FarSurfaceBiomeProvider extends BiomeProvider implements IBiomeRegetter
{
	/** The biome list. */
	protected final BiomeCache biomeCache;
	public final LayerProp layers;

	public FarSurfaceBiomeProvider(WorldInfo info)
	{
		biomeCache = new BiomeCache(this);
		allowedBiomes.add(FarGenBiomes.subtropical_broadleaf_forest);
		allowedBiomes.add(FarGenBiomes.subtropical_coniferous_forest);
		allowedBiomes.add(FarGenBiomes.savanna);
		allowedBiomes.add(FarGenBiomes.tropical_monsoon_forest);
		allowedBiomes.add(FarGenBiomes.tropical_rainforest);
		allowedBiomes.add(FarGenBiomes.tropical_thorny_forest);
		allowedBiomes.add(FarGenBiomes.temperate_broadleaf_forest);
		layers = Layers.wrapSurface(info.getSeed());
	}
	
	@Override
	public Biome getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn)
	{
		return biomeCache.getBiome(pos.getX(), pos.getZ(), biomeGenBaseIn);
	}
	
	/**
	 * Gets biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager.
	 */
	public int[] loadBlockGeneratorData(@Nullable int[] listToReuse, int x, int z, int width, int length)
	{
		IntCache.resetIntCache();
		
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new int[width * length];
		}
		
		int[] aint = layers.biomeLayer2.getInts(x, z, width, length);
		System.arraycopy(aint, 0, listToReuse, 0, width * length);
		return listToReuse;
	}

	/**
	 * Gets biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager.
	 */
	@Override
	public Biome[] loadBlockGeneratorData(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
	{
		return getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
	{
		IntCache.resetIntCache();

		if (biomes == null || biomes.length < width * height)
		{
			biomes = new Biome[width * height];
		}

		int[] aint = layers.biomeLayer1.getInts(x, z, width, height);

		try
		{
			for (int i = 0; i < width * height; ++i)
			{
				biomes[i] = BiomeBase.getBiomeFromID(aint[i] & 0xFF);
			}

			return biomes;
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
			crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
			crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			crashreportcategory.addCrashSection("z", Integer.valueOf(z));
			crashreportcategory.addCrashSection("w", Integer.valueOf(width));
			crashreportcategory.addCrashSection("h", Integer.valueOf(height));
			throw new ReportedException(crashreport);
		}
	}

	/**
	 * Gets a list of biomes for the specified blocks.
	 */
	@Override
	public Biome[] getBiomeGenAt(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
	{
		IntCache.resetIntCache();
		
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new Biome[width * length];
		}
		
		if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
		{
			Biome[] abiome = biomeCache.getCachedBiomes(x, z);
			System.arraycopy(abiome, 0, listToReuse, 0, width * length);
			return listToReuse;
		}
		else
		{
			int[] aint = layers.biomeLayer2.getInts(x, z, width, length);
			
			for (int i = 0; i < width * length; ++i)
			{
				listToReuse[i] = BiomeBase.getBiomeFromID(aint[i] & 0xFF);
			}
			
			return listToReuse;
		}
	}
	
	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
	{
		IntCache.resetIntCache();
		int i = x - radius >> 2; int j = z - radius >> 2; int k = x + radius >> 2;
		int l = z + radius >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		int[] aint = layers.biomeLayer1.getInts(i, j, i1, j1);
		try
		{
			for (int k1 = 0; k1 < i1 * j1; ++k1)
			{
				Biome biome = BiomeBase.getBiomeFromID(aint[k1] & 0xFF);
				if (!allowed.contains(biome))
					return false;
			}
			return true;
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
			crashreportcategory.addCrashSection("Layer", layers.biomeLayer1.toString());
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
		int i = x - range >> 2;	int j = z - range >> 2;
		int k = x + range >> 2;
		int l = z + range >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		int[] aint = layers.biomeLayer1.getInts(i, j, i1, j1);
		BlockPos blockpos = null;
		int k1 = 0;

		for (int l1 = 0; l1 < i1 * j1; ++l1)
		{
			int i2 = i + l1 % i1 << 2;
			int j2 = j + l1 / i1 << 2;
			Biome biome = BiomeBase.getBiomeFromID(aint[l1] & 0xFF);

			if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0))
			{
				blockpos = new BlockPos(i2, 0, j2);
				++k1;
			}
		}

		return blockpos;
	}

	@Override
	public Biome getBiome(int saveID, BlockPos pos)
	{
		return BiomeBase.getBiomeFromID(saveID);
	}

	@Override
	public void cleanupCache()
	{
		biomeCache.cleanupCache();
	}
}