/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import net.minecraft.util.math.MathHelper;

/**
 * @author ueyudiud
 */
public class Maths
{
	public static double[][] gaussianL(int size, double sigma)
	{
		int size1 = size * 2 + 1;
		double t = 0D;
		double[][] ret = new double[size1][size1];
		double s2 = sigma * sigma;
		for(int i = 0; i < size1; ++i)
		{
			for(int j = 0; j < size1; ++j)
			{
				int i1 = i - size - 1;
				int j1 = j - size - 1;
				t += (ret[i][j] = Math.exp(- (i1 * i1 + j1 * j1) / (2 * s2)));
			}
		}
		for(int i = 0; i < size1; ++i)
		{
			for(int j = 0; j < size1; ++j)
			{
				ret[i][j] /= t;
			}
		}
		return ret;
	}
	
	public static float[][] gaussianLf(int size, float sigma)
	{
		int size1 = size * 2 + 1;
		float t = 0F;
		float[][] ret = new float[size1][size1];
		double s2 = sigma * sigma;
		for(int i = 0; i < size1; ++i)
		{
			for(int j = 0; j < size1; ++j)
			{
				int i1 = i - size - 1;
				int j1 = j - size - 1;
				t += (ret[i][j] = (float) Math.exp(- (i1 * i1 + j1 * j1) / (2 * s2)));
			}
		}
		for(int i = 0; i < size1; ++i)
		{
			for(int j = 0; j < size1; ++j)
			{
				ret[i][j] /= t;
			}
		}
		return ret;
	}
	
	public static double mod(double a, double b)
	{
		double v;
		return (v = a % b) > 0 ? v : v + b;
	}
	
	public static float mod(float a, float b)
	{
		float v;
		return (v = a % b) > 0 ? v : v + b;
	}
	
	public static int mod(int a, int b)
	{
		int v;
		return (v = a % b) > 0 ? v : v + b;
	}
	
	public static long mod(long a, long b)
	{
		long v;
		return (v = a % b) > 0 ? v : v + b;
	}
	
	public static float average(float...floats)
	{
		float j = 0;
		for(float i : floats)
		{
			j += i;
		}
		return j / floats.length;
	}
	
	public static double average(double...doubles)
	{
		double j = 0;
		for(double i : doubles)
		{
			j += i;
		}
		return j / doubles.length;
	}
	
	public static float lerp(float a, float b, float x)
	{
		return a + (b - a) * x;
	}
	
	public static double lerp(double a, double b, double x)
	{
		return a + (b - a) * x;
	}
	
	public static double invsqrt_fast(double a)
	{
		return MathHelper.fastInvSqrt(a);
	}
}