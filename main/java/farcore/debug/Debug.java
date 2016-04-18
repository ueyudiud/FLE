package farcore.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import farcore.lib.world.biome.BiomeBase;
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
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.IntCache;

public class Debug
{
	public static void main(String[] args)
	{
		Biomes.init();
		LayerBase orLayer = new LayerZoom(5, 47L, new LayerLinkMainland(1, 381L, new LayerZoom(9031L, new LayerLinkMainland(3, 381L, new LayerFuzzyZoom(7L, new LayerAdd(1, 3, 91L, new LayerFuzzyZoom(3, 8L, new LayerAdd(1, 5, 2L, new LayerFuzzyZoom(3L, new LayerStart(5, 1L))))))))));
		LayerTemp tempLayer = new LayerTemp(16, 2839L);
		LayerRainfall rainfallLayer = new LayerRainfall(10, 37292L, new LayerTerrainBase(10, 38917L));
		orLayer = new LayerFloor(718L, orLayer, tempLayer, rainfallLayer);
		orLayer = new LayerRiver(5819L, new LayerFuzzyZoom(2, 27L, orLayer), new LayerRing(2874L, new LayerFuzzyZoom(6, 127L, new LayerFuzzyZoom(2, 927L, new LayerAdd(2, 3, 273L, new LayerStart(2, 901L))))));
		orLayer = new LayerEdge(508L, orLayer);
		orLayer = new LayerBeach(71628L, orLayer);
		orLayer = new LayerZoom(3, 384918L, orLayer);
		orLayer = new LayerSort(381L, orLayer, new LayerZoom(27048L, new LayerRing(381L, new LayerZoom(4, 283L, new LayerAdd(2, 3, 29L, new LayerZoom(2, 2L, new LayerStart(3, -2L)))))));
		GenLayer out1 = new GenLayerSmooth(289471L, orLayer);
		GenLayer out2 = new GenLayerVoronoiZoom(94719L, out1);
		out2.initWorldGenSeed(3767946578827316380L);
		int size = 4096;
		for(int i = 0; i < size; ++i)
		{
			System.out.println(out2.getInts(i, 0, size, 1)[size - i - 1]);
		}
	}
	
	private static void draw(String string, int w, int h, int[] range)
	{
		File file = new File("./" + string + ".png");
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int idx = 0;
		Graphics graphics = image.getGraphics();
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				graphics.setColor(new Color(BiomeBase.getBiome(range[idx++]).color));
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