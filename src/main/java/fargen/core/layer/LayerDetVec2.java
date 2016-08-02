package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerDetVec2 extends Layer
{
	private int k;
	private int m;
	
	public LayerDetVec2(long seed, int k, int mul, GenLayer layer)
	{
		super(seed);
		parent = layer;
		this.k = k;
		m = mul;
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
				int b = (a1 * 5 + a2 * 7 + a3 * 5 - a5 * 5 - a6 * 7 - a7 * 5) * k / 17 + m;
				int c = (a3 * 5 + a4 * 7 + a5 * 5 - a7 * 5 - a8 * 7 - a1 * 5) * k / 17 + m;
				ret[j * w + i] = b << 8 | c;
			}
		}
		return ret;
	}
}