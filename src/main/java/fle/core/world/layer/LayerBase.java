package fle.core.world.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import farcore.debug.Debug;
import farcore.lib.world.biome.BiomeBase;
import farcore.util.FleLog;
import farcore.util.V;
import farcore.util.noise.NoisePerlin;
import fle.core.world.layer.surface.LayerBeach;
import fle.core.world.layer.surface.LayerEdge;
import fle.core.world.layer.surface.LayerFloor;
import fle.core.world.layer.surface.LayerRainfall;
import fle.core.world.layer.surface.LayerRiver;
import fle.core.world.layer.surface.LayerSort;
import fle.core.world.layer.surface.LayerTemp;
import fle.core.world.layer.surface.LayerTerrainBase;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public abstract class LayerBase extends GenLayer
{
	public static GenLayer[] wrapSuface(long seed, WorldType type)
	{
		LayerBase orLayer = new LayerStart(11, 1L);
		drawImage(256, orLayer, "layer1.0");
		orLayer = new LayerFuzzyZoom(3L, orLayer);
		drawImage(256, orLayer, "layer1.1");
		orLayer = new LayerAdd(1, 9, 2L, orLayer);
		drawImage(256, orLayer, "layer1.2");
		orLayer = new LayerFuzzyZoom(3, 8L, orLayer);
		drawImage(256, orLayer, "layer1.3");
		orLayer = new LayerLinkMainland(3, 381L, orLayer);
		drawImage(256, orLayer, "layer2");
		orLayer = new LayerZoom(9031L, orLayer);
		orLayer = new LayerLinkMainland(1, 381L, orLayer);
		orLayer = new LayerZoom(5, 47L, orLayer);
		drawImage(256, orLayer, "layer3");
//		LayerTemp tempLayer = new LayerTemp(new NoisePerlin(1L, 8, 0.81D, 2D, 3D), 2839L);
		LayerTerrainBase terrainLayer = new LayerTerrainBase(new NoisePerlin(101L, 6, 5L, 0.48D, 1.8D), 38917L);
//		LayerRainfall rainfallLayer = new LayerRainfall(new NoisePerlin(201L, 7, 0.32D, 1.4D, 1.5D), 37292L, terrainLayer);
		orLayer = new LayerFloor(718L, orLayer, terrainLayer);
//		drawImage(256, orLayer, "layer4");
		LayerBase riverLayer = new LayerStart(9, 901L);
		drawImage(256, riverLayer, "layer4.0");
		riverLayer = new LayerFuzzyZoom(927L, riverLayer);
		riverLayer = new LayerAreaAdd(2, 30, 2.5F, 272L, riverLayer);
		drawImage(256, riverLayer, "layer4.1");
		riverLayer = new LayerFuzzyZoom(127L, riverLayer);
		riverLayer = new LayerAreaAdd(3, 25, 1.2F, 274L, riverLayer);
		drawImage(256, riverLayer, "layer4.2");
		riverLayer = new LayerFuzzyZoom(127L, riverLayer);
		riverLayer = new LayerAreaAdd(3, 20, 0.6F, 279L, riverLayer);
		drawImage(256, riverLayer, "layer4.3");
		riverLayer = new LayerFuzzyZoom(3, 127L, riverLayer);
		riverLayer = new LayerRing(2874L, riverLayer);
		drawImage(256, riverLayer, "layer4.4");
		orLayer = new LayerRiver(5819L, new LayerFuzzyZoom(2, 27L, orLayer), riverLayer);
		drawImage(256, orLayer, "layer4.5");
//		orLayer = new LayerEdge(508L, orLayer);
		orLayer = new LayerBeach(71628L, orLayer);
		drawImage(256, orLayer, "layer5");
		orLayer = new LayerZoom(3, 384918L, orLayer);
		orLayer = new LayerSort(381L, orLayer, new LayerZoom(27048L, new LayerRing(381L, new LayerZoom(4, 283L, new LayerAdd(2, 3, 29L, new LayerZoom(2, 2L, new LayerStart(3, -2L)))))));
		drawImage(256, orLayer, "layer6");
		GenLayer out1 = new GenLayerSmooth(289471L, orLayer);
		GenLayer out2 = new GenLayerVoronoiZoom(94719L, out1);
		out2.initWorldGenSeed(seed);
		orLayer.setScale(1);
		return new GenLayer[]{out1, out2, out1};
	}
	
	protected int expand = 1;
	private int scale = 1;
	
	public LayerBase(long seed)
	{
		super(seed);
	}
	
	public abstract int[] getInts(int x, int y, int w, int h);
	
	public int getScale()
	{
		return scale;
	}
	
	public void setScale(int scale)
	{
		this.scale = scale;
		if(parent instanceof LayerBase)
		{
			((LayerBase) parent).setScale(scale * expand);
		}
	}
	
	public static void drawImage(int size, GenLayer genlayer, String name)
	{
	    if (!V.debug)
	    {
	    	return;
	    }
	    try
	    {
	    	genlayer.initWorldGenSeed(4L);
	    	File outFile = new File(name + ".png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	int[] ints = genlayer.getInts(0, 0, size, size);
	    	BufferedImage outBitmap = new BufferedImage(size, size, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	FleLog.getLogger().info(name + ".png");
	    	for (int x = 0; x < size; x++)
	    	{
	    		for (int z = 0; z < size; z++)
	    		{
	    			if ((ints[(x * size + z)] != -1) && (BiomeBase.getBiomeGenArray()[ints[(x * size + z)]] != null))
	    			{
	    				graphics.setColor(new Color(BiomeBase.getBiome(ints[(x * size + z)]).color));
	    				graphics.drawRect(x, z, 1, 1);
	    			}
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