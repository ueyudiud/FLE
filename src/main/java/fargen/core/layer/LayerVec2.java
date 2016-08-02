package fargen.core.layer;

import farcore.lib.util.NoiseBase;
import net.minecraft.world.gen.layer.IntCache;

public class LayerVec2 extends LayerNoise
{
	private int d;
	
	public LayerVec2(long seed, int div, NoiseBase noise)
	{
		super(seed, noise);
		d = div;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		double[] v = noise.noise(null, w, h, 2, x, y, 27482.0, 1.0, 1.0, 47162.0);
		int a = w * h;
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < a; ret[i] = (int) (v[i] * d) << 8 | (int) (v[i + a] * d), ++i)
		{
			;
		}
		return ret;
	}
}