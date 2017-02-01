package nebula.common.util.noise;

import java.util.Random;

import nebula.common.util.Maths;

public class NoiseCell extends NoiseBase
{
	private final double coe;
	private byte disType;
	private double persistence;
	private double size;
	private int length;
	private double[] cache = new double[4];
	
	public NoiseCell(Random rand, int disType, int length, double size, double persistence)
	{
		this(rand.nextLong(), disType, length, size, persistence);
	}
	public NoiseCell(long seed, int disType, int length, double size, double persistence)
	{
		super(seed);
		this.disType = (byte) disType;
		this.length = length;
		this.size = size;
		this.persistence = persistence;
		double coe = 0;
		for(int i = -length; i <= length; ++i)
		{
			for(int j = -length; j <= length; ++j)
			{
				for(int k = -length; k <= length; ++k)
				{
					coe += 1D / (1D + persistence * distance(i, j, k));
				}
			}
		}
		this.coe = 1.0 / coe;
	}
	
	@Override
	public double[] noise(double[] array, int u, int v, int w, double x, double y, double z, double xScale,
			double yScale, double zScale)
	{
		if(array == null || array.length < u * v * w)
		{
			array = new double[u * v * w];
		}
		int c = 0;
		for(int k = 0; k < u; ++k)
		{
			for(int j = 0; j < v; ++j)
			{
				for(int i = 0; i < w; ++i)
				{
					array[c++] = noise(x + xScale * k, y + yScale * j, z + zScale * i);
				}
			}
		}
		return array;
	}
	
	@Override
	public double noise(double x, double y, double z)
	{
		double val = 0;
		int x1 = (int) (x < 0 ? x - 1 : x);
		int y1 = (int) (y < 0 ? y - 1 : y);
		int z1 = (int) (z < 0 ? z - 1 : z);
		double v1 = Maths.mod(x, size) / size;
		double v2 = Maths.mod(y, size) / size;
		double v3 = Maths.mod(z, size) / size;
		for(int i = - length + 1; i <= length; ++i)
		{
			for(int j = - length + 1; j <= length; ++j)
			{
				for(int k = - length + 1; k <= length; ++k)
				{
					double[] l = nextCell(x1 + i, y1 + j, z1 + k);
					val += l[3] / (1.0 + persistence * distance(v1, v2, v3, i + l[0], j + l[1], k + l[2]));
				}
			}
		}
		return Math.min(val * coe, 1.0);
	}
	
	private double[] nextCell(int x, int y, int z)
	{
		cache[0] = next(x, y, z, 382947L);
		cache[1] = next(x, y, z, 472813L);
		cache[2] = next(x, y, z, 382048L);
		cache[3] = next(x, y, z, 192719L);
		cache[3] *= cache[3];
		return cache;
	}

	private double distance(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return distance(x1 - x2, y1 - y2, z1 - z2);
	}
	
	private double distance(double a, double b, double c)
	{
		switch (disType)
		{
		case 0 : return Math.abs(a) + Math.abs(b) + Math.abs(c);
		case 1 : return Math.sqrt(a * a + b * b + c * c);
		case 2 : return a * a + b * b + c * c;
		case 3 : return Math.cbrt(Math.abs(a * a * a) + Math.abs(b * b * b) + Math.abs(c * c * c));
		default: return 0;
		}
	}
	
	private double next(long n)
	{
		n ^= (n >> 13);
		n = (n * (n * n * 60493 + 19990303) + 1376312589 + seed) & 0x7FFFFFFF;
		return (double)n / (double) 0x7FFFFFFFL;
	}
	private double next(long x, long y, long z, long t)
	{
		x ^= (x >> 12);
		y ^= (y >> 10);
		z ^= (z >> 11);
		t ^= (t >> 13);
		x = x * 749471927491L + y * 3759173371L + z * 3759184203L + t * 473847382947195L + seed;
		return next(x);
	}
}