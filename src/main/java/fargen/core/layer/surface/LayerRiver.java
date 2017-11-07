/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.surface;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerRiver extends LayerExpandMix
{
	public LayerRiver(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		if (up != core || down != core || left != core || right != core)
		{
			return LayerSurfaceTerrain._river.biomeID;
		}
		return 0;
	}
	
	private int calcWidth(int i)
	{
		return i >= 2 ? 2 + (i & 0x1) : i;
	}
}
