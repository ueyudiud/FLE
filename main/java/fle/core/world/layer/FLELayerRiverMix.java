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
		int[] is0 = parent.getInts(x, z, w, h);
		int[] is1 = riverLayer.getInts(x, z, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		int a, v, u;
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
			{
				a = v = is0[i + w * j];
				u = is1[i + w * j];
				if(v == FLEBiome.ocean.biomeID || v == FLEBiome.frozenOcean.biomeID)
				{
					a = v;
				}
				else if(u == 1)
				{
					a = v == FLEBiome.iceMountains.biomeID || v == FLEBiome.icePlains.biomeID || 
							v == FLEBiome.coldBeach.biomeID || v == FLEBiome.coldTaiga.biomeID ||
							v == FLEBiome.coldTaigaHills.biomeID ? FLEBiome.frozenRiver.biomeID : 
								v == FLEBiome.extremeHills.biomeID || v == FLEBiome.savannaPlateau.biomeID ? FLEBiome.river_high.biomeID :
									v == FLEBiome.hill.biomeID || v == FLEBiome.forestHills.biomeID || 
									v == FLEBiome.desertHills.biomeID || v == FLEBiome.roofedForest_hill.biomeID ||
									v == FLEBiome.jungleHills.biomeID || v == FLEBiome.taigaHills.biomeID ? 
											FLEBiome.river_mid.biomeID : FLEBiome.river_low.biomeID;
				}
				ret[i + j * w] = a;
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		riverLayer.initWorldGenSeed((seed + 28591741L) * seed + 385917519L);
	}
}