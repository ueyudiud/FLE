package fle.core.world.layer;

import java.lang.reflect.Array;
import java.util.Arrays;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerZoom1 extends FLELayer
{
	public FLELayerZoom1(long seed, GenLayer aLayer)
	{
		super(seed);
		parent = aLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int x1 = x >> 1;
		int z1 = z >> 1;
		int w1 = (w >> 1) + 2;
		int h1 = (h >> 1) + 2;
		int[] mIs = parent.getInts(x1, z1, w1, h1);
		int k0 = w1 - 1 << 1;
		int k1 = h1 - 1 << 1;
		int[] cache = new int[k0 * k1];
		int value1, value2, value3, value4, u, v, o;
		for(int i = 0; i < w1 - 1; ++i)
			for(int j = 0; j < h1 - 1; ++j)
			{
				initChunkSeed((long) (i + x) << 1, (long) (j + z) << 1);
				value1 = mIs[i + j * w1];
				value2 = mIs[(i + 1) + j * w1];
				value3 = mIs[i + (j + 1) * w1];
				value4 = mIs[i + 1 + (j + 1) * w1];
				u = i << 1;
				v = j << 1;
				o = (i * 2) + (j * 2) * k0;
				cache[o] = value1;
				cache[o + 1] = selectRandom(value1, value2);
				cache[o + k0] = selectRandom(value1, value3);
				cache[o + k0 + 1] = selectRandom(cache[o + 1], cache[o + k0]);
			}
		int[] ret = IntCache.getIntCache(w * h);
		for(int loop = 0; loop < h; ++loop)
			System.arraycopy(cache, loop * k0, ret, w * loop, w);
		return ret;
	}
}