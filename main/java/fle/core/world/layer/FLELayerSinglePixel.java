package fle.core.world.layer;

import net.minecraft.world.gen.layer.IntCache;

public class FLELayerSinglePixel extends FLELayer
{
	boolean shouldGenDefault;
	long genChance;
	
	public FLELayerSinglePixel(boolean a, long g, long seed)
	{
		super(seed);
		genChance = g;
		shouldGenDefault = a;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				initChunkSeed((long) x + i, (long) z + j);
				ret[i + w * j] = nextLong(genChance) == 0L ? 1 : 0;
			}
		if(shouldGenDefault)
		{
			if(x > -w && x <= 0 && z > -h && z <= 0)
			{
				ret[-x + -z * w] = 1;
			}
		}
		return ret;
	}
}