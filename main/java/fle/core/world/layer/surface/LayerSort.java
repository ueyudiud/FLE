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
				if(r == EnumBiome.mountain_deciduous_forest.id())
				{
					if(height == 1)
					{
						r = EnumBiome.valley_tropic.id();
					}
				}
				else if(r == EnumBiome.mountain_meadow.id())
				{
					if(height == 1)
					{
						r = EnumBiome.valley_temperate.id();
					}
				}
				else if(r == EnumBiome.mountain_frigid.id())
				{
					if(height == 1)
					{
						r = EnumBiome.valley_frigid.id();
					}
				}
				else if(r == EnumBiome.mountain_snowy.id())
				{
					if(height == 1)
					{
						r = EnumBiome.grassland_plateau.id();
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