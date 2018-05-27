/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.biome.layer.surface.rock;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerRockRemappedData extends LayerExpandMix
{
	public LayerRockRemappedData(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		// int depth = core & 1;
		// int d = (core >> 1) & 3;
		// int u = (core >> 4) & 7;
		// int m = depth == 0 ? d : u;
		// if ((core & lava_in) != 0)
		// {
		// u = m = d;
		// }
		// if ((core & weathering) != 0)
		// {
		// u |= 256;
		// }
		// return u << 20 | m << 10 | d;
		return core;
	}
}
