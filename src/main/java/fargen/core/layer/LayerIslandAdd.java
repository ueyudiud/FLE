/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerIslandAdd extends Layer
{
	final int	rand;
	final int	type;
	
	public LayerIslandAdd(long seed, GenLayer parent, int rand, int type)
	{
		super(seed);
		this.parent = parent;
		this.rand = rand;
		this.type = type;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] result = this.parent.getInts(x, y, w, h);
		int c = 0;
		for (int i = 0; i < h; ++i)
			for (int j = 0; j < w; ++j)
			{
				if (result[c] == 0)
				{
					initChunkSeed(x + j, y + i);
					result[c] = nextInt(this.rand) == 0 ? this.type : 0;
				}
				++c;
			}
		return result;
	}
}
