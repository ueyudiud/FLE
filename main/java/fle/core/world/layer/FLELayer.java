package fle.core.world.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import fle.api.util.FleLog;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoisePerlin;
import fle.core.world.biome.FLEBiome;
import fle.core.world.dim.FLESuperFlatSurfaceChunkProvider;
import fle.core.world.dim.FLEWorldType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract class FLELayer extends GenLayer
{
	public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType type)
    {
        boolean flag = false;
        if(type == FLEWorldType.FLAT)
        {
        	FLELayerFlat gen0 = new FLELayerFlat();
            drawImage(512, gen0, "0 ContinentsStart");
        	FLELayerFloor gen2 = new FLELayerFloor(new NoiseMix(4D, new NoisePerlin(1001L, 2)), new NoiseMix(4D, new NoisePerlin(1003L, 2)), gen0, 10L);
            drawImage(512, gen2, "1 ContinentsFloor");
        	FLELayerZoom2 gen1 = new FLELayerZoom2(8, 29L, gen2);
        	drawImage(512, gen1, "2 ContinentsZoom");
        	FLELayerEdge gen3 = new FLELayerEdge(2928L, gen1);
        	drawImage(512, gen3, "3 ContinentsEdge");
        	gen3 = new FLELayerEdge(528L, gen3);
        	drawImage(512, gen3, "4 ContinentsEdge");
        	gen1 = new FLELayerZoom2(2, seed, gen3);
        	drawImage(512, gen1, "5 ContinentsZoom");
        	return new GenLayer[]{gen1, gen1, gen1};
        }
        FLELayerSinglePixel gen0 = new FLELayerSinglePixel(true, 15L, 2L + seed);
        drawImage(512, gen0, "0 ContinentsStart");
        GenLayerZoom gen1 = new GenLayerZoom(2000L, gen0);
        drawImage(512, gen1, "1 ContinentsZoom");
        FLELayerAddPixel gen2 = new FLELayerAddPixel(15L, gen1, FLEBiome.warm_plains.biomeID, 1L);
        drawImage(512, gen2, "2 ContinentsAddIsland");
        gen1 = new GenLayerZoom(9L, gen2);
        drawImage(512, gen1, "3 ContinentsZoom");
        gen2 = new FLELayerAddPixel(31L, gen1, FLEBiome.warm_plains.biomeID, 16L);
        drawImage(512, gen2, "4 ContinentsAddIsland");
        gen2 = new FLELayerAddPixel(63L, gen2, FLEBiome.warm_plains.biomeID, 32L);
        drawImage(512, gen2, "5 ContinentsAddIsland");
        gen2 = new FLELayerAddPixel(127L, gen2, FLEBiome.warm_plains.biomeID, 64L);
        drawImage(512, gen2, "6 ContinentsAddIsland");
        FLELayerZoom2 gen3 = new FLELayerZoom2(1, 255L, gen2);
        drawImage(512, gen3, "7 ContinentsZoom");
        FLELayerFloor gen4;
        FLELayerMontain gen6 = new FLELayerMontain(4L, gen3, new NoiseMix(32D, new NoisePerlin(1L, 2)));
        drawImage(512, gen6, "8 ContinentsMontain");
        gen4 = new FLELayerFloor(new NoiseMix(16D, new NoisePerlin(1001L, 2)), new NoiseMix(16D, new NoisePerlin(1003L, 2)), gen6, 10L);
        drawImage(512, gen4, "9 ContinentsFloor");
        gen1 = new GenLayerZoom(99L, gen4);
        drawImage(512, gen1, "10 ContinentsZoom");
        gen1 = new GenLayerZoom(2L, gen1);
        drawImage(512, gen1, "11 ContinentsZoom");
        FLELayerEdge gen5 = new FLELayerEdge(32L, gen1);
        drawImage(512, gen5, "12 ContinentsEdge");
        gen5 = new FLELayerEdge(33L, gen5);
        drawImage(512, gen5, "13 ContinentsZoom");
        //gen5 = new FLELayerEdge(33L, gen5);
        //drawImage(512, gen5, "14 ContinentsEdge");
        gen1 = new GenLayerZoom(2L, gen5);
        drawImage(512, gen1, "14 ContinentsZoom");
        FLELayerBeach gen11 = new FLELayerBeach(72L, gen1);
        drawImage(512, gen11, "15 ContinentsBeach");
        gen3 = new FLELayerZoom2(2, 2L, gen11);
        drawImage(512, gen3, "16 ContinentsZoom");
        FLELayerRiver gen7 = new FLELayerRiver(13, 9L);
        drawImage(512, gen7, "17 ContinentsRiver");
        GenLayerSmooth gen8 = new GenLayerSmooth(192L, gen7);
        drawImage(512, gen8, "18 ContinentsSmooth");
        FLELayerZoom2 gen10 = new FLELayerZoom2(2, 294L, gen8);
        drawImage(512, gen10, "18 ContinentsZoom");
        FLELayerRiverMix gen9 = new FLELayerRiverMix(gen3, gen10, 1L);
        drawImage(512, gen9, "19 ContinentsRiverMix");
        //GenLayerRiver genRiver = new GenLayerRiver(31L, gen3);
        //drawImage(512, genRiver, "15-beta ContinentsRiver");
        //GenLayerSmooth genlayersmooth = new GenLayerSmooth(999L, genRiver);
        //drawImage(512, genlayersmooth, "16-beta ContinentsSmooth");
        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(999L, gen3);
        drawImage(512, genlayersmooth1, "25-beta ContinentsSmooth");
        if(type == FLEWorldType.LARGE_BIOMES)
        {
        	gen3 = new FLELayerZoom2(6, 294L, genlayersmooth1);
        	genlayersmooth1 = new GenLayerSmooth(284L, gen3);
        	gen10 = new FLELayerZoom2(6, 294L, gen9);
        	gen8 = new GenLayerSmooth(481L, gen10);
        }
        //GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        //drawImage(512, genlayerrivermix, "26-beta ContinentsRiverMix");
        //GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        //drawImage(512, genlayervoronoizoom, "27-beta ContinentsVoronoiZoom");
        genlayersmooth1.initWorldGenSeed(seed);
        gen9.initWorldGenSeed(seed);
        //genlayervoronoizoom.initWorldGenSeed(seed);
        return type == FLEWorldType.LARGE_BIOMES ? new GenLayer[]{gen8, gen3, gen8} : new GenLayer[]{gen9, genlayersmooth1, gen9};
    }
	
	private static boolean shouldDraw = false;
	
	public static void drawImage(int size, GenLayer genlayer, String name)
	{
	    if (!shouldDraw)
	    {
	    	return;
	    }
	    try
	    {
	    	File outFile = new File(name + ".png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	int[] ints = genlayer.getInts(256, 256, size, size);
	    	BufferedImage outBitmap = new BufferedImage(size, size, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	FleLog.getLogger().info(name + ".png");
	    	for (int x = 0; x < size; x++)
	    	{
	    		for (int z = 0; z < size; z++)
	    		{
	    			if ((ints[(x * size + z)] != -1) && (BiomeGenBase.getBiomeGenArray()[ints[(x * size + z)]] != null))
	    			{
	    				graphics.setColor(new Color(BiomeGenBase.getBiome(ints[(x * size + z)]).color));
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
	
	public FLELayer(long seed)
	{
		super(seed);
	}

	@Override
	public abstract int[] getInts(int x, int z, int w, int h);
	
	protected int choose(int...is)
	{
		return is[nextInt(is.length)];
	}
}