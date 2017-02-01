package fargen.core.layer;

import nebula.common.util.noise.NoiseBase;
import net.minecraft.world.gen.layer.IntCache;

public class LayerStartNoise extends LayerNoise
{
	int d;

	public LayerStartNoise(long seed, int div, NoiseBase noise)
	{
		super(seed, noise);
		d = div;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		double[] v = noise.noise(null, w, h, x, y);
		int a = w * h;
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < a; ret[i] = (int) (v[i] * d), ++i)
		{
			;
		}
		return ret;
	}
}