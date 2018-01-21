/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.surface;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerAddDeepOcean extends LayerExpandMix
{
	public LayerAddDeepOcean(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		if (core == 0)
		{
			int oceanCount = 0;
			if (up == 0) oceanCount++;
			if (down == 0) oceanCount++;
			if (left == 0) oceanCount++;
			if (right == 0) oceanCount++;
			return oceanCount > 3 ? LayerSurfaceTerrain._deep_ocean.id : core;
		}
		return core;
	}
}
