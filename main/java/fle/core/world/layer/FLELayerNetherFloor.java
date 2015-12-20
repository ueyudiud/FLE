package fle.core.world.layer;

import net.minecraft.world.gen.layer.IntCache;
import fle.core.util.noise.NoiseBase;
import fle.core.world.biome.FLEBiome;

public class FLELayerNetherFloor extends FLELayer
{
	NoiseBase temNoise;
	NoiseBase hfNoise;

	public FLELayerNetherFloor(NoiseBase aNoise1, NoiseBase aNoise2, long seed)
	{
		super(seed);
		temNoise = aNoise1;
		hfNoise = aNoise2;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		double[] temValues = temNoise.noise(new double[w * h], x, z, w, h);
		double[] hfValues = hfNoise.noise(new double[w * h], x, z, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				initChunkSeed(x + i, z + j);
				int k0 = i + j * w;
				double v1 = (temValues[k0] + 1D) * 0.5D;
				double v2 = (hfValues[k0]+ 1D) * 0.5D * (1D - Math.sqrt(v1));
				if(v1 > 0.8F)
				{
					ret[k0] = FLEBiome.hell_lava_sea.biomeID;
					continue;
				}
				else if(v1 > 0.6F)
				{
					ret[k0] = FLEBiome.hell.biomeID;
					continue;
				}
				else if(v1 > 0.4F)
				{
					if(v2 > 0.125F) ret[k0] = FLEBiome.hell_crystalland.biomeID;
					else ret[k0] = FLEBiome.hell.biomeID;
				}
				else if(v1 > 0.2F)
				{
					if(v2 > 0.2F) ret[k0] = FLEBiome.hell_crystalland.biomeID;
					else ret[k0] = FLEBiome.hell.biomeID;
				}
				else
				{
					if(v2 > 0.3F) ret[k0] = FLEBiome.hell_crystalland.biomeID;
					else ret[k0] = FLEBiome.hell.biomeID;
				}
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		temNoise.setSeed(seed * 671947103L + 8668391L);
		hfNoise.setSeed(seed * 29419410L + 39528021L);
	}
}