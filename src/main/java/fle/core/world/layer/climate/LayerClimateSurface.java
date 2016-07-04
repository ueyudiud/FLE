package fle.core.world.layer.climate;

import farcore.enums.EnumBiome;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoisePerlin;
import fle.core.world.climate.EnumClimate;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerClimateSurface extends LayerBase
{
	public NoiseBasic noise1 = new NoisePerlin(0L, 5, 12D, 2.7D, 0.5D);
	public NoiseBasic noise2 = new NoisePerlin(0L, 6, 24D, 1.9D, 0.6D);
	public NoiseBasic noise3 = new NoisePerlin(0L, 7, 24D, 1.9D, 0.6D);
	
	public LayerClimateSurface(long seed, GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] par = parent.getInts(x, y, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		double[] rainfall = noise2.noise(null, 16, 16, x, y);
		double[] temp = noise3.noise(null, 16, 16, x, y);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				if(par[id] == EnumBiome.ocean.id())
				{
					ret[id] = EnumClimate.ocean.id();
				}
				else if(par[id] == EnumBiome.ocean_deep.id())
				{
					ret[id] = EnumClimate.deep_ocean.id();
				}
				else if(par[id] == EnumBiome.continental_shelf.id())
				{
					ret[id] = EnumClimate.continental_shelf.id();
				}
				else
				{
					int t = (int) Math.floor(temp[id] * 12);
					int r = (int) Math.floor(rainfall[id] * 9);
					EnumClimate climate = EnumClimate.climateGraphEarth[t][r];
					if(climate.getAssociated().size() != 1)
 					{
						double value = noise1.noise(x + j, 0, y + i);						
						climate = climate.getAssociated().get((int) Math.floor(climate.getAssociated().size() * value));
 					}
					ret[id] = climate.id();
				}
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		seed = seed * 6364136223846793005L + 1442695040888963407L;
		noise1.setSeed(seed);
		seed = seed * 6364136223846793005L + 1442695040888963407L;
		noise2.setSeed(seed);
		seed = seed * 6364136223846793005L + 1442695040888963407L;
		noise3.setSeed(seed);
	}
}