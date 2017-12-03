/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.instance;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import fargen.core.biome.layer.surface.rock.LayerRockRemappedData;
import fargen.core.biome.layer.surface.rock.LayerRockStart;
import fargen.core.layer.LayerFuzzyZoom;
import fargen.core.layer.LayerIslandExpand;
import fargen.core.layer.LayerStart;
import fargen.core.layer.LayerZoom;
import fargen.core.layer.biome.LayerSurfaceBiome;
import fargen.core.layer.biome.LayerSurfaceBiomeEdge;
import fargen.core.layer.biome.LayerSurfaceBiomeRemix;
import fargen.core.layer.biome.LayerSurfaceBiomeStart;
import fargen.core.layer.biome.LayerSurfaceRainfall;
import fargen.core.layer.biome.LayerSurfaceSoilRemix;
import fargen.core.layer.biome.LayerSurfaceSoilStart;
import fargen.core.layer.surface.LayerAddDeepOcean;
import fargen.core.layer.surface.LayerLake;
import fargen.core.layer.surface.LayerRiver;
import fargen.core.layer.surface.LayerRiverMix;
import fargen.core.layer.surface.LayerRiverStart;
import fargen.core.layer.surface.LayerShore;
import fargen.core.layer.surface.LayerSurfaceTerrain;
import fargen.core.layer.surface.LayerSurfaceTerrainEdge;
import nebula.Log;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public class Layers
{
	private static GenLayer wrapContinent(long seed)
	{
		GenLayer layer = new LayerStart(seed, 4);
		drawImage(256, layer, "01 Start");
		layer = new LayerFuzzyZoom(10L, layer);
		drawImage(256, layer, "02 Zoom");
		layer = new LayerIslandExpand(30L, layer, 1);
		drawImage(256, layer, "03 ExpandIsland");
		layer = new LayerFuzzyZoom(12L, layer);
		drawImage(256, layer, "04 Zoom");
		layer = new GenLayerSmooth(47L, layer);
		drawImage(256, layer, "05 Smooth");
		layer = new LayerIslandExpand(32L, layer, 1);
		drawImage(256, layer, "06 ExpandIsland");
		layer = new LayerZoom(14L, layer);
		drawImage(256, layer, "07 Zoom");
		layer = new LayerIslandExpand(34L, layer, 1);
		drawImage(256, layer, "08 ExpandIsland");
		layer = new LayerZoom(16L, layer);
		drawImage(256, layer, "09 Zoom");
		return layer;
	}
	
	private static GenLayer wrapZoom(String key, GenLayer layer, int state)
	{
		switch (state)
		{
		case 0:
			layer = new LayerZoom(6276L, 2, layer);
			drawImage(256, layer, "17-" + key + "-1 Zoom");
		case 1:
			layer = new GenLayerSmooth(29383L, layer);
			drawImage(256, layer, "17-" + key + "-2 Zoom End");
		case 2:
			layer = new LayerZoom(471827L, 2, layer);
			drawImage(256, layer, "17-" + key + " Zoom End");
		default:
		}
		return layer;
	}
	
	public static GenLayer[] wrapSurface(long seed)
	{
		GenLayer layer = wrapContinent(seed);
		layer = new LayerAddDeepOcean(100L, layer);
		drawImage(256, layer, "09 Deep Ocean");
		GenLayer layer1 = new LayerZoom(3843L, layer);
		drawImage(256, layer1, "10-a Zoom");
		layer1 = new LayerSurfaceTerrain(4718L, layer1);
		drawImage(256, layer1, "11-a Terrain");
		
		GenLayer layer2 = new LayerZoom(2739L, 2, layer1);
		drawImage(256, layer2, "10-b River Initalize Zoom");
		layer1 = new LayerLake(5716L, layer1);
		drawImage(256, layer1, "12-a Lake");
		layer1 = new LayerZoom(6001L, 2, layer1);
		drawImage(256, layer1, "13-a Terrain Zoom");
		layer1 = new LayerSurfaceTerrainEdge(7192L, layer1);
		drawImage(256, layer1, "14-a Terrain Edge");
		layer1 = new LayerZoom(6273L, layer1);
		drawImage(256, layer1, "15-a-1 Zoom");
		layer1 = new LayerIslandExpand(6274L, layer1, -1);
		drawImage(256, layer1, "14-a-2 Expand");
		layer1 = new LayerZoom(6275L, layer1);
		drawImage(256, layer1, "15-a-3 Zoom");
		layer1 = new LayerShore(13048L, layer1);
		drawImage(256, layer1, "16-a Shore");
		
		GenLayer layer4 = new LayerSurfaceBiomeStart(3L);
		drawImage(256, layer4, "40 Biome Start");
		layer4 = new LayerZoom(47L, 2, layer4);
		drawImage(256, layer4, "41 Biome Zoom");
		layer4 = new LayerSurfaceRainfall(48L, layer4);
		drawImage(256, layer4, "42 Biome Rainfall");
		layer4 = new GenLayerSmooth(87L, layer4);
		drawImage(256, layer4, "43 Smooth");
		
		GenLayer soil = new LayerSurfaceSoilStart(501L, layer4);
		drawImage(256, layer4, "51 Soil Start");
		soil = new GenLayerSmooth(4017L, soil);
		drawImage(256, layer1, "52 Soil Smooth");
		soil = wrapZoom("s", soil, 0);
		
		layer4 = new LayerSurfaceBiome(513L, layer4);
		drawImage(256, layer4, "45 Biome Mixed");
		layer4 = new GenLayerSmooth(4017L, layer4);
		drawImage(256, layer1, "46 Biome Smooth");
		
		layer1 = new LayerZoom(6276L, 2, layer1);
		drawImage(256, layer1, "17-a Zoom");
		
		layer4 = new LayerZoom(6276L, 2, layer4);
		drawImage(256, layer1, "47 Biome Zoom");
		layer4 = new LayerSurfaceBiomeEdge(4847L, layer4);
		drawImage(256, layer1, "48 Biome Edge");
		layer4 = wrapZoom("b", layer4, 1);
		
		layer2 = new LayerRiverStart(381L, layer2);
		drawImage(256, layer2, "11-b River Start");
		layer2 = new LayerZoom(9372L, layer2);
		layer2 = new LayerIslandExpand(9373L, layer2, -1);
		layer2 = new LayerZoom(18392L, 5, layer2);
		drawImage(256, layer2, "12-b River Zoom");
		layer2 = new LayerRiver(18398L, layer2);
		layer2 = new GenLayerSmooth(34728L, layer2);
		drawImage(256, layer2, "13-b River");
		layer2 = new GenLayerSmooth(38294L, layer2);
		
		layer1 = new GenLayerSmooth(29383L, layer1);
		GenLayer layer3 = new LayerRiverMix(384719L, layer1, layer2);
		
		drawImage(256, layer3, "15 River Mixed");
		layer3 = new LayerZoom(471827L, 2, layer3);
		drawImage(256, layer3, "16 Output");
		
		layer4 = new LayerSurfaceBiomeRemix(3847L, layer4, layer3);
		soil = new LayerSurfaceSoilRemix(4819L, soil, layer3);
		
		GenLayer layerResult = new GenLayerVoronoiZoom(5817L, layer4);
		drawImage(256, layer4, "17 Voronoi Zoom");
		soil = new GenLayerVoronoiZoom(5817L, soil);
		drawImage(256, layer4, "54 Soil Voronoi Zoom");
		
		GenLayer rock = new LayerRockStart(21L);
		drawImage(256, rock, "R1 Start");
		rock = new LayerFuzzyZoom(22L, 3, rock);
		drawImage(256, rock, "R2 Zoom");
		rock = new LayerRockRemappedData(39L, rock);
		drawImage(256, rock, "R3 Mapped");
		rock = new LayerZoom(48L, 2, rock);
		drawImage(256, rock, "R4 Zoom");
		rock = new GenLayerSmooth(59L, rock);
		drawImage(256, rock, "R5 Smooth");
		
		layerResult.initWorldGenSeed(seed);
		rock.initWorldGenSeed(seed);
		soil.initWorldGenSeed(seed);
		
		return new GenLayer[] { layer3, layerResult, rock, soil };
	}
	
	private static final boolean DRAW_IMG = false;
	
	public static void drawImage(int size, GenLayer genlayer, String name)
	{
		if (!DRAW_IMG) return;
		try
		{
			genlayer.initWorldGenSeed(84L);
			File outFile = new File(name + ".png");
			if (outFile.exists()) return;
			int[] ints = genlayer.getInts(0, 0, size, size);
			BufferedImage outBitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
			graphics.clearRect(0, 0, size, size);
			Log.info(name + ".png");
			for (int x = 0; x < size; x++)
			{
				for (int z = 0; z < size; z++)
				{
					int v = ints[(x * size + z)];
					if (v < 0)
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
