/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer;

/**
 * @author ueyudiud
 */
public class LayerStart extends Layer
{
	private int rand;
	
	public LayerStart(long seed, int rand)
	{
		super(seed);
		this.rand = rand;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] result = array(w, h);
		for (int i = 0; i < h; ++i)
			for (int j = 0; j < w; ++j)
			{
				if ((x + j & 0xFFFFFFFD) == 0 && (y + i & 0xFFFFFFFD) == 0)
				{
					result[i * w + j] = 1;
				}
				else
				{
					initChunkSeed(x + j, y + i);
					result[i * w + j] = nextInt(this.rand) == 0 ? 1 : 0;
				}
			}
		return result;
	}
}
