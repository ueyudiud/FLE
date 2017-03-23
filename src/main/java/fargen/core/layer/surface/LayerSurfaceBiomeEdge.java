/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceBiome._high_plains;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceBiome._plains;
import static fargen.core.layer.surface.LayerSurfaceBiome._swamp;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiomeEdge extends LayerExpandMix
{
	public LayerSurfaceBiomeEdge(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		if (core == _montain.biomeID)
		{
			return getUnlessAbsent(core, up, down, left, right, _montain_edge.biomeID);
		}
		if (core == _swamp.biomeID || core == _high_plains.biomeID)
		{
			return getUnlessAbsent(core, up, down, left, right, _plains.biomeID);
		}
		return core;
	}
	
	private int getUnlessAbsent(int t, int a, int b, int c, int d, int f)
	{
		return a != t || b != t || c != t || d != t ? f : t;
	}
}