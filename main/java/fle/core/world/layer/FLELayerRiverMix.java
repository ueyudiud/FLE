package fle.core.world.layer;

import fle.core.world.biome.FLEBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerRiverMix extends FLELayer
{
	private GenLayer riverLayer;
	
	public FLELayerRiverMix(GenLayer aLayer1, GenLayer aLayer2, long seed)
	{
		super(seed);
		parent = aLayer1;
		riverLayer = aLayer2;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int[] is0 = parent.getInts(x, z, w, h);
		int[] is1 = riverLayer.getInts(x, z, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		int a, u;
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
			{
				a = is0[i + w * j];
				u = is1[i + w * j];
				if(a == FLEBiome.ocean.biomeID || a == FLEBiome.frozenOcean.biomeID)
				{
					;
				}
				else if(u == 1)
				{
					a = FLEBiome.river_low.biomeID;
				}
				ret[i + w * j] = a;
			}
		return is0;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		riverLayer.initWorldGenSeed(seed);
	}
}