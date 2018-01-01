/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceTerrain._high_plains;
import static fargen.core.layer.surface.LayerSurfaceTerrain._high_plains_edge;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceTerrain._plains;
import static fargen.core.layer.surface.LayerSurfaceTerrain._swamp;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceTerrainEdge extends LayerExpandMix
{
	public LayerSurfaceTerrainEdge(long seed, GenLayer layer)
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
		if (core == _swamp.biomeID)
		{
			return getUnlessAbsent(core, up, down, left, right, _plains.biomeID);
		}
		if (core == _high_plains.biomeID)
		{
			return getUnlessAbsent(core, up, down, left, right, _high_plains_edge.biomeID);
		}
		return core;
	}
}
