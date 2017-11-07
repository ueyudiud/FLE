/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import static fargen.core.layer.surface.LayerSurfaceTerrain._beach1;
import static fargen.core.layer.surface.LayerSurfaceTerrain._beach2;
import static fargen.core.layer.surface.LayerSurfaceTerrain._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._river;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceSoilRemix extends Layer
{
	private GenLayer	soils;
	private GenLayer	terrain;
	
	public LayerSurfaceSoilRemix(long seed, GenLayer soils, GenLayer terrain)
	{
		super(seed);
		this.soils = soils;
		this.terrain = terrain;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		this.soils.initWorldGenSeed(seed);
		this.terrain.initWorldGenSeed(seed);
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ter = this.terrain.getInts(x, y, w, h);
		int[] ret = this.soils.getInts(x, y, w, h);
		for (int y1 = 0; y1 < h; ++y1)
			for (int x1 = 0; x1 < w; ++x1)
			{
				int id = y1 * w + x1;
				ret[id] = getSoil(ter[id], ret[id]);
			}
		return ret;
	}
	
	private int getSoil(int terrain, int soilType)
	{
		if (isOceanic(terrain) || isBeachic(terrain))
		{
			return Byte.MAX_VALUE;
		}
		// if (isRiver(terrain))
		// {
		// }
		return soilType;
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
	
	private boolean isRiver(int ter)
	{
		return ter == _river.biomeID;
	}
}
