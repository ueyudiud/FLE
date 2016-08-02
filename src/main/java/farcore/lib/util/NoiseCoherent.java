package farcore.lib.util;

import java.util.Random;

public class NoiseCoherent extends NoiseBase
{
	private double length;
	private double offsetX;
	private double offsetY;
	private double offsetZ;

	public NoiseCoherent(long seed)
	{
		this(seed, 1D);
	}
	public NoiseCoherent(long seed, double length)
	{
		super(seed);
		this.length = length;
		Random random = new Random(seed);
		offsetX = random.nextDouble() * 0xFF;
		offsetY = random.nextDouble() * 0xFF;
		offsetZ = random.nextDouble() * 0xFF;
	}
	
	@Override
	public double[] noise(double[] array, int u, int v, int w, double x, double y, double z, double xScale,
			double yScale, double zScale)
	{
		if(array == null || array.length < u * v * w)
		{
			array = new double[u * v * w];
		}
		double xM = xScale / length;
		double yM = yScale / length;
		double zM = zScale / length;
		double x0 = x * xScale / length + offsetX;
		double y0 = y * yScale / length + offsetY;
		double z0 = z * zScale / length + offsetZ;
		double x1 = x0, y1 = y0, z1 = z0;
		long lastX = (long) x0;
		long lastY = (long) y0;
		long lastZ = (long) z0;
		double n1 = next(lastX, lastY, lastZ);
		double n2 = next(lastX + 1, lastY, lastZ);
		double n3 = next(lastX, lastY + 1, lastZ);
		double n4 = next(lastX + 1, lastY + 1, lastZ);
		double n5 = next(lastX, lastY, lastZ + 1);
		double n6 = next(lastX + 1, lastY, lastZ + 1);
		double n7 = next(lastX, lastY + 1, lastZ + 1);
		double n8 = next(lastX + 1, lastY + 1, lastZ + 1);
		double u1, u2, u3, u4;
		for(int k = 0; k < w; ++k, z1 += zM)
		{
			if(lastZ != (long) z1)
			{
				if(lastX == (long) x0 && lastY == (long) y0 && lastZ + 1 == (long) z1)
				{
					++lastZ;
					n1 = n5;
					n2 = n6;
					n3 = n7;
					n4 = n8;
					n5 = next(lastX, lastY, lastZ + 1);
					n6 = next(lastX + 1, lastY, lastZ + 1);
					n7 = next(lastX, lastY + 1, lastZ + 1);
					n8 = next(lastX + 1, lastY + 1, lastZ + 1);
				}
				else
				{
					lastX = (long) x0;
					lastY = (long) y0;
					lastZ = (long) z1;
					n1 = next(lastX, lastY, lastZ);
					n2 = next(lastX + 1, lastY, lastZ);
					n3 = next(lastX, lastY + 1, lastZ);
					n4 = next(lastX + 1, lastY + 1, lastZ);
					n5 = next(lastX, lastY, lastZ + 1);
					n6 = next(lastX + 1, lastY, lastZ + 1);
					n7 = next(lastX, lastY + 1, lastZ + 1);
					n8 = next(lastX + 1, lastY + 1, lastZ + 1);
				}
			}
			for(int j = 0; j < v; ++j, y1 += yM)
			{
				if(lastY != (long) y1)
				{
					if(lastX == (long) x0 && lastY + 1 == (long) y1 && lastZ == (long) z1)
					{
						++lastY;
						n1 = n3;
						n2 = n4;
						n5 = n7;
						n6 = n8;
						n3 = next(lastX, lastY + 1, lastZ);
						n4 = next(lastX + 1, lastY + 1, lastZ);
						n7 = next(lastX, lastY + 1, lastZ + 1);
						n8 = next(lastX + 1, lastY + 1, lastZ + 1);
					}
					else
					{
						lastX = (long) x0;
						lastY = (long) y1;
						lastZ = (long) z1;
						n1 = next(lastX, lastY, lastZ);
						n2 = next(lastX + 1, lastY, lastZ);
						n3 = next(lastX, lastY + 1, lastZ);
						n4 = next(lastX + 1, lastY + 1, lastZ);
						n5 = next(lastX, lastY, lastZ + 1);
						n6 = next(lastX + 1, lastY, lastZ + 1);
						n7 = next(lastX, lastY + 1, lastZ + 1);
						n8 = next(lastX + 1, lastY + 1, lastZ + 1);
					}
				}
				for(int i = 0; i < u; ++i, x1 += xM)
				{
					if(lastX != (long) x1)
					{
						if(lastX + 1 == (long) x1 && lastY == (long) y1 && lastZ == (long) z1)
						{
							++lastX;
							n1 = n2;
							n3 = n4;
							n5 = n6;
							n7 = n8;
							n2 = next(lastX + 1, lastY, lastZ);
							n4 = next(lastX + 1, lastY + 1, lastZ);
							n6 = next(lastX + 1, lastY, lastZ + 1);
							n8 = next(lastX + 1, lastY + 1, lastZ + 1);
						}
						else
						{
							lastX = (long) x1;
							lastY = (long) y1;
							lastZ = (long) z1;
							n1 = next(lastX, lastY, lastZ);
							n2 = next(lastX + 1, lastY, lastZ);
							n3 = next(lastX, lastY + 1, lastZ);
							n4 = next(lastX + 1, lastY + 1, lastZ);
							n5 = next(lastX, lastY, lastZ + 1);
							n6 = next(lastX + 1, lastY, lastZ + 1);
							n7 = next(lastX, lastY + 1, lastZ + 1);
							n8 = next(lastX + 1, lastY + 1, lastZ + 1);
						}
					}
					u1 = lerp(s_curve(point(x1)), n1, n2);
					u2 = lerp(s_curve(point(x1)), n3, n4);
					u3 = lerp(s_curve(point(x1)), n5, n6);
					u4 = lerp(s_curve(point(x1)), n7, n8);
					u1 = lerp(s_curve(point(y1)), u1, u2);
					u3 = lerp(s_curve(point(y1)), u3, u4);
					array[(k * v + j) * u + i] = lerp(s_curve(point(z1)), u1, u3);
				}
				x1 = x0;
			}
			y1 = y0;
		}
		return array;
	}

