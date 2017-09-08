/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import net.minecraft.util.math.MathHelper;

/**
 * @author ueyudiud
 */
public final class Maths
{
	private Maths() {}
	
	/**
	 * Returns the largest {@code int} value is less
	 * than or equal to the argument.
	 * @param f a {@code float} value.
	 * @return a floor {@code int} value.
	 * @throws NumberFormatException if argument is illegal, too big or too small in integer range.
	 */
	public static int floori(float f) throws NumberFormatException
	{
		int v = Float.floatToRawIntBits(f);
		int e = ((v & 0x7f800000) >> 23) - 127;
		
		if (e < 0)
			return (v & 0x80000000) == 0 || (v & 0x007fffff) == 0 ? 0 : -1;
		else if (e == 127)
			throw new NumberFormatException("NaN or inf got.");
		else if (e >= 31)
			throw new NumberFormatException("Out of range.");
		else
		{
			e -= 23;
			int x = 0x00800000 | (v & 0x007fffff);
			if (e > 0) x <<= e; else if (e < 0) x >>= -e;
			return (v & 0x80000000) == 0 ? x :
				(v & (1 << e) - 1) == 0 ? -x :
					-x-1;
		}
	}
	
	/**
	 * Returns the largest {@code long} value is less
	 * than or equal to the argument.
	 * @param f a {@code float} value.
	 * @return a floor {@code long} value.
	 * @throws NumberFormatException if argument is illegal, too big or too small in long range.
	 */
	public static long floorl(float f)
	{
		int v = Float.floatToRawIntBits(f);
		int e = ((v & 0x7f800000) >> 23) - 127;
		
		if (e < 0)
			return (v & 0x80000000) == 0 || (v & 0x007fffff) == 0 ? 0 : -1;
		else if (e == 127)
			throw new NumberFormatException("NaN or inf got.");
		else if (e >= 63)
			throw new NumberFormatException("Out of range.");
		else
		{
			e -= 23;
			long x = 0x00800000 | (v & 0x007fffff);
			if (e > 0) x <<= e; else if (e < 0) x >>= -e;
			return (v & 0x80000000) == 0 ? x :
				(v & (1 << e) - 1) == 0 ? -x :
					-x-1;
		}
	}
	
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
	
	/**
	 * Returns the floor modulus of the {@code double} arguments.<p>
	 * The value of result will always be positive.
	 * @param a
	 * @param b
	 * @return a non-negative {@code double} value.
	 */
	public static strictfp double mod(double a, double b)
	{
		double v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	/**
	 * Returns the floor modulus of the {@code float} arguments.<p>
	 * The value of result will always be positive.
	 * @param a
	 * @param b
	 * @return a non-negative {@code float} value.
	 */
	public static strictfp float mod(float a, float b)
	{
		float v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	/**
	 * Returns the floor modulus of the {@code int} arguments.<p>
	 * The value of result will always be positive.
	 * @param a
	 * @param b
	 * @return a non-negative {@code int} value.
	 */
	public static int mod(int a, int b)
	{
		int v;
		return (v = a % b) >= 0 ? v : v + b;
	}
	
	/**
	 * Returns the floor modulus of the {@code long} arguments.<p>
	 * The value of result will always be positive.
	 * @param a
	 * @param b
	 * @return a non-negative {@code long} value.
	 */
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
	
	/**
	 * Take linear interpolation between two {@code float} value, and
	 * use a value to measure the point.
	 * @param a the first value.
	 * @param b the second value.
	 * @param x a {@code float} value which is predicated between 0.0 to 1.0.
	 * @return
	 */
	public static float lerp(float a, float b, float x)
	{
		return a + (b - a) * x;
	}
	
	/**
	 * Take linear interpolation between two {@code double} value, and
	 * use a value to measure the point.
	 * @param a the first value.
	 * @param b the second value.
	 * @param x a {@code double} value which is predicated between 0.0 to 1.0.
	 * @return
	 */
	public static double lerp(double a, double b, double x)
	{
		return a + (b - a) * x;
	}
	
	public static double invsqrt_fast(double a)
	{
		return MathHelper.fastInvSqrt(a);
	}
	
	/**
	 * Get the GCD of two {@code int} value.
	 * @param a
	 * @param b
	 * @return GCD
	 */
	public static int gcd(int a, int b)
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
	
	public static int gcd(int...is)
	{
		switch (is.length)
		{
		case 0 : return 1;
		case 1 : return is[0];
		case 2 : return gcd(is[0], is[1]);
		default:
			int a = is[0];
			for(int i = 1; i < is.length; ++i)
			{
				a = gcd(a, is[i]);
			}
			return a;
		}
	}
	
	public static double log_average(double a, double b)
	{
		return L.similar(a, b) ? a : a == 0 || b == 0 ? 0 : (a - b) / (Math.log(a) - Math.log(b));
	}
	
	public static float sq(float f)
	{
		return f * f;
	}
	
	public static double sq(double d)
	{
		return d * d;
	}
}