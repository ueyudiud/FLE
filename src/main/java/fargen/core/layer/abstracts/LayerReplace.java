/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.abstracts;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public abstract class LayerReplace extends Layer
{
	public LayerReplace(long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = this.parent.getInts(x, y, w, h);
		for (int y1 = 0; y1 < h; ++y1)
			for (int x1 = 0; x1 < w; ++x1)
			{
				int id = y1 * w + x1;
				ret[id] = getValue(x + x1, y + y1, ret[id]);
			}
		return ret;
	}
	
	protected abstract int getValue(int x, int y, int id);
}
