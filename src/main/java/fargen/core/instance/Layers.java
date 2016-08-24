package fargen.core.instance;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import farcore.lib.util.Log;
import fargen.core.layer.Layer;
import fargen.core.layer.LayerFuzzyZoom;
import fargen.core.layer.LayerLerpZoom;
import fargen.core.layer.LayerRing;
import fargen.core.layer.LayerSmooth;
import fargen.core.layer.LayerStartRand;
import fargen.core.layer.LayerVoronoiZoom;
import fargen.core.layer.LayerZoom;
import fargen.core.layer.biome.LayerBaseBiome;
import fargen.core.layer.biome.LayerBiomeSurfaceMixed;
import fargen.core.layer.chunk.LayerChunkEdge;
import fargen.core.layer.terrain.LayerRiver;
import fargen.core.layer.terrain.LayerTerrainBase;
import fargen.core.util.LayerProp;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public class Layers
{
	private static final boolean DRAW_IMG = true;
	
	public static LayerProp wrapSurface(long seed)
	{
		LayerProp prop = new LayerProp();
		//Chunk layers.
		Layer layer1 = new LayerStartRand(1L, 8);
		drawImage(256, layer1, "layer1.1");
		layer1 = new LayerZoom(5, 2L, layer1);
		drawImage(256, layer1, "layer1.2");
		layer1 = new LayerSmooth(7L, layer1);
		drawImage(256, layer1, "layer1.3");
		layer1 = new LayerChunkEdge(8L, layer1);
		drawImage(256, layer1, "layer1.4");
		layer1 = new LayerVoronoiZoom(9L, layer1);
		drawImage(256, layer1, "layer1.5");
		//Terrain base layers.
		Layer layer2 = new LayerStartRand(11L, 16);
		drawImage(256, layer2, "layer2.1");
		layer2 = new LayerFuzzyZoom(12L, layer2);
		drawImage(256, layer2, "layer2.2");
		layer2 = new LayerLerpZoom(14L, layer2);
		drawImage(256, layer2, "layer2.3");
		layer2 = new LayerSmooth(2024L, layer2);
		layer2 = new LayerLerpZoom(15L, layer2);
		drawImage(256, layer2, "layer2.4");
		layer2 = new LayerSmooth(2001L, layer2);
		layer2 = new LayerLerpZoom(16L, layer2);
		drawImage(256, layer2, "layer2.5");
		layer2 = new LayerSmooth(2022L, layer2);
		layer2 = new LayerLerpZoom(17L, layer2);
		drawImage(256, layer2, "layer2.6");
		layer2 = new LayerSmooth(2002L, layer2);
		layer2 = new LayerLerpZoom(17L, layer2);
		drawImage(256, layer2, "layer2.7");
		layer2 = new LayerSmooth(2025L, layer2);
		layer2 = new LayerLerpZoom(18L, layer2);
		drawImage(256, layer2, "layer2.8");
		layer2 = new LayerSmooth(2003L, layer2);
		drawImage(256, layer2, "layer2.9");
		layer2 = new LayerLerpZoom(19L, layer2);
		drawImage(256, layer2, "layer2.10");
		layer2 = new LayerLerpZoom(20L, layer2);
		drawImage(256, layer2, "layer2.11");
		//River layers.
		Layer layer7 = new LayerStartRand(51L, 5);
		drawImage(256, layer7, "layer7.1");
		layer7 = new LayerZoom(5, 52L, layer7);
		drawImage(256, layer7, "layer7.2");
		layer7 = new LayerRing(59L, layer7);
		layer7 = new LayerSmooth(2019L, layer7);
		drawImage(256, layer7, "layer7.3");
		layer7 = new LayerVoronoiZoom(61L, layer7);
		drawImage(256, layer7, "layer7.4");
		//Terrain recalculated layers.
		Layer layer3 = new LayerTerrainBase(21L, layer2, layer1);
		drawImage(256, layer3, "layer3.1");
		layer3 = new LayerRiver(22L, layer3, layer7);
		//Temperature layers.
		Layer layer4 = new LayerStartRand(23L, 9);
		drawImage(256, layer4, "layer4.1");
		layer4 = new LayerFuzzyZoom(2, 24L, layer4);
		drawImage(256, layer4, "layer4.2");
		layer4 = new LayerLerpZoom(26L, layer4);
		drawImage(256, layer4, "layer4.3");
		layer4 = new LayerZoom(2, 28L, layer4);
		drawImage(256, layer4, "layer4.4");
		layer4 = new LayerLerpZoom(30L, layer4);
		drawImage(256, layer4, "layer4.5");
		//Humidity layers.
		Layer layer5 = new LayerStartRand(31L, 11);
		drawImage(256, layer5, "layer5.1");
		layer5 = new LayerFuzzyZoom(2, 32L, layer5);
		drawImage(256, layer5, "layer5.2");
		layer5 = new LayerLerpZoom(34L, layer5);
		drawImage(256, layer5, "layer5.3");
		layer5 = new LayerZoom(2, 35L, layer5);
		drawImage(256, layer5, "layer5.4");
		layer5 = new LayerLerpZoom(37L, layer5);
		drawImage(256, layer5, "layer5.5");
		//Biome layers.
		Layer layer6 = new LayerBaseBiome(41L, layer4, layer5);
		layer6 = new LayerZoom(3, 472L, layer6);
		drawImage(256, layer6, "layer6.1");
		layer6 = new LayerVoronoiZoom(42L, layer6);
		drawImage(256, layer6, "layer6.2");
		layer6 = new LayerBiomeSurfaceMixed(43L, layer6, layer3);
		drawImage(256, layer6, "layer6.3");
		//Mixed layers.
		GenLayer layer9 = new GenLayerVoronoiZoom(1001L, layer6);
		drawImage(256, layer9, "layer9");
		prop.terrainLayer = layer3;
		prop.biomeLayer1 = layer6;
		prop.biomeLayer2 = layer9;
		layer9.initWorldGenSeed(seed);
		layer6.markZoom(1);
		prop.markZoom();
		return prop;
	}
	
	public static void drawImage(int size, GenLayer genlayer, String name)
	{
		if (!DRAW_IMG)
			return;
		try
		{
			genlayer.initWorldGenSeed(83L);
			File outFile = new File(name + ".png");
			if (outFile.exists())
				return;
			int[] ints = genlayer.getInts(0, 0, size, size);
			BufferedImage outBitmap = new BufferedImage(size, size, 1);
			Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
			graphics.clearRect(0, 0, size, size);
			Log.info(name + ".png");
			for (int x = 0; x < size; x++)
			{
				for (int z = 0; z < size; z++)
				{
					int v = ints[(x * size + z)];
					if(v < 0)
					{
						v = -v;
						v ^= 716282619;
						v ^= v >> 4;
					}
					else
					{
						v ^= v >> 4;
					}
					int c = (v * 71 & 0xFF) << 16 | (v * 79 & 0xFF) << 8 | (v * 89 & 0xFF);
					graphics.setColor(new Color(c));
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