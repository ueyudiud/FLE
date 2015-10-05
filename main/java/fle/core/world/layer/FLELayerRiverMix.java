package fle.core.world.layer;

import fle.core.world.biome.FLEBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class FLELayerRiverMix extends FLELayer
{
	private GenLayer riverLayer;
	
	public FLELayerRiverMix(GenLayer aLayer1, GenLayer aLayer2, long seed)
	{
		super(seed);
		parent = aLayer1;
		riverLayer = aLayer2;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int is0[] = parent.getInts(x - 1, z - 1, w + 2, w + 2);
		int is1[] = riverLayer.getInts(x - 1, z - 1, w + 1, h + 1);
		int ret[] = IntCache.getIntCache(w * h);
		int v1, v2, v3, v4, w1, w2, w3, w4, v5, w5;
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
			{
				int o = i + (w + 2) * j;
				v1 = is0[o + 1];
				v2 = is0[o + w + 2];
				v3 = is0[o + 1 + 2 * (w + 2)];
				v4 = is0[o + 2 + w + 2];
				v5 = is0[o + 1 + w + 2];
				w1 = is1[o + 1];
				w2 = is1[o + w + 2];
				w3 = is1[o + 1 + 2 * (w + 2)];
				w4 = is1[o + 2 + w + 2];
				w5 = is1[o + 1 + w + 2];
				int a = 0;
				if(v5 == FLEBiome.ocean.biomeID || v5 == FLEBiome.frozenOcean.biomeID)
					a = v5;
				else if(w5 == 1)
				{
					int k = 0;
					if(w1 == w2) ++k;
					if(w2 == w3) ++k;
					if(w3 == w4) ++k;
					if(w4 == w1) ++k;
					if(w1 == w3) ++k;
					if(w2 == w4) ++k;
					if(k >= 3)
					{
						a = v1 == v2 ? v1 : v2;
					}
					else
					{
						a = v5 == FLEBiome.iceMountains.biomeID || v5 == FLEBiome.icePlains.biomeID || 
								v5 == FLEBiome.coldBeach.biomeID || v5 == FLEBiome.coldTaiga.biomeID ||
								v5 == FLEBiome.coldTaigaHills.biomeID ? FLEBiome.frozenRiver.biomeID : FLEBiome.river.biomeID;
					}
				}
				else
					a = v5;
				ret[i + j * w] = a;
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		riverLayer.initWorldGenSeed(seed);
	}
}