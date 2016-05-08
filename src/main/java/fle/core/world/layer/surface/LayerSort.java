package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerSort extends LayerBase
{
	private GenLayer layer;
	
	public LayerSort(long seed, GenLayer floor, GenLayer layer)
	{
		super(seed);
		this.parent = floor;
		this.layer = layer;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] array1 = parent.getInts(x, y, w, h);
		int[] array2 = layer.getInts(x, y, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				int height = array2[id];
				int r = array1[id];
				if(r == EnumBiome.mid_mountain.id())
				{
					if(height == 1)
					{
						r = EnumBiome.valley.id();
					}
				}
				else if(r == EnumBiome.high_mountain.id())
				{
					if(height == 1)
					{
						r = EnumBiome.plateau.id();
					}
				}
				ret[id] = r;
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		layer.initWorldGenSeed(seed);
	}
}