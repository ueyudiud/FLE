package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerAdd extends LayerBase
{
	private int add;
	private int chance;

	public LayerAdd(int add, int chance, long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
		this.add = add;
		this.chance = chance;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] is = IntCache.getIntCache(w * h);
		int[] par = parent.getInts(x, y, w, h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				if(par[id] != 0)
					is[id] = par[id];
				initChunkSeed(x + j, y + i);
				if(nextInt(chance) == 0)
					is[id] = add;
				else
					is[id] = 0;
			}
		return is;
	}	
}