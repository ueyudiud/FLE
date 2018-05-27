/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.biome.layer.surface.rock;

import fargen.core.layer.Layer;

/**
 * @author ueyudiud
 */
public class LayerRockStart extends Layer
{
	static final short	lava_in		= 32;
	static final short	weathering	= 64;
	
	public LayerRockStart(long seed)
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
				result[i * w + j] = nextInt(31);
				if (nextInt(4) == 0)
				{
					result[i * w + j] |= lava_in;
				}
				if (nextInt(3) == 0)
				{
					result[i * w + j] |= weathering;
				}
				if (nextInt(12) == 0)
				{
					result[i * w + j] |= 128;
				}
				if (nextInt(12) == 0)
				{
					result[i * w + j] |= 256;
				}
			}
		return result;
	}
}
