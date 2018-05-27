/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.layer;

/**
 * @author ueyudiud
 */
public class LayerRandomStart extends Layer
{
	private int max;
	
	public LayerRandomStart(long seed, int max)
	{
		super(seed);
		this.max = max;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] result = array(w, h);
		for (int i = 0; i < h; ++i)
			for (int j = 0; j < w; ++j)
			{
				initChunkSeed(x + j, y + i);
				result[i * w + j] = nextInt(this.max);
			}
		return result;
	}
}
