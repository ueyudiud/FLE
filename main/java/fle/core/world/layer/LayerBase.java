package fle.core.world.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import farcore.lib.world.biome.BiomeBase;
import farcore.util.FleLog;
import farcore.util.V;
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