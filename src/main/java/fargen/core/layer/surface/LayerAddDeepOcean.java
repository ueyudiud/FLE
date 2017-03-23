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
		int oceanCount = 0;
		if (up == 0) oceanCount++;
		if (down == 0) oceanCount++;
		if (left  == 0) oceanCount++;
		if (right == 0) oceanCount++;
		return (core == 0) && (oceanCount > 3) ? LayerSurfaceBiome._deep_ocean.biomeID : core;
	}
}