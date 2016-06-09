package farcore.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import farcore.lib.world.biome.BiomeBase;

@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		
	}
	
	public static void draw(String string, int w, int h, int[] range)
	{
		File file = new File("./" + string + ".png");
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int idx = 0;
		Graphics graphics = image.getGraphics();
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				graphics.setColor(new Color(BiomeBase.getBiome(range[i + j * w]).color));
				graphics.drawRect(i, j, 1, 1);
			}
		try
		{
			ImageIO.write(image, "png", file);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static double max(double[] value)
	{
		double i = value[0];
		for(double d : value)
		{
			if(d > i)
				i = d;
		}
		return i;
	}
	
	private static double min(double[] value)
	{
		double i = value[0];
		for(double d : value)
		{
			if(d < i)
				i = d;
		}
		return i;
	}
}