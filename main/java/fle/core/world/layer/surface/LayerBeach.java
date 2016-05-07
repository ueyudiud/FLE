package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerBeach extends LayerBase
{
	public LayerBeach(long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] par = parent.getInts(x1, y1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int i1 = par[(i + 1) * w1 + j + 1],
						i2 = par[(i + 2) * w1 + j + 1],
						i3 = par[(i + 1) * w1 + j + 2],
						i4 = par[i * w1 + j + 1],
						i5 = par[(i + 1) * w1 + j];
				if((i1 & 512) != 0)
				{
					i1 = i1 & (~512);
				}
				else if(isRiver(i1))
				{
					initChunkSeed(x + j, y + i);
					int mix = selectModeOrRandom(i1, i2, i3, i4);
					if(isWater(mix))
					{
						
					}
					else if(mix == EnumBiome.high_mountain.id())
					{
						i1 = EnumBiome.plateau.id();
					}
					else if(mix == EnumBiome.mid_mountain.id())
					{
						i1 = EnumBiome.valley.id();
					}
				}
				else if(isOcean(i1) ? 
						!(isWater(i2) && isWater(i3) && isWater(i4) && isWater(i5)) : 
							(isOcean(i2) || isOcean(i3) || isOcean(i4) || isOcean(i5)))
				{
					if(i1 == EnumBiome.ocean.id() || i1 == EnumBiome.ocean_deep.id())
					{
						i1 = EnumBiome.continental_shelf.id();
					}
					else if(i1 == EnumBiome.river.id())
					{
						if(!(isRiver(i2) || isRiver(i3) || isRiver(i4) || isRiver(i5)))
						{
							i1 = EnumBiome.plain.id();
						}
						else
						{
							i1 = EnumBiome.continental_shelf.id();
						}
					}
					else
					{
						i1 = EnumBiome.beach.id();
					}
				}
				ret[i * w + j] = i1;
			}
		return ret;
	}
	
	private boolean isWater(int id)
	{
		return isOcean(id) || isRiver(id);
	}
	
	private boolean isRiver(int id)
	{
		return id == EnumBiome.river.id();
	}
	
	private boolean isOcean(int id)
	{
		return id == EnumBiome.ocean.id() || id == EnumBiome.ocean_deep.id();
	}
}