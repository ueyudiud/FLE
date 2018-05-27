/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author ueyudiud
 */
public abstract class Layer extends GenLayer
{
	public Layer(long seed)
	{
		super(seed);
	}
	
	@Override
	public abstract int[] getInts(int x, int y, int w, int h);
	
	protected static int[] array(int u, int v)
	{
		return IntCache.getIntCache(u * v);
	}
	
	/**
	 * Generates a pseudo random boolean value.
	 */
	protected boolean nextBoolean()
	{
		return nextInt(2) == 0;
	}
}
