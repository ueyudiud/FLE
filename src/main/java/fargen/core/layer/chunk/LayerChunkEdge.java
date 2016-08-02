package fargen.core.layer.chunk;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerChunkEdge extends Layer
{
	private static final int[] op = {0, 1, 1, 1, 0, -1, -1, -1};

	public LayerChunkEdge(long seed, GenLayer layer)
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
				int a1 = par[q - w1 - 1];
				int a2 = par[q - w1];
				int a3 = par[q - w1 + 1];
				int a4 = par[q + 1];
				int a5 = par[q + w1 + 1];
				int a6 = par[q + w1];
				int a7 = par[q + w1 - 1];
				int a8 = par[q - 1];
				int b = op(a1) + op(a2 + 1) + op(a3 + 2) + op(a4 + 3) + op(a5 + 4) + op(a6 + 5) + op(a7 + 6) + op(a8 + 7);
				int c = Math.abs(op(a3) + op(a4 + 1) + op(a5 + 2) + op(a6 + 3) + op(a7 + 4) + op(a8 + 5) + op(a1 + 6) + op(a2 + 7));
				ret[j * w + i] = b > 0 ? 3 : b < 0 ? 2 : c != 0 ? 1 : 0;
			}
		}
		return ret;
	}
	
	private static int op(int v)
	{
		return op[v & 7];
	}
}