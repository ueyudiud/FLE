package fle.core.world.layer.surface;

import farcore.enums.EnumTemp;
import fle.core.world.layer.LayerNoise;
import net.minecraft.world.gen.layer.IntCache;

public class LayerTemp extends LayerNoise
{
	private double[] cache;
	
	public LayerTemp(int octave, long seed)
	{
		super(octave, seed);
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		if(cache == null || cache.length < w * h)
			cache = new double[w * h];
		cache = octaves.generateNoiseOctaves(cache, x, y, w, h, 120D, 120D, 120D);
		double mul = 1D / (double) (1 << (octave - 1));
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				double v = cache[i + j * h] * mul;
				int k;
				if(v < -0.66)
				{
					k = EnumTemp.freeze.ordinal();
				}
				else if(v < -0.33)
				{
					k = EnumTemp.cold.ordinal();
				}
				else if(v < 0)
				{
					k = EnumTemp.cool.ordinal();
				}
				else if(v < 0.33)
				{
					k = EnumTemp.warm.ordinal();
				}
				else if(v < 0.67)
				{
					k = EnumTemp.hot.ordinal();
				}
				else
				{
					k = EnumTemp.blazing.ordinal();
				}
				ret[i * w + j] = k;
			}
		return ret;
	}
}