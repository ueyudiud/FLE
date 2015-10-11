package fle.core.world.layer;

import fle.core.world.biome.FLEBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerRiver extends FLELayer
{
	public FLELayerRiver(int aTime, long seed)
	{
		super(seed);
		GenLayer mLayer = new FLELayerSinglePixel(false, 18L, seed);
		for(int i = 1; i < aTime; ++i)
		{
			mLayer = new FLELayerZoom(seed + 1, mLayer);
		}
		parent = mLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int w1 = w + 2;
		int[] is = parent.getInts(x - 1, z - 1, w1, h + 2);
		int[] ret = IntCache.getIntCache(w * h);
		int v1, v2, v3, v4, v5, v6, v7, v8, v9;
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				int o = i + w1 * j;
				v1 = is[o];
				v2 = is[o + 1];
				v3 = is[o + 2];
				v4 = is[o + w1];
				v5 = is[o + 1 + w1];
				v6 = is[o + 2 + w1];
				v7 = is[o + 2 * w1];
				v8 = is[o + 1 + 2 * w1];
				v9 = is[o + 2 + 2 * w1];
				int k0 = 0;
				initChunkSeed(x + i, z + j);
				if(v2 != v5 || v4 != v5 || v6 != v5 || v8 != v5) ret[i + w * j] = 1;
				else if(v1 != v2 || v1 != v4 || v3 != v2 || v3 != v6 ||
						v7 != v4 || v7 != v8 || v9 != v8 || v9 != v6) ret[i + w * j] = nextInt(8) < 6 ? FLEBiome.river.biomeID : 0;
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		parent.initWorldGenSeed(seed * 19382491L);
	}
}