package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class FLELayerItrZoom extends FLELayer
{
	private int zt;
	
	public FLELayerItrZoom(int aZoomTime, long seed, GenLayer aLayer)
	{
		super(seed);
		zt = aZoomTime;
		GenLayer layer = new GenLayerZoom(seed, aLayer);
		for(int i = 1; i < zt; ++i)
		{
			layer = new GenLayerZoom(seed + i, layer);
		}
		parent = layer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		return parent.getInts(x, z, w, h);
	}
}