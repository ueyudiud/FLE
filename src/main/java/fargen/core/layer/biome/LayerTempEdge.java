package fargen.core.layer.biome;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerTempEdge extends Layer
{
	public LayerTempEdge(long seed, GenLayer layer)
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
		for(int j = 0; j < h; j ++)
		{
			for(int i = 0; i < w; i ++)
			{
				int q = (j + 1) * w1 + i + 1;
				int a = par[q];
				int a2 = par[q - w1];
				int a4 = par[q + 1];
				int a6 = par[q + w1];
				int a8 = par[q - 1];
				initChunkSeed(x + i, y + j);
				int b = selectModeOrRandom(a2, a4, a6, a8);
				ret[j * w + i] = a == b ? a : selectRandom((a + b) / 2, (a + b + 1) / 2);
			}
		}
		return ret;
	}
}