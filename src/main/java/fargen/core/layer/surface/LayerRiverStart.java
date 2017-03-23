/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceBiome._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain;
import static fargen.core.layer.surface.LayerSurfaceBiome._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceBiome._ocean;

import fargen.core.layer.abstracts.LayerReplace;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerRiverStart extends LayerReplace
{
	public LayerRiverStart(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int id)
	{
		//		initChunkSeed(x, y);
		return !isOceanic(id) && !isMontain(id) ? 1 : 0;
	}
	
	protected boolean isMontain(int id)
	{
		return id == _montain.biomeID || id == _montain_edge.biomeID;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == _ocean.biomeID || id == _deep_ocean.biomeID;
	}
}