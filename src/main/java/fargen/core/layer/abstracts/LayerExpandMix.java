/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.abstracts;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author ueyudiud
 */
public abstract class LayerExpandMix extends Layer
{
	public LayerExpandMix(long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int xSize = w + 2;
		int zSize = h + 2;
		int[] par = this.parent.getInts(x - 1, y - 1, xSize, zSize);
		int[] res = IntCache.getIntCache(w * h);
		for (int z1 = 0; z1 < h; z1++)
			for (int x1 = 0; x1 < w; x1++)
			{
				int index = (x1 + 1) + (z1 + 1) * xSize;
				res[x1 + z1 * w] = getValue(x + x1 + 1, y + z1 + 1, par[index - xSize], par[index + xSize], par[index - 1], par[index + 1], par[index]);
			}
		return res;
	}
	
	protected abstract int getValue(int x, int y, int up, int down, int left, int right, int core);
	
	protected int getUnlessAbsent(int t, int a, int b, int c, int d, int f)
	{
		return a != t || b != t || c != t || d != t ? f : t;
	}
}
