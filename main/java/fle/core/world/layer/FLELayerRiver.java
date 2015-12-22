package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerRiver extends FLELayer
{
	public FLELayerRiver(int time, long seed)
	{
		super(seed);
		GenLayer mLayer = new FLELayerSinglePixel(false, 9, seed);
		int i;
		for(i = 1; i < time / 2; ++i)
		{
			mLayer = new GenLayerZoom(i * 294117L, mLayer);
			mLayer = new FLELayerAddPixel(36, mLayer, 2, 39284L + i * 385081041L);
		}
		for(; i < time; ++i)
		{
			mLayer = new GenLayerZoom(i * 294143L, mLayer);
		}
		parent = mLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int w1 = w + 2;
		int h1 = h + 2;
		int[] is = parent.getInts(x - 1, z - 1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		int[] vs;
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				int o = i + w1 * j;
				vs = new int[]{
					is[o],
					is[o + 1],
					is[o + 2],
					is[o +     w1],
					is[o + 1 + w1],
					is[o + 2 + w1],
					is[o +     2 * w1],
					is[o + 1 + 2 * w1],
					is[o + 2 + 2 * w1]
				};
				
				int c = 0;
				for(int a0 = 0; a0 < vs.length; ++a0)
					for(int a1 = 0; a1 < a0; ++a1)
					{
						if(vs[a0] != vs[a1]) ++c;
					}
				if(c > 6) ret[i + w * j] = 1;
			}
		return ret;
	}
}