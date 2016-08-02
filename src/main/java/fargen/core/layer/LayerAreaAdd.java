package fargen.core.layer;

import farcore.lib.util.NoisePerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerAreaAdd extends LayerNoise
{
	private int add;
	private int chance1;
	private float chance2;
	
	public LayerAreaAdd(int add, int chance1, float chance2, long seed, GenLayer layer)
	{
		super(seed, new NoisePerlin(seed, 3, 3.0F, 1.8F, 1.2F));
		parent = layer;
		this.add = add;
		this.chance1 = chance1;
		this.chance2 = chance2;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] par = parent.getInts(x, y, w, h);
		int[] is = IntCache.getIntCache(w * h);
		double[] flo = noise.noise(null, w, h, x, y);
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
					is[id] = nextInt(chance1) * flo[id] < chance2 ? add : 0;
				}
			}
		}
		return is;
	}
}