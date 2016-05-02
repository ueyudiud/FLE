package fle.core.world.layer.surface;

import farcore.enums.EnumTemp;
import farcore.util.noise.NoiseBasic;
import fle.core.world.layer.LayerNoise;
import net.minecraft.world.gen.layer.IntCache;

public class LayerTemp extends LayerNoise
{
	private double[] cache;
	
	public LayerTemp(NoiseBasic noise, long seed)
	{
		super(noise, seed);
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		if(cache == null || cache.length < w * h)
			cache = new double[w * h];
		cache = noise.noise(cache, w, h, x, y);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				double v = cache[i * w + j];
				int k;
				if(v < 0.125)
				{
					k = EnumTemp.freeze.ordinal();
				}
				else if(v < 0.3)
				{
					k = EnumTemp.cold.ordinal();
				}
				else if(v < 0.45)
				{
					k = EnumTemp.cool.ordinal();
				}
				else if(v < 0.625)
				{
					k = EnumTemp.warm.ordinal();
				}
				else if(v < 0.8)
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