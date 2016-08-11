package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerRing extends Layer
{
	public LayerRing(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
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
		{
			for(int j = 0; j < w; ++j)
			{
				int id = (i + 1) * w1 + (j + 1);
				int i1 = par[id];
				label:
				{
					if(par[id - 1] != i1 || par[id + 1] != i1 ||
							par[id - 1 - w1] != i1 || par[id + 1 - w1] != i1 ||
							par[id - 1 + w1] != i1 || par[id + 1 + w1] != i1 ||
							par[id - w1] != i1 || par[id + w1] != i1)
					{
						break label;
					}
					ret[i * w + j] = 0;
					continue;
				}
				ret[i * w + j] = 1;
			}
		}
		return ret;
	}
}