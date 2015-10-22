package fle.core.util;

import java.util.Iterator;

public class Surface implements Iterable<Double>
{
	private final int w;
	private final int h;
	private final double a;
	private final double b;
	private final double c;
	private final double d;
	
	public Surface(int width, int height, double A, double B, double C, double D)
	{
		w = width;
		h = height;
		a = A;
		b = B;
		c = C;
		d = D;
	}
	
	public double getValue(int x, int y)
	{
		double X = (double) x / w;
		double Y = (double) y / h;
		return (a * X + b * (1 - X)) * Y + 
				(c * X + d * (1 - X)) * (1 - Y);
	}

	@Override
	public SurfaceItr iterator()
	{
		return new SurfaceItr();
	}
	
	private class SurfaceItr implements Iterator<Double>
	{
		int id = 0;

		@Override
		public boolean hasNext()
		{
			return id + 1 < w * h;
		}

		@Override
		public Double next()
		{
			return getValue(id % w, id / w);
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}	
	}
}