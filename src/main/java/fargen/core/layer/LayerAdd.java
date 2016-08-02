package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerAdd extends Layer
{
	private int add;
	private int chance;

	public LayerAdd(long seed, int add, int chance, GenLayer layer)
	{
		super(seed);
		this.add = add;
		this.chance = chance;
		parent = layer;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] is = IntCache.getIntCache(w * h);
		int[] par = parent.getInts(x, y, w, h);
		for(int i = 0; i < h; ++i)
		{
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				if(par[id] != 0)
				{
					is[id] = par[id];
				}
				else
				{
					initChunkSeed(x + j, y + i);
					is[id] = nextInt(chance) == 0 ? add : 0;
				}
			}
		}
		return is;
	}
}