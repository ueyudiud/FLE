package farcore.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import farcore.lib.world.biome.BiomeBase;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoiseCoherent;
import farcore.util.noise.NoisePerlin;
import fle.core.world.layer.LayerAdd;
import fle.core.world.layer.LayerBase;
import fle.core.world.layer.LayerFuzzyZoom;
import fle.core.world.layer.LayerLinkMainland;
import fle.core.world.layer.LayerRing;
import fle.core.world.layer.LayerStart;
import fle.core.world.layer.LayerZoom;
import fle.core.world.layer.surface.LayerBeach;
import fle.core.world.layer.surface.LayerEdge;
import fle.core.world.layer.surface.LayerFloor;
import fle.core.world.layer.surface.LayerRainfall;
import fle.core.world.layer.surface.LayerRiver;
import fle.core.world.layer.surface.LayerSort;
import fle.core.world.layer.surface.LayerTemp;
import fle.core.world.layer.surface.LayerTerrainBase;
import fle.load.Biomes;
import fle.load.Substances;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.IntCache;

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