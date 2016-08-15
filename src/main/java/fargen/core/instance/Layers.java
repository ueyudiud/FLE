package fargen.core.instance;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import farcore.lib.util.Log;
import farcore.lib.util.NoisePerlin;
import fargen.core.layer.Layer;
import fargen.core.layer.LayerAreaAdd;
import fargen.core.layer.LayerFuzzyZoom;
import fargen.core.layer.LayerRing;
import fargen.core.layer.LayerSmooth;
import fargen.core.layer.LayerStartNoise;
import fargen.core.layer.LayerStartRand;
import fargen.core.layer.LayerZoom;
import fargen.core.layer.biome.LayerBaseBiome;
import fargen.core.layer.biome.LayerBiomeEdge;
import fargen.core.layer.biome.LayerBiomeSurfaceMixed;
import fargen.core.layer.biome.LayerTempEdge;
import fargen.core.layer.biome.LayerTempZoom;
import fargen.core.layer.chunk.LayerChunkEdge;
import fargen.core.layer.terrain.LayerRiver;
import fargen.core.layer.terrain.LayerTerrainBase;
import fargen.core.util.LayerProp;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public class Layers
{
	private static final boolean DRAW_IMG = false;
	
	public static LayerProp wrapSurface(long seed)
	{
		LayerProp prop = new LayerProp();
		//Wrap chunk layer.
		Layer layer1 = new LayerStartRand(1L, 8);
		drawImage(256, layer1, "l_01_chunk");
		layer1 = new LayerZoom(4, 2L, layer1);
		drawImage(256, layer1, "l_02_zoom");
		layer1 = new LayerSmooth(3L, layer1);
		drawImage(256, layer1, "l_03_smooth");
		layer1 = new LayerChunkEdge(4L, layer1);
		drawImage(256, layer1, "l_04_chunk_edge");
		layer1 = new LayerZoom(6, 5L, layer1);
		drawImage(256, layer1, "l_05_zoom");
		//Land layer.
		Layer layer2 = new LayerStartNoise(3729L, 16,
				prop.heightNoise = new NoisePerlin(6L, 3, 64.0, 1.5, 1.8));
		drawImage(256, layer2, "l_10_height");
		prop.heightLayer = layer2;
		layer2 = new LayerTerrainBase(37383L, layer2, layer1);
		drawImage(256, layer2, "l_11_terrain_base");
		layer2 = new LayerSmooth(917L, layer2);
		drawImage(256, layer2, "l_12_smooth");
		//River layer.
		Layer layer3 = new LayerStartRand(38190L, 6);
		drawImage(256, layer3, "l_20_river_start");
		layer3 = new LayerFuzzyZoom(927L, layer3);
		layer3 = new LayerAreaAdd(2, 30, 2.5F, 272L, layer3);
		drawImage(256, layer3, "l_21_add");
		layer3 = new LayerFuzzyZoom(127L, layer3);
		layer3 = new LayerAreaAdd(3, 22, 1.3F, 274L, layer3);
		drawImage(256, layer3, "l_22_add");
		layer3 = new LayerFuzzyZoom(2, 127L, layer3);
		layer3 = new LayerSmooth(9278L, layer3);
		layer3 = new LayerZoom(3, 129L, layer3);
		layer3 = new LayerRing(2874L, layer3);
		layer3 = new LayerSmooth(3729L, layer3);
		drawImage(256, layer3, "l_23_ring");
		//Land Layer.
		layer2 = new LayerRiver(5819L, layer2, layer3);
		drawImage(256, layer2, "l_14_river_mix");
		//		layer2 = new LayerZoom(279371L, layer2);
		//		drawImage(256, layer2, "l_15_zoom");
		Layer layer4 = new LayerSmooth(39172940L, layer2);
		drawImage(256, layer4, "l_40_terrain_wrap");
		//Biome Layer
		Layer layer_temp = new LayerStartRand(10001L, 9);
		layer_temp = new LayerTempEdge(617L, layer_temp);
		layer_temp = new LayerTempEdge(618L, layer_temp);
		layer_temp = new LayerTempZoom(3829L, layer_temp);
		layer_temp = new LayerTempEdge(619L, layer_temp);
		layer_temp = new LayerTempEdge(620L, layer_temp);
		layer_temp = new LayerTempZoom(3830L, layer_temp);
		layer_temp = new LayerTempEdge(629L, layer_temp);
		layer_temp = new LayerTempZoom(3831L, layer_temp);
		drawImage(256, layer_temp, "l_50_temperature");
		Layer layer_humidity = new LayerStartRand(10002L, 11);
		layer_humidity = new LayerTempEdge(3781L, layer_humidity);
		layer_humidity = new LayerTempEdge(3782L, layer_humidity);
		layer_humidity = new LayerTempZoom(3929L, layer_humidity);
		layer_humidity = new LayerTempEdge(3784L, layer_humidity);
		layer_humidity = new LayerTempEdge(3785L, layer_humidity);
		layer_humidity = new LayerTempZoom(3930L, layer_humidity);
		layer_humidity = new LayerTempEdge(3786L, layer_humidity);
		layer_humidity = new LayerTempZoom(3931L, layer_humidity);
		drawImage(256, layer_humidity, "l_51_humidity");
		Layer layer5 = new LayerBaseBiome(472827L, layer_temp, layer_humidity);
		drawImage(256, layer5, "l_52_basebiome");
		layer5 = new LayerBiomeEdge(36280L, layer5);
		layer5 = new LayerZoom(2, 91827L, layer5);
		layer5 = new LayerBiomeEdge(36281L, layer5);
		layer5 = new LayerZoom(86182L, layer5);
		layer5 = new LayerBiomeEdge(36282L, layer5);
		drawImage(256, layer5, "l_52_edge");
		layer5 = new LayerBiomeSurfaceMixed(283918L, layer5, layer4);
		layer5 = new LayerZoom(86192L, layer5);
		layer5 = new LayerSmooth(3819L, layer5);
		drawImage(256, layer5, "l_53_mixed");
		layer2 = new LayerZoom(86192L, layer2);
		GenLayer out = new GenLayerVoronoiZoom(371923L, layer5);
		drawImage(256, out, "l_54_voroni");
		out.initWorldGenSeed(seed);
		layer5.markZoom(1);
		prop.terrainLayer = layer2;
		prop.biomeLayer1 = layer5;
		prop.biomeLayer2 = out;
		prop.markZoom();
		return prop;
	}
	
	public static void drawImage(int size, GenLayer genlayer, String name)
	{
		if (!DRAW_IMG)
			return;
		try
		{
			genlayer.initWorldGenSeed(82L);
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