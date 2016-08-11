package fargen.core.util;

import farcore.lib.util.NoiseBase;
import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

public class LayerProp
{
	public Layer terrainLayer;
	public Layer heightLayer;
	public Layer biomeLayer1;
	public GenLayer biomeLayer2;
	public double heightStep;
	public NoiseBase heightNoise;

	public int[] terrain(int x, int y, int w, int h)
	{
		return terrainLayer.getInts(x, y, w, h);
	}

	public void markZoom()
	{
		heightStep = 1D / heightLayer.zoom();
	}
	
	public double[] terrainHeight(double[] array, int x, int y, int w, int h)
	{
		return terrainHeight(array, x, y, w, h, 1D);
	}
	
	public double[] terrainHeight(double[] array, int x, int y, int w, int h, double size)
	{
		return heightNoise.noise(array, w, h, (double) x, (double) y, heightStep * size, heightStep * size);
	}
}