	@Override
	public double noise(double x, double y, double z)
	{
		double x0 = x / length + offsetX;
		double y0 = y / length + offsetY;
		double z0 = z / length + offsetZ;
		long x1 = (long) x0;
		long y1 = (long) y0;
		long z1 = (long) z0;
		double x2 = s_curve(point(x0));
		double y2 = s_curve(point(y0));
		double z2 = s_curve(point(z0));
		double n1, n2, n3, n4, n5, n6, n7, n8;
		double u1, u2, u3, u4;
		n1 = next(x1, y1, z1);
		n2 = next(x1 + 1, y1, z1);
		n3 = next(x1, y1 + 1, z1);
		n4 = next(x1 + 1, y1 + 1, z1);
		n5 = next(x1, y1, z1 + 1);
		n6 = next(x1 + 1, y1, z1 + 1);
		n7 = next(x1, y1 + 1, z1 + 1);
		n8 = next(x1 + 1, y1 + 1, z1 + 1);
		u1 = lerp(x2, n1, n2);
		u2 = lerp(x2, n3, n4);
		u3 = lerp(x2, n5, n6);
		u4 = lerp(x2, n7, n8);
		u1 = lerp(y2, u1, u2);
		u3 = lerp(y2, u3, u4);
		return lerp(z2, u1, u3);
	}

	private double next(long n)
	{
		n ^= (n >> 13);
		n = (n * (n * n * 60493 + 19990303) + 1376312589 + seed) & 0x7FFFFFFF;
		return (double)n / (double) 0x7FFFFFFFL;
	}
	private double next(long x, long y, long z)
	{
		x ^= (x >> 12);
		y ^= (y >> 10);
		z ^= (z >> 11);
		x = x * 749471927491L + y * 3759173371L + z * 3759184203L + seed;
		return next(x);
	}
}