/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import static fargen.core.FarGenBiomes.glacispical_land;
import static fargen.core.FarGenBiomes.rocky_desert;
import static fargen.core.FarGenBiomes.temperate_desert;
import static fargen.core.FarGenBiomes.tropical_desert;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiomeEdge extends LayerExpandMix
{
	static final int EDGE_MARK = 0x256;
	
	public LayerSurfaceBiomeEdge(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		if (core == rocky_desert.biomeID || core == glacispical_land.biomeID)
		{
			return getUnlessAbsent(core, up, down, left, right, core | EDGE_MARK);
		}
		if (isDesert(core))
		{
			return isDesert(up) && isDesert(down) && isDesert(left) && isDesert(right) ? core : core | EDGE_MARK;
		}
		return core;
	}
	
	protected boolean isDesert(int i)
	{
		return i == tropical_desert.biomeID || i == temperate_desert.biomeID;
	}
}
