package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import fle.core.util.noise.NoiseBase;
import fle.core.world.biome.FLEBiome;

public class FLELayerFloor extends FLELayer
{
	final NoiseBase noise;
	final NoiseBase noise1;
	
	public FLELayerFloor(NoiseBase aNoise1, NoiseBase aNoise2, GenLayer aLayer, long seed)
	{
		super(seed);
		noise = aNoise1.setSeed(seed);
		noise1 = aNoise2.setSeed(seed);
		parent = aLayer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		double[] values = noise.noise(new double[w * h], x, z, w, h);
		double[] values1 = noise1.noise(new double[w * h], x, z, w, h);
		int[] value2 = parent.getInts(x, z, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				initChunkSeed(x + i, z + j);
				int k0 = i + j * w;
				int bioID = value2[k0];
				double v1 = (values[k0] + 1D) * 0.5D;
				double v2 = (values1[k0]+ 1D) * 0.5D;
				if(bioID == FLEBiome.ocean.biomeID)
				{
					ret[k0] = v2 > 0.1F ? FLEBiome.ocean.biomeID : FLEBiome.frozenOcean.biomeID;
					continue;
				}
				if(v1 > 0.8F)
				{
					if(bioID == 1)
					{
						if(v2 > 0.8F) ret[k0] = FLEBiome.jungle.biomeID;
						else if(v2 > 0.5F) ret[k0] = FLEBiome.warm_forest.biomeID;
						else if(v2 > 0.2F) ret[k0] = FLEBiome.savanna.biomeID;
						else if(v2 > 0.1F) ret[k0] = FLEBiome.wasteland.biomeID;
						else ret[k0] = FLEBiome.desert.biomeID;
					}
					else if(bioID == 2)
					{
						if(v2 > 0.78F) ret[k0] = FLEBiome.jungleHills.biomeID;
						else if(v2 > 0.52F) ret[k0] = FLEBiome.forestHills.biomeID;
						else if(v2 > 0.26F) ret[k0] = FLEBiome.savanna.biomeID;
						else ret[k0] = FLEBiome.desertHills.biomeID;
					}
					else
					{
						if(v2 > 0.58F) ret[k0] = FLEBiome.forestHills.biomeID;
						else if(v2 > 0.28F) ret[k0] = FLEBiome.savannaPlateau.biomeID;
						else ret[k0] = FLEBiome.savannaPlateau.biomeID;
					}
					continue;
				}
				else if(v1 > 0.6F)
				{
					if(bioID == 1)
					{
						if(v2 > 0.8F) ret[k0] = FLEBiome.swampland.biomeID;
						else if(v2 > 0.6F) ret[k0] = FLEBiome.roofedForest.biomeID;
						else if(v2 > 0.4F) ret[k0] = FLEBiome.mid_forest.biomeID;
						else if(v2 > 0.12F) ret[k0] = FLEBiome.warm_plains.biomeID;
						else ret[k0] = FLEBiome.desert.biomeID;
					}
					else if(bioID == 2)
					{
						if(v2 > 0.7F) ret[k0] = FLEBiome.roofedForest_hill.biomeID;
						else if(v2 > 0.6F) ret[k0] = FLEBiome.forestHills.biomeID;
						else if(v2 > 0.1F) ret[k0] = FLEBiome.hill.biomeID;
						else ret[k0] = FLEBiome.desertHills.biomeID;
					}
					else if(bioID == 3)
					{
						if(v2 > 0.64F) ret[k0] = FLEBiome.forestHills.biomeID;
						else ret[k0] = FLEBiome.hill.biomeID;
					}
					else if(bioID == 4)
					{
						if(v2 > 0.7F) ret[k0] = FLEBiome.forestHills.biomeID;
						else ret[k0] = FLEBiome.hill.biomeID;
					}
				}
				else if(v1 > 0.4F)
				{
					if(bioID == 1)
					{
						if(v2 > 0.6F) ret[k0] = FLEBiome.mid_forest.biomeID;
						else if(v2 > 0.4F) ret[k0] = FLEBiome.warm_plains.biomeID;
						else ret[k0] = FLEBiome.desert.biomeID;
					}
					else if(bioID == 2)
					{
						if(v2 > 0.64F) ret[k0] = FLEBiome.forestHills.biomeID;
						else ret[k0] = FLEBiome.plains.biomeID;
					}
					else if(bioID == 3)
					{
						if(v2 > 0.72F) ret[k0] = FLEBiome.forestHills.biomeID;
						else ret[k0] = FLEBiome.hill.biomeID;
					}
					else if(bioID == 4)
					{
						ret[k0] = FLEBiome.hill.biomeID;
					}
				}
				else if(v1 > 0.2F)
				{
					if(bioID == 1)
					{
						if(v2 > 0.6F) ret[k0] = FLEBiome.megaTaiga.biomeID;
						else ret[k0] = FLEBiome.taiga.biomeID;
					}
					else if(bioID == 2)
					{
						if(v2 > 0.6F) ret[k0] = FLEBiome.megaTaigaHills.biomeID;
						else ret[k0] = FLEBiome.taigaHills.biomeID;
					}
					else if(bioID == 3)
					{
						if(v2 > 0.62F) ret[k0] = FLEBiome.megaTaigaHills.biomeID;
						else ret[k0] = FLEBiome.coldTaigaHills.biomeID;
					}
					else if(bioID == 4)
					{
						if(v2 > 0.64F) ret[k0] = FLEBiome.taigaHills.biomeID;
						else ret[k0] = FLEBiome.extremeHills.biomeID;
					}
				}
				else
				{
					if(bioID == 1)
					{
						if(v2 > 0.75F) ret[k0] = FLEBiome.coldTaiga.biomeID;
						else ret[k0] = FLEBiome.icePlains.biomeID;
					}
					else if(bioID == 2)
					{
						if(v2 > 0.8F) ret[k0] = FLEBiome.coldTaigaHills.biomeID;
						else ret[k0] = FLEBiome.coldTaigaHills.biomeID;
					}
					else
					{
						ret[k0] = FLEBiome.iceMountains.biomeID;
					}
				}
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		noise.setSeed(seed * 671947103L + 8668391L);
		noise1.setSeed(seed * 29419410L + 39528021L);
	}
}