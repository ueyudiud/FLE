package fle.core.world.layer;

import fle.core.world.biome.FLEBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerBeach extends FLELayer
{
	public FLELayerBeach(long seed, GenLayer aLayer)
	{
		super(seed);
		parent = aLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int[] is = parent.getInts(x - 1, z - 1, w + 2, h + 2);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int a0 = 0;
				int a1 = 0;
				int a2 = 0;
				for(int k = 0; k <= 2; ++k)
					for(int l = 0; l <= 2; ++l)
					{
						int value = is[(i + k) * (w + 2) + j + l];
						if(value == FLEBiome.slope.biomeID || value == FLEBiome.frozenSlope.biomeID || value == FLEBiome.ocean.biomeID || value == FLEBiome.frozenOcean.biomeID)
							++a0;
						if(value == FLEBiome.frozenSlope.biomeID || value == FLEBiome.icePlains.biomeID)
							++a1;
						if(value == FLEBiome.iceMountains.biomeID || value == FLEBiome.coldTaigaHills.biomeID || value == FLEBiome.extremeHills.biomeID)
							++a2;
					}
				ret[i * w + j] = a0 > 2 && a0 < 8 ? (a1 > 4 ? FLEBiome.coldBeach.biomeID : a2 > 3 ? FLEBiome.stoneBeach.biomeID : FLEBiome.beach.biomeID) : is[(i + 1) * (w + 2) + j + 1];
			}
		return ret;
	}

}
