/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

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
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	public static float mod(float a, float b)
	{
		float v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	public static int mod(int a, int b)
	{
		int v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	public static long mod(long a, long b)
	{
		long v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	public static float sum(float...floats)
	{
		float ret = 0;
		for (float f : floats) ret += f;
		return ret;
	}
	
	public static double sum(double...doubles)
	{
		double ret = 0;
		for (double d : doubles) ret += d;
		return ret;
	}
	
	public static float average(float...floats)
	{
		return sum(floats) / floats.length;
	}
	
	public static double average(double...doubles)
	{
		return sum(doubles) / doubles.length;
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
	
	public static int commonDiv(int a, int b)
	{
		if(b > a)
		{
			a ^= b;
			b ^= a;
			a ^= b;
		}
		do
		{
			int temp = a % b;
			a = b;
			b = temp;
		}
		while (b != 0);
		return a;
	}
	
	public static int commonDiv(int...is)
	{
		switch (is.length)
		{
		case 0 : return 1;
		case 1 : return is[0];
		case 2 : return commonDiv(is[0], is[1]);
		default:
			int a = is[0];
			for(int i = 1; i < is.length; ++i)
			{
				a = commonDiv(a, is[i]);
			}
			return a;
		}
	}
}