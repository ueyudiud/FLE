package fargen.core.layer;

import java.util.Arrays;

import net.minecraft.world.gen.layer.IntCache;

public class LayerStart extends Layer
{
	private int target;

	public LayerStart(int target)
	{
		super(0L);
		this.target = target;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);
		Arrays.fill(ret, target);
		return ret;
	}
}