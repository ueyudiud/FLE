/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceBiome._beach1;
import static fargen.core.layer.surface.LayerSurfaceBiome._beach2;
import static fargen.core.layer.surface.LayerSurfaceBiome._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceBiome._lake;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceBiome._ocean;
import static fargen.core.layer.surface.LayerSurfaceBiome._river;
import static fargen.core.layer.surface.LayerSurfaceBiome._swamp;

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
				return isMontain(core) ? _beach2.biomeID : _beach1.biomeID;
			}
		}
		return core;
	}
	
	protected boolean isMontain(int id)
	{
		return id == _montain.biomeID || id == _montain_edge.biomeID;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == _ocean.biomeID || id == _deep_ocean.biomeID;
	}
	
	protected boolean isWateric(int id)
	{
		return id == _swamp.biomeID || id == _river.biomeID || id == _lake.biomeID;
	}
}