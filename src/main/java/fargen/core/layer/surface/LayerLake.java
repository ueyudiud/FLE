/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.surface;

import fargen.core.layer.abstracts.LayerExpandMix;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerLake extends LayerExpandMix
{
	public LayerLake(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int up, int down, int left, int right, int core)
	{
		initChunkSeed(x, y);
		if (isOceanic(core))
		{
			return !isOceanic(up) && !isOceanic(down) && !isOceanic(right) && !isOceanic(left) ? LayerSurfaceTerrain._lake.id : core;
		}
		return core;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == LayerSurfaceTerrain._ocean.id || id == LayerSurfaceTerrain._deep_ocean.id;
	}
}
