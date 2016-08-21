package fargen.core.util;

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

public class LayerProp
{
	public Layer terrainLayer;
	public Layer heightLayer;
	public Layer biomeLayer1;
	public GenLayer biomeLayer2;
	
	public int[] terrain(int x, int y, int w, int h)
	{
		return terrainLayer.getInts(x, y, w, h);
	}
	
	public void markZoom()
	{
	}
}