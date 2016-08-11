package fargen.core.layer.biome;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerBiomeEdge extends Layer
{
	public LayerBiomeEdge(long seed, GenLayer layer)
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
				int l = a >> 8; a &= 0xFF;
				int a2 = par[q - w1] & 0xFF;
				int a4 = par[q + 1] & 0xFF;
				int a6 = par[q + w1] & 0xFF;
				int a8 = par[q - 1] & 0xFF;
				ret[j * w + i] = a != a2 || a != a4 || a != a6 || a != a8 ?
						l + 1 << 8 | a : l << 8 | a;
			}
		}
		return ret;
	}
}