package fle.core.util.noise;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import fle.api.util.FleLog;
import fle.core.util.FLEMath;
import fle.core.world.dim.FLESurfaceChunkProvider;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class VecNoiseHandler
{	
	/**
	 * Debug mode.
	 * @param args
	 */
	public static void main(String[] args)
	{
		FLESurfaceChunkProvider.drawImage(128, "Debug Chunk");
		drawImage(1024, "Debug", new NoiseMix(2, 16, new NoisePerlin(38573013L, 2)), new VecNoisePerlin(3759180L, 3, 1.0F));
	}
	
	final static double basic;
	
	static
	{
		double a = 0;
		for(int j0 = -2; j0 <= 2; ++j0)
			for(int j1 = -2; j1 <= 2; ++j1)
				a += FLEMath.alpha(2D, FLEMath.distance(j0, j1));
		basic = a;
	}
	
	public static double[] getValue(int x, int z, NoiseBase height, VecNoiseBase noise, int size)
	{
		/**
		double[] a = a(x - 1, z - 1, height, noise, size + 2);
		double[] ret = new double[size * size];
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
			{
				double v = 0;
				for(int k = -1; k <= 1; ++k)
					for(int l = -1; l <= 1; ++l)
					{
						v += a[(i + k + 1) * (size + 2) + (j + l + 1)] * ((k | l) == 0x0 ? 0.75 : 0.03125);
					}
				ret[i * size + j] = v;
			}
		return ret;
		 */
		return a(x, z, height, noise, size);
	}
	
	private static double[] a(int x, int z, NoiseBase height, VecNoiseBase noise, int size)
	{
		Vec[] vecNoise = noise.noise(null, x - 3, z - 3, size + 6, size + 6);
		double[] heightNoise = height.noise(null, x - 3, z - 3, size + 6, size + 6);
		double[] ret = new double[size * size];
		for(int i0 = 0; i0 < size; ++i0)
			for(int i1 = 0; i1 < size; ++i1)
			{
				double a = 0;
				for(int j0 = -3; j0 <= 3; ++j0)
					for(int j1 = -3; j1 <= 3; ++j1)
					{
						Vec vec = vecNoise[i0 + j0 + 3 + (i1 + j1 + 3) * (size + 6)];
						double o = FLEMath.alpha(2D, FLEMath.distance(j0 + vec.xCoord, j1 + vec.zCoord));
						double b = heightNoise[i0 + j0 + 3 + (i1 + j1 + 3) * (size + 6)];
						b = b * 0.8F + 0.2F;
						a += b * o;
					}
				a /= basic;
				a = a * 0.9D + 0.1D;
				if(a > 1.0D)
				{
					a = (a - 1D) / 2D + 1D;
				}
				else if(a < -1.0D)
				{
					a = (a + 1D) / 2D - 1D;
				}
				
				if(a > 1.0D) a = 1.0D;
				else if(a < -1.0D) a = -1.0D;
				ret[i0 + i1 * size] = a;
			}
		return ret;
	}
	
	public static void drawImage(int size, String name, NoiseBase height, VecNoiseBase noise)
	{
	    try
	    {
	    	File outFile = new File(name + ".png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	double[] ds = getValue(0, 0, height, noise, size);
	    	int[] ints = new int[size * size];
	    	for(int i = 0; i < ds.length; ++i)
	    		ints[i] = (int) (ds[i] * 64 + 128);
	    	BufferedImage outBitmap = new BufferedImage(size, size, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	FleLog.getLogger().info(name + ".png");
	    	for (int x = 0; x < size; x++)
	    	{
	    		for (int z = 0; z < size; z++)
	    		{
	    			graphics.setColor(new Color(0x010101 * ints[size * z + x]));
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