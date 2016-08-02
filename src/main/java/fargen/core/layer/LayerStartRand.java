package fargen.core.layer;

import net.minecraft.world.gen.layer.IntCache;

public class LayerStartRand extends Layer
{
	private int maxGen;

	public LayerStartRand(long seed, int maxGen)
	{
		super(seed);
		this.maxGen = maxGen;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);
		
		for (int i1 = 0; i1 < h; ++i1)
		{
			for (int j1 = 0; j1 < w; ++j1)
			{
				initChunkSeed(x + j1, y + i1);
				ret[j1 + i1 * w] = nextInt(maxGen);
			}
		}
		return ret;
	}
}