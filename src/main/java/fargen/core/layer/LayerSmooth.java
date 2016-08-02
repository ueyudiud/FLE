package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerSmooth extends Layer
{
	public LayerSmooth(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int i = x - 1;
		int j = y - 1;
		int k = w + 2;
		int l = h + 2;
		int[] par = parent.getInts(i, j, k, l);
		int[] ret = IntCache.getIntCache(w * h);
		
		for (int i1 = 0; i1 < h; ++i1)
		{
			for (int j1 = 0; j1 < w; ++j1)
			{
				int k1 = par[j1 + 0 + (i1 + 1) * k];
				int l1 = par[j1 + 2 + (i1 + 1) * k];
				int i2 = par[j1 + 1 + (i1 + 0) * k];
				int j2 = par[j1 + 1 + (i1 + 2) * k];
				int k2 = par[j1 + 1 + (i1 + 1) * k];
				if (k1 == l1 && i2 == j2)
				{
					initChunkSeed(j1 + x, i1 + y);
					
					if (nextInt(2) == 0)
					{
						k2 = k1;
					}
					else
					{
						k2 = i2;
					}
				}
				else
				{
					if (k1 == l1)
					{
						k2 = k1;
					}
					
					if (i2 == j2)
					{
						k2 = i2;
					}
				}
				ret[j1 + i1 * w] = k2;
			}
		}
		return ret;
	}
}