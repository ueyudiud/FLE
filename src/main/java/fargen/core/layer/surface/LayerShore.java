/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceTerrain._beach1;
import static fargen.core.layer.surface.LayerSurfaceTerrain._beach2;
import static fargen.core.layer.surface.LayerSurfaceTerrain._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._lake;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceTerrain._ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._river;
import static fargen.core.layer.surface.LayerSurfaceTerrain._swamp;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerShore extends LayerExpandMix
{
	public LayerShore(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		if (!isOceanic(core) && !isWateric(core))
		{
			if (isOceanic(up) || isOceanic(down) || isOceanic(left) || isOceanic(right))
			{
				return isMontain(core) ? _beach2.id : _beach1.id;
			}
		}
		return core;
	}
	
	protected boolean isMontain(int id)
	{
		return id == _montain.id || id == _montain_edge.id;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == _ocean.id || id == _deep_ocean.id;
	}
	
	protected boolean isWateric(int id)
	{
		return id == _swamp.id || id == _river.id || id == _lake.id;
	}
}
