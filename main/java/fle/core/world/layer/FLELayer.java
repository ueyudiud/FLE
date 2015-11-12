package fle.core.world.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import fle.api.util.FleLog;
import fle.core.util.noise.NoiseBase;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoisePerlin;
import fle.core.world.biome.FLEBiome;
import fle.core.world.dim.FLEWorldType;

public abstract class FLELayer extends GenLayer
{	
	public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType type)
    {
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
        	gen1.initWorldGenSeed(seed);
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
        FLELayerMontain gen6 = new FLELayerMontain(4L, gen3, new NoiseMix(32D, new NoisePerlin(1L, 2)));
        drawImage(512, gen6, "8 ContinentsMontain");
        gen1 = new GenLayerZoom(99L, gen6);
        drawImage(512, gen1, "9 ContinentsZoom");
        gen1 = new GenLayerZoom(2L, gen1);
        drawImage(512, gen1, "10 ContinentsZoom");
        FLELayerFloor gen4;
        gen4 = new FLELayerFloor(new NoiseMix(87D, new NoisePerlin(1001L, 2)), new NoiseMix(83D, new NoisePerlin(1003L, 2)), gen1, 10L);
        drawImage(512, gen4, "11 ContinentsFloor");
        FLELayerEdge gen5 = new FLELayerEdge(32L, gen4);
        drawImage(512, gen5, "12 ContinentsEdge");
        gen5 = new FLELayerEdge(33L, gen5);
        drawImage(512, gen5, "13 ContinentsZoom");
        gen1 = new GenLayerZoom(2L, gen5);
        drawImage(512, gen1, "14 ContinentsZoom");
        FLELayerBeach gen11 = new FLELayerBeach(72L, gen1);
        drawImage(512, gen11, "15 ContinentsBeach");
        gen3 = new FLELayerZoom2(1, 2L, gen11);
        drawImage(512, gen3, "16 ContinentsZoom");
        GenLayerSmooth gen13 = new GenLayerSmooth(999L, gen3);
        drawImage(512, gen13, "17 ContinentsSmooth");

        FLELayerRiver gen7 = new FLELayerRiver(8, 38L);
        drawImage(512, gen7, "18 ContinentsRiver");
        FLELayerZoom2 gen10 = new FLELayerZoom2(1, 294L, gen7);
        drawImage(512, gen10, "19 ContinentsZoom");
        GenLayerSmooth gen8 = new GenLayerSmooth(192L, gen10);
        drawImage(512, gen8, "20 ContinentsSmooth");
        FLELayerRiverMix gen9 = new FLELayerRiverMix(gen13, gen8, 183L);
        drawImage(512, gen9, "21 ContinentsRiverMix");
        if(type == FLEWorldType.LARGE_BIOMES)
        {
        	gen3 = new FLELayerZoom2(6, 294L, gen9);
        	gen13 = new GenLayerSmooth(284L, gen3);
        	GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(10L, gen13);
            gen12.initWorldGenSeed(seed);
            gen13.initWorldGenSeed(seed);
            drawImage(512, gen12, "22-beta ContinentsVoronoiZoom");
            return new GenLayer[]{gen13, gen12, gen13};
        }
        //GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(27L + seed, gen9);
        GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(27L + seed, gen13);
        gen9.initWorldGenSeed(seed);
        gen12.initWorldGenSeed(seed);
        drawImage(512, gen12, "22-beta ContinentsVoronoiZoom");
        return new GenLayer[]{gen13, gen12, gen13};
    }

	public static GenLayer[] initializeAllNetherBiomeGenerators(long seed,
			WorldType type)
	{
        boolean flag = false;
        if(type == FLEWorldType.FLAT)
        {
        	FLELayerNetherFloor gen2 = new FLELayerNetherFloor(new NoiseMix(4D, new NoisePerlin(1001L, 2)), new NoiseMix(4D, new NoisePerlin(1003L, 2)), 10L);
            drawImage(512, gen2, "0 ContinentsFloor");
        	FLELayerZoom2 gen1 = new FLELayerZoom2(8, 29L, gen2);
        	drawImage(512, gen1, "1 ContinentsZoom");
        	FLELayerEdge gen3 = new FLELayerEdge(2928L, gen1);
        	drawImage(512, gen3, "2 ContinentsEdge");
        	gen3 = new FLELayerEdge(528L, gen3);
        	drawImage(512, gen3, "3 ContinentsEdge");
        	gen1 = new FLELayerZoom2(2, seed, gen3);
        	drawImage(512, gen1, "4 ContinentsZoom");
        	gen1.initWorldGenSeed(seed);
        	return new GenLayer[]{gen1, gen1, gen1};
        }
        GenLayerZoom gen1;
        FLELayerZoom2 gen3;
        FLELayerNetherFloor gen4;
        gen4 = new FLELayerNetherFloor(new NoiseMix(87D, new NoisePerlin(1001L, 2)), new NoiseMix(83D, new NoisePerlin(1003L, 2)), 10L);
        drawImage(512, gen4, "1 ContinentsFloor");
        FLELayerEdge gen5 = new FLELayerEdge(32L, gen4);
        drawImage(512, gen5, "2 ContinentsEdge");
        gen5 = new FLELayerEdge(33L, gen5);
        drawImage(512, gen5, "3 ContinentsZoom");
        gen1 = new GenLayerZoom(2L, gen5);
        drawImage(512, gen1, "4 ContinentsZoom");
        gen3 = new FLELayerZoom2(1, 2L, gen1);
        drawImage(512, gen3, "5 ContinentsZoom");
        GenLayerSmooth gen13 = new GenLayerSmooth(999L, gen3);
        drawImage(512, gen13, "6 ContinentsSmooth");
        
        if(type == FLEWorldType.LARGE_BIOMES)
        {
        	gen3 = new FLELayerZoom2(6, 294L, gen13);
        	gen13 = new GenLayerSmooth(284L, gen3);
        	GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(10L, gen13);
            gen12.initWorldGenSeed(seed);
            gen13.initWorldGenSeed(seed);
            drawImage(512, gen12, "7-beta ContinentsVoronoiZoom");
            return new GenLayer[]{gen13, gen12, gen13};
        }
        //GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(27L + seed, gen9);
        GenLayerVoronoiZoom gen12 = new GenLayerVoronoiZoom(27L + seed, gen13);
        gen13.initWorldGenSeed(seed);
        gen12.initWorldGenSeed(seed);
        drawImage(512, gen12, "7-beta ContinentsVoronoiZoom");
        return new GenLayer[]{gen13, gen12, gen13};
	}
	
	private static boolean shouldDraw = false;
	
	private static synchronized void set()
	{
		shouldDraw = !shouldDraw;
	}
	
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
	    	int[] ints = genlayer.getInts(0, 0, size, size);
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