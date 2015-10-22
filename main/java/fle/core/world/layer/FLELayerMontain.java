package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import fle.core.util.noise.NoiseBase;

public class FLELayerMontain extends FLELayer
{
	final NoiseBase noise;
	
	public FLELayerMontain(long seed, GenLayer aLayer, NoiseBase aNoise)
	{
		super(seed);
		parent = aLayer;
		noise = aNoise;
		noise.setSeed(seed);
	}

	private static final float a = 1.0F / 17.0F;
	
	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		double[] ns = noise.noise(new double[w * h], x, z, w, h);
		int[] is = parent.getInts(x - 1, z - 1, w + 2, h + 2);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				int index = i + w * j;
				float height = 0;
				int a = is[i + 1 + (w + 2) * j];
				for(int k : a(w + 2))
				{
					if(is[(i + 1 + (w + 2) * (j + 1)) + k] == 0) height -= this.a;
					else height += this.a;
				}
				double h1 = (ns[index] + 1.0D) * height;
				if(h1 > 0.8F) a = 4;
				else if(h1 > 0.7F) a = 3;
				else if(h1 > 0.5F) a = 2;
				else if(h1 > 0.0F) a = 1;
				else a = is[i + 1 + (w + 2) * (j + 1)];
				ret[index] = a;
			}
		return ret;
	}
	
	private int[] a(int w)
	{
		return new int[]{0, 1, 1 + w, w, w - 1, -1, -1 - w, -w, -w + 1};
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		noise.setSeed(seed * 2859181L + 859581910L);
		super.initWorldGenSeed(seed);
	}
}