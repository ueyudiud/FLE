/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import static fargen.core.FarGenBiomes.beach;
import static fargen.core.FarGenBiomes.ocean_f;
import static fargen.core.FarGenBiomes.ocean_f_deep;
import static fargen.core.FarGenBiomes.ocean_sf;
import static fargen.core.FarGenBiomes.ocean_sf_deep;
import static fargen.core.FarGenBiomes.ocean_st;
import static fargen.core.FarGenBiomes.ocean_st_deep;
import static fargen.core.FarGenBiomes.ocean_t;
import static fargen.core.FarGenBiomes.ocean_t_deep;
import static fargen.core.FarGenBiomes.ocean_te;
import static fargen.core.FarGenBiomes.ocean_te_deep;
import static fargen.core.FarGenBiomes.river;
import static fargen.core.layer.surface.LayerSurfaceTerrain._beach1;
import static fargen.core.layer.surface.LayerSurfaceTerrain._beach2;
import static fargen.core.layer.surface.LayerSurfaceTerrain._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._river;

import fargen.core.biome.BiomeBase;
import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiomeRemix extends Layer
{
	private GenLayer biomes;
	private GenLayer terrain;
	
	public LayerSurfaceBiomeRemix(long seed, GenLayer biomes, GenLayer terrain)
	{
		super(seed);
		this.biomes = biomes;
		this.terrain = terrain;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		this.biomes.initWorldGenSeed(seed);
		this.terrain.initWorldGenSeed(seed);
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ter = this.terrain.getInts(x, y, w, h);
		int[] ret = this.biomes.getInts(x, y, w, h);
		for (int y1 = 0; y1 < h; ++y1)
			for (int x1 = 0; x1 < w; ++x1)
			{
				int id = y1 * w + x1;
				ret[id] = getBiome(ter[id], ret[id]);
			}
		return ret;
	}
	
	private int getBiome(int terrain, int biome)
	{
		if (isOceanic(terrain))
		{
			return getOceanicBiome(biome, isDeepOcean(terrain));
		}
		if (isRiver(terrain))
		{
			return getRiverBiome(biome);
		}
		if (isBeachic(terrain))
		{
			return getBeachBiome(biome);
		}
		return biome;
	}
	
	private boolean isOceanic(int ter)
	{
		return ter == _ocean.biomeID || ter == _deep_ocean.biomeID;
	}
	
	private boolean isBeachic(int ter)
	{
		return ter == _beach1.biomeID || ter == _beach2.biomeID;
	}
	
	private boolean isDeepOcean(int ter)
	{
		return ter == _deep_ocean.biomeID;
	}
	
	private int getOceanicBiome(int biome, boolean flag)
	{
		BiomeBase b = BiomeBase.getBiomeFromID(biome);
		if (b.isOcean()) return biome;
		switch (b.zone.category1)
		{
		case TROPICAL : return (flag ? ocean_t_deep : ocean_t).biomeID;
		case SUBTROPICAL : return (flag ? ocean_st_deep : ocean_st).biomeID;
		default:
		case TEMPERATE : return (flag ? ocean_te_deep : ocean_te).biomeID;
		case SUBFRIGID : return (flag ? ocean_sf_deep : ocean_sf).biomeID;
		case FRIGID : return (flag ? ocean_f_deep : ocean_f).biomeID;
		}
	}
	
	private boolean isRiver(int ter)
	{
		return ter == _river.biomeID;
	}
	
	private int getRiverBiome(int biome)
	{
		BiomeBase b = BiomeBase.getBiomeFromID(biome);
		return river[b.zone.ordinal()].biomeID;
	}
	
	private int getBeachBiome(int biome)
	{
		BiomeBase b = BiomeBase.getBiomeFromID(biome);
		return beach[b.zone.ordinal()].biomeID;
	}
}