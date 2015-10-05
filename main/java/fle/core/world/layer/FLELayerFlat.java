package fle.core.world.layer;

import java.util.Arrays;

import net.minecraft.world.gen.layer.IntCache;

public class FLELayerFlat extends FLELayer
{
	public FLELayerFlat()
	{
		super(0L);
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);
		Arrays.fill(ret, 1);
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed) {}
}