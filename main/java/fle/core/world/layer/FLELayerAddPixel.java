package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerAddPixel extends FLELayer
{
	private long genChance;
	private int defaultValue;
	
	public FLELayerAddPixel(long g, GenLayer aLayer, int d, long seed)
	{
		super(seed);
		genChance = g;
		defaultValue = d;
		parent = aLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int x1 = x - 1;
		int z1 = z - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] aIs = parent.getInts(x1, z1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		int value1, value2, value3, value4;
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				if(aIs[i + 1 + (j + 1) * w1] != 0)
				{
					ret[i + j * w] = aIs[i + 1 + (j + 1) * w1];
					continue;
				}
				initChunkSeed((long) x + i, (long) z + j);
				if((aIs[i + 1 + j * w1] | aIs[i + (j + 1) * w1] | aIs[i + 2 + (j + 1) * w1] | aIs[i + 1 + (j + 2) * w1]) != 0)
					ret[i + j * w] = selectRandom(aIs[i + 1 + j * w1], aIs[i + (j + 1) * w1], aIs[i + 2 + (j + 1) * w1], aIs[i + 1 + (j + 2) * w1]);
				else
					ret[i + j * w] = nextLong(genChance) == 0 ? defaultValue : 0;
			}
		return ret;
	}
}