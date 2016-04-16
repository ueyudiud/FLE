package fle.core.world.layer.surface;

import farcore.enums.EnumTerrain;
import fle.core.world.layer.LayerNoise;
import net.minecraft.world.gen.layer.IntCache;

public class LayerRainfall extends LayerNoise
{
	private double[] cache;
	
	public LayerRainfall(int octave, long seed, LayerTerrainBase layer)
	{
		super(octave, seed);
		this.parent = layer;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] array = parent.getInts(x1, y1, w1, h1);
		if(cache == null || cache.length < w * h)
			cache = new double[w * h];
		cache = octaves.generateNoiseOctaves(cache, x, y, w, h, 8D, 8D, 8D);
		double mul = 1D / (double) (1 << (octave - 1));
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = w1 * (i + 1) + j + 1;
				double r1 = cache[i + j * h] * mul;
				int t1 = array[id];
				if(t1 == EnumTerrain.basin.ordinal())
				{
					r1 *= 0.85;
				}
				else
				{
					int t2 = array[id - w1];
					int t3 = array[id - 1];
					int t4 = array[id + 1];
					int t5 = array[id + w1];
					if(t2 > t1) r1 *= 0.85;
					if(t3 > t1) r1 *= 0.85;
					if(t4 > t1) r1 *= 0.85;
					if(t5 > t1) r1 *= 0.85;
				}
				ret[w * i + j] = (int) (t1 * 10);
			}
		return ret;
	}

	public LayerTerrainBase getTerrain()
	{
		return (LayerTerrainBase) parent;
	}
}