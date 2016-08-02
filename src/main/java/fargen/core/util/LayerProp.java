package fargen.core.util;

import farcore.lib.util.NoiseBase;
import fargen.core.layer.Layer;

public class LayerProp
{
	public Layer terrainLayer;
	public Layer heightLayer;
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
		return terrainHeight(array, x, y, w, h, 4D);
	}

	public double[] terrainHeight(double[] array, int x, int y, int w, int h, double size)
	{
		return heightNoise.noise(array, w, h, (double) x, (double) y, heightStep * size, heightStep * size);
	}
}