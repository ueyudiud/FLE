package fle.core.world.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import fle.core.world.biome.FLEBiome;

public class FLELayerEdge extends FLELayer
{
	public FLELayerEdge(long seed, GenLayer aLayer)
	{
		super(seed);
		parent = aLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int x1 = x - 1;
		int z1 = z - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] mIs = parent.getInts(x1, z1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		int value1, value2, value3, value4, value5;
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				value1 = mIs[i + 1 + j * w1];
				value2 = mIs[i + (j + 1) * w1];
				value3 = mIs[i + 1 + (j + 2) * w1];
				value4 = mIs[i + 2 + (j + 1) * w1];
				value5 = mIs[i + 1 + (j + 1) * w1];
				if (value1 == value3 && value2 == value4)
                {
	                initChunkSeed((long)(x + i), (long)(z + j));
                    if (nextInt(2) == 0)
                    {
                        value5 = value2;
                    }
                    else
                    {
                        value5 = value3;
                    }
                }
                else
                {
                    if (value1 == value3)
                    {
                        value5 = value1;
                    }

                    if (value2 == value4)
                    {
                        value5 = value2;
                    }
                }
				int k = 0;
				if(value1 == value5) ++k;
				if(value2 == value5) ++k;
				if(value3 == value5) ++k;
				if(value4 == value5) ++k;
				int[] vs = new int[]{value1, value2, value3, value4};
				if(k < 4)
				{
	                initChunkSeed((long)(x + i), (long)(z + j));
	                if(value5 == FLEBiome.jungle.biomeID)
	                	value5 = FLEBiome.jungleEdge.biomeID;
	                if(value5 == FLEBiome.desert.biomeID)
	                	value5 = FLEBiome.wasteland.biomeID;
	                if(value5 == FLEBiome.desertHills.biomeID)
	                	value5 = FLEBiome.savannaPlateau.biomeID;
	                if(value5 == FLEBiome.extremeHills.biomeID)
	                	value5 = FLEBiome.extremeHillsEdge.biomeID;
	                if(value5 == FLEBiome.frozenOcean.biomeID)
	                {
	                	for(int v : vs)
		                	if(v != FLEBiome.frozenOcean.biomeID || v != FLEBiome.ocean.biomeID)
		                	{
		                		value5 = FLEBiome.frozenSlope.biomeID;
		                		break;
		                	}
	                }
	                if(value5 == FLEBiome.ocean.biomeID)
	                {
	                	for(int v : vs)
		                	if(v != FLEBiome.frozenOcean.biomeID || v != FLEBiome.ocean.biomeID)
		                	{
		                		value5 = FLEBiome.slope.biomeID;
		                		break;
		                	}
	                }
				}
                ret[i + j * w] = value5;
			}
		return ret;
	}
}