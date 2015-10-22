package fle.core.util;

public class FLEMath
{
	public static <T> T[] selectCollection(int x, int y, int u, int v, int w, int h, T[] input, T[] output)
	{
		if(input.length != x * y || output.length != u * v) throw new RuntimeException();
		if(u + w > x) throw new ArrayIndexOutOfBoundsException(u + w);
		if(v + h > y) throw new ArrayIndexOutOfBoundsException(v + h);
		T[] ret = output;
		for(int i = 0; i < h; ++i)
		{
			System.arraycopy(input, u + (v + i) * y, ret, i * w, w);
		}
		return ret;
	}
	public static double[] selectCollection(int x, int y, int u, int v, int w, int h, double[] input)
	{
		if(input.length != x * y) throw new RuntimeException();
		if(u + w > x) throw new ArrayIndexOutOfBoundsException(u + w);
		if(v + h > y) throw new ArrayIndexOutOfBoundsException(v + h);
		double[] ret = new double[w * h];
		for(int i = 0; i < h; ++i)
		{
			System.arraycopy(input, u + (v + i) * y, ret, i * w, w);
		}
		return ret;
	}
	public static long[] selectCollection(int x, int y, int u, int v, int w, int h, long[] input)
	{
		if(input.length != x * y) throw new RuntimeException();
		if(u + w > x) throw new ArrayIndexOutOfBoundsException(u + w);
		if(v + h > y) throw new ArrayIndexOutOfBoundsException(v + h);
		long[] ret = new long[w * h];
		for(int i = 0; i < h; ++i)
		{
			System.arraycopy(input, u + (v + i) * y, ret, i * w, w);
		}
		return ret;
	}
	
	public static long iDivide(long num, long over)
	{
		return (long) Math.floor((double) num / (double) over);
	}
	
	public static double fPart(double num)
	{
		return mod(num, 1D);
	}
	
	public static double gRound(double num, double over)
	{
		return mod(num, over) / over;
	}
	
	public static double gRound(long num, long over)
	{
		return (double) mod(num, over) / (double) over;
	}
	
	public static double mod(double num, double over)
	{
		double ret = num % over;
		return ret < 0D ? ret + over : ret;
	}
	
	public static long mod(long num, long over)
	{
		long ret = num % over;
		return ret < 0L ? ret + over : ret;
	}
	
	public static double linear(double a, double b, double linear)
	{
		return linear > 1D ? b : linear < 0D ? a : a + (b - a) * linear;
	}
	
	public static strictfp double alpha(double size, double p)
	{
		double a = p / size;
		return a > 1D || a < -1D ? 0 : 2D - 2 / (a * a * a * a - 2 * a * a + 2);
	}
	
	public static strictfp double alpha(double p)
	{
		return p > 1D || p < -1D ? 0 : 2D - 2 / (p * p * p * p - 2 * p * p + 2);
	}
	
	public static double distance(double x, double y, double z)
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public static double distance(double x, double y)
	{
		return Math.sqrt(x * x + y * y);
	}
}