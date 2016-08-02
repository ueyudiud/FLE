package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;

public abstract class Layer extends GenLayer
{
	protected int zoomLevel = 1;
	int zoom;
	
	public Layer(long seed)
	{
		super(seed);
	}

	@Override
	public abstract int[] getInts(int x, int y, int w, int h);
	
	public void markZoom(int zoom)
	{
		this.zoom = zoom;
		if(parent != null && parent instanceof Layer)
		{
			((Layer) parent).markZoom(zoom * zoomLevel);
		}
	}
	
	public int zoom()
	{
		return zoom;
	}
}