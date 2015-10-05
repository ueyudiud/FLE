package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;

public class FLELayerFuzzyZoom extends FLELayer
{
	public FLELayerFuzzyZoom(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int x1 = x >> 1;
		int z1 = z >> 1;
		int w1 = (w >> 1) + 3;
		int h1 = (h >> 1) + 3;
		int[] is0 = parent.getInts(x1, z1, w1, h1);
		int[] is1 = new int[w1 * h1 * 4];
		int w2 = w1 << 1;
		for (int loop = 0; loop < h1 - 1; loop++)
	    {
			int var13 = loop << 1;
			int var14 = var13 * w2;
			int var15 = is0[(0 + (loop + 0) * w1)];
			int var16 = is0[(0 + (loop + 1) * w1)];
			for (int var17 = 0; var17 < w1 - 1; var17++)
			{
				initChunkSeed(var17 + x1 << 1, loop + z1 << 1);
				int var18 = is0[(var17 + 1 + (loop + 0) * w1)];
				int var19 = is0[(var17 + 1 + (loop + 1) * w1)];
				is1[var14] = var15;
				is1[(var14++ + w2)] = choose(var15, var16);
				is1[var14] = choose(var15, var18);
				is1[(var14++ + w2)] = choose(var15, var18, var16, var19);
				var15 = var18;
				var16 = var19;
			}
	    }
	    int[] ret = new int[w * h];
	    for (int var13 = 0; var13 < h; var13++)
	    {
	    	System.arraycopy(is1, (var13 + (z & 0x1)) * (w1 << 1) + (x & 0x1), ret, var13 * w, w);
	    }
	    return ret;
	}
}