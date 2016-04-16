package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerLinkMainland extends LayerBase
{
	private int chance;
	
	public LayerLinkMainland(int chance, long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
		this.chance = chance;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] is = IntCache.getIntCache(w * h);
		int[] par = parent.getInts(x1, y1, w1, h1);
		int v1, v2, v3, v4, v5;
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				if((v1 = par[w1 * (i + 1) + j + 1]) != 0)
				{
					is[w * i + j] = v1;
				}
				else
				{
					v2 = par[w1 * i + j + 1];
					v3 = par[w1 * (i + 1) + j];
					v4 = par[w1 * (i + 2) + j + 1];
					v5 = par[w1 * (i + 1) + j + 2];
					initChunkSeed(x + j, y + i);
					if(chance == 1)
					{
						is[w * i + j] = selectModeOrRandom(v2, v3, v4, v5);
					}
					else if(nextInt(chance) == 0)
					{
						is[w * i + j] = selectModeOrRandom(v2, v3, v4, v5);
					}
				}
			}
		return is;
	}
}