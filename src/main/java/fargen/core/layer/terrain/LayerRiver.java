package fargen.core.layer.terrain;

import farcore.data.EnumTerrain;
import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerRiver extends Layer
{
	private GenLayer layer;
	
	public LayerRiver(long seed, GenLayer layer, GenLayer river)
	{
		super(seed);
		parent = layer;
		this.layer = river;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] par = parent.getInts(x, y, w, h);
		int[] river = layer.getInts(x, y, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		int size = w * h;
		for(int i = 0; i < size; ++i)
		{
			if(river[i] != 0 && canRiverGen(par[i]))
			{
				ret[i] = EnumTerrain.river.ordinal();
			}
			else
			{
				ret[i] = par[i];
			}
		}
		return ret;
	}
	
	private boolean canRiverGen(int id)
	{
		return EnumTerrain.values()[id].canRiverGen;
	}
	
	private boolean isOcean(int id)
	{
		return EnumTerrain.values()[id].isWater;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		layer.initWorldGenSeed(seed);
	}

	@Override
	public void markZoom(int zoom)
	{
		super.markZoom(zoom);
		if(layer != null && layer instanceof Layer)
		{
			((Layer) layer).markZoom(zoom * zoomLevel);
		}
	}
}