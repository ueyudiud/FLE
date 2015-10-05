package fle.core.world.layer;

import net.minecraft.world.gen.layer.IntCache;

public class FLELayerIsland extends FLELayer
{
	public FLELayerIsland(long seed)
	{
		super(seed);
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int[] mIs = IntCache.getIntCache(w * h);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				initChunkSeed((long) x + w, (long) z + h);
				mIs[i + w * j] = nextLong(6) == 0 ? 1 : 0;
			}
		if(x > -w && x <= 0 && z > -h && z <= 0)
		{
			mIs[-x + -z * w] = 1;
		}
		return mIs;
	}
}