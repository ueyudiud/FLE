import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import fle.core.util.noise.NoiseBase;
import fle.core.util.noise.NoiseFuzzy;

public class Debug
{
	public static void main(String[] args)
	{
		try
		{
			File outFile = new File("debug.png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	NoiseBase noise = new NoiseFuzzy(24984L, 3, 15D, 1.7D, 2F);
	    	int size = 16;
	    	double[] draw = new double[256 * size * size];
	    	for(int i = 0; i < size; ++i)
	    		for(int j = 0; j < size; ++j)
	    		{
	    			double[] ds = noise.noise(new double[256], 16 * i, 16 * j, 16, 16);
	    			for(int k = 0; k < 16; ++k)
	    				System.arraycopy(ds, k * 16, draw, (j * 16 + k) * size * 16 + i * 16, 16);
	    		}
	    	BufferedImage outBitmap = new BufferedImage(size * 16, size * 16, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	for (int x = 0; x < size * 16; x++)
	    	{
	    		for (int z = 0; z < size * 16; z++)
	    		{
	    			graphics.setColor(new Color(0x010101 * (int) (0x7F * draw[z * size * 16 + x] + 0x7F)));
	    			graphics.drawRect(x, z, 1, 1);
	    		}
	    	}
	    	ImageIO.write(outBitmap, "png", outFile);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}