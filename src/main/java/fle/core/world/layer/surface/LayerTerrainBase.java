package fle.core.world.layer.surface;

import java.util.Random;

import farcore.enums.EnumTerrain;
import farcore.util.U;
import farcore.util.noise.NoiseBasic;
import fle.core.world.layer.LayerNoise;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.layer.IntCache;

public class LayerTerrainBase extends LayerNoise
{
	private double[] cache;
	
	public LayerTerrainBase(NoiseBasic noise, long seed)
	{
		super(noise, seed);
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		if(cache == null || cache.length < w1 * h1)
			cache = new double[w1 * h1];
		cache = noise.noise(cache, w1, h1, x1, y1);
		int[] array = IntCache.getIntCache(w1 * h1);
		for(int i = 0; i < w1; ++i)
			for(int j = 0; j < h1; ++j)
			{
				array[j * w1 + i] = EnumTerrain.get((float) (cache[j * w1 + i] )).ordinal();
			}
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int i1 = array[(i + 1) * w1 + j + 1];
				int i2 = array[(i + 2) * w1 + j + 1];
				int i3 = array[(i + 1) * w1 + j + 2];
				int i4 = array[i * w1 + j + 1];
				int i5 = array[(i + 1) * w1 + j];
				if(i1 == EnumTerrain.plain.ordinal())
				{
					if(U.Lang.min(i2, i3, i4, i5) > EnumTerrain.plain.ordinal())
					{
						i1 = EnumTerrain.basin.ordinal();
					}
				}
				else if(i1 == EnumTerrain.ex_mountain.ordinal())
				{
					if(U.Lang.min(i2, i3, i4, i5) < EnumTerrain.plain.ordinal())
					{
						i1 = EnumTerrain.plateau.ordinal();
					}
				}
				ret[i * w + j] = i1;
			}
		return ret;
	}
}