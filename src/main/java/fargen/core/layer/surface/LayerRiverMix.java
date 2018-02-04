/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.core.layer.surface;

import static fargen.core.layer.surface.LayerSurfaceTerrain._beach1;
import static fargen.core.layer.surface.LayerSurfaceTerrain._beach2;
import static fargen.core.layer.surface.LayerSurfaceTerrain._deep_ocean;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain;
import static fargen.core.layer.surface.LayerSurfaceTerrain._montain_edge;
import static fargen.core.layer.surface.LayerSurfaceTerrain._ocean;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author ueyudiud
 */
public class LayerRiverMix extends Layer
{
	private GenLayer	base;
	private GenLayer	river;
	
	public LayerRiverMix(long seed, GenLayer base, GenLayer river)
	{
		super(seed);
		this.base = base;
		this.river = river;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] biomes = this.base.getInts(x, y, w, h);
		int[] rivers = this.river.getInts(x, y, w, h);
		int[] result = IntCache.getIntCache(w * h);
		for (int y1 = 0; y1 < h; ++y1)
			for (int x1 = 0; x1 < w; ++x1)
			{
				int id = y1 * w + x1;
				int b = biomes[id];
				int r = rivers[id];
				
				if (isOceanic(b) || isMontain(b))
				{
					result[id] = b;
				}
				else if (r > 0)
				{
					if (isBeach(b))
					{
						result[id] = _ocean.id;
					}
					else
					{
						result[id] = r;
					}
				}
				else
				{
					result[id] = b;
				}
			}
		return result;
	}
	
	protected boolean isMontain(int id)
	{
		return id == _montain.id || id == _montain_edge.id;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == _ocean.id || id == _deep_ocean.id;
	}
	
	protected boolean isBeach(int id)
	{
		return id == _beach1.id || id == _beach2.id;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		this.base.initWorldGenSeed(seed);
		this.river.initWorldGenSeed(seed);
	}
}
