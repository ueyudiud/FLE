/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.layer.biome;

import fargen.core.layer.DataSurface;
import fargen.core.layer.Layer;
import nebula.common.util.L;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.util.math.MathHelper;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiomeStart extends Layer
{
	private NoisePerlin noise = new NoisePerlin(0L, 3, 0.25F, 2.0F, 2.0F);
	
	public LayerSurfaceBiomeStart(long seed)
	{
		super(seed);
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] result = array(w, h);
		for (int i = 0; i < h; ++i)
			for (int j = 0; j < w; ++j)
			{
				initChunkSeed(x + j, y + i);
				int temp = temperature(x + j, y + i);
				result[i * w + j] = temp | nextInt(16) << 8;
			}
		return result;
	}
	
	protected int temperature(int x, int y)
	{
		float off = (float) ((y + 4 /** The generation offset. */
				& 0x1F) * Math.PI / DataSurface.TEMP_RANGE_SIZE);
		int ret = (int) ((1.0F - MathHelper.sin(off)) * DataSurface.TEMP_LEVEL_SIZE + this.noise.noise(x, y) * 0.25F) - 1;
		return L.range(0, 7, ret);
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		this.noise.setSeed(seed ^ 431573952356934L);
	}
}
