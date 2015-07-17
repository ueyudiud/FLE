package fla.core.util;

import fla.core.util.MathHelper.Function.LinearFunction;

public class MathHelper 
{
	public static LinearFunction setupLFunction(int a2, int b2)
	{
		return new LinearFunction(b2 / a2, 0);
	}
	public static LinearFunction setupLFunction(int b1, int a2, int b2)
	{
		int k = (b2 - b1) / a2;
		return new LinearFunction(k, b1);
	}
	public static LinearFunction setupLFunction(int a1, int b1, int a2, int b2)
	{
		int k = (b1 - b2) / (a1 - a2);
		return new LinearFunction(k, b1 - a1 * k);
	}
	
	public static Integer[] transferInteger(int...is)
	{
		Integer[] ret = new Integer[is.length];
		for(int i = 0; i < ret.length; ++i)
		{
			ret[i] = is[i];
		}
		return ret;
	}
	public static double distribution(double a, double b, double c)
	{
		return 1 / (c * Math.sqrt(2 * Math.PI)) * Math.exp(- Math.pow(a - b, 2) / (2 * Math.pow(c, 2)));
	}
	public static double distribution(double a, double c)
	{
		return 1 / (c * Math.sqrt(2 * Math.PI)) * Math.exp(- Math.pow(a, 2) / (2 * Math.pow(c, 2)));
	}
	public static double distribution(double a)
	{
		return distribution(a, 1D);
	}
	
	public static double getSq(Double...ds)
	{
		double d = 0;
		for(int i = 0; i < ds.length; ++i)
		{
			if(ds[i] == 0) continue;
			d += 1 / ds[i];
		}
		return (double) ds.length / d;
	}
	public static double getSq(double...ds)
	{
		double d = 0;
		for(int i = 0; i < ds.length; ++i)
		{
			if(ds[i] == 0) continue;
			d += 1 / ds[i];
		}
		return (double) ds.length / d;
	}
	
	public static int floor(int i, int j)
	{
		return  i < 0 ? (int) (Math.floor(i / j) * j - j) : i == 0 ? 0 : (int) (Math.floor(i / j) * j);
	}
	
	public static int mode(int i, int j)
	{
		int a = i - floor(i, j);
		return i % j == 0 ? 0 : a < 0 ? j + a : a;
	}

	public static abstract class Function
	{
		public abstract int caculate(int x);
		
		public abstract double caculate(double x);
		
		@Override
		public abstract int hashCode();
		
		@Override
		public abstract String toString();
		
		@Override
		public abstract boolean equals(Object obj);
		
		public static class LinearFunction extends Function
		{
			double k;
			double b;
			
			public LinearFunction(double k, double b) 
			{
				this.k = k;
				this.b = b;
			}
			
			@Override
			public int caculate(int x) 
			{
				return (int) (k * x + b);
			}
			
			@Override
			public double caculate(double x) 
			{
				return k * x + b;
			}

			@Override
			public int hashCode() 
			{
				return (int) (new Double(k).intValue() * 0xFFFFFFF + new Double(b).intValue());
			}

			@Override
			public String toString() 
			{
				return "y=" + k + "x" + b;
			}

			@Override
			public boolean equals(Object obj) 
			{
				if (obj instanceof LinearFunction) 
				{
					LinearFunction fun = (LinearFunction) obj;
					return fun.k == k && fun.b == b;
				}
				return false;
			}
			
		}
	}
}