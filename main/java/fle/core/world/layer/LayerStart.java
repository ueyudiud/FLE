package fle.core.world.layer;

import net.minecraft.world.gen.layer.IntCache;

public class LayerStart extends LayerBase
{
	private int genChance;
	
	public LayerStart(int genChance, long seed)
	{
		super(seed);
		this.genChance = genChance;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);

        for (int i1 = 0; i1 < h; ++i1)
        {
            for (int j1 = 0; j1 < w; ++j1)
            {
        		if((x + j1) * (x + j1) + (y + i1) * (y + i1) < 10)
        		{
        			ret[j1 + i1 * w] = 1;
        		}
        		else
        		{
                    initChunkSeed((long)(x + j1), (long)(y + i1));
                    ret[j1 + i1 * w] = nextInt(genChance) == 0 ? 1 : 0;
        		}
            }
        }
        
        if (x > -w && x <= 0 && y > -h && y <= 0)
        {
            ret[-x + -y * w] = 1;
        }
		return ret;
	}
}