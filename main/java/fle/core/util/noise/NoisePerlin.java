package fle.core.util.noise;

import java.util.Arrays;

public class NoisePerlin extends NoiseBase
{
	@Deprecated
	private final int octave;

	public NoisePerlin(long aSeed, int aOctave)
	{
		this(aSeed);
	}
	public NoisePerlin(long aSeed)
	{
		super(aSeed);
		octave = 0;
	}

	public double noise(long x, long z)
	{
		return noise(x, z, 1);
	}
	public double noise(long x, long y, long z)
	{
		return noise(x, y, z, 1);
	}
	public double noise(long x, long z, int size)
	{
		return noise(x, 10, z, size);
	}
	public double noise(long x, long y, long z, int size)
	{
		return next(x, y, z, 0);
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
			{
				values[i + w * j] += next(x + i, 10, z + j, 0);
			}
		return values;
	}
	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new double[w * l * h];
		else Arrays.fill(values, 0);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				for(int k = 0; k < h; ++k)
				{
					values[i + w * j + k * w * l] = next(x + i, y + j, z + k, 0);
				}
		return values;
	}
	
	private double next(long x, long y, long z, long t)
	{
		x = ((x << 15) + y * seed) ^ Math.abs(x);
		y = ((z << 14) + z * y) ^ Math.abs(y);
		z = ((y << 13) + x * y) ^ Math.abs(z);
		long ret = x * 1843271941L + y * 492471019L + z * 4292525927L + t * 141241L;
		ret = ret * (ret * ret * seed + 19990303L) + 1376312589L;
		return (1.0D - ((double)((ret * (ret * ret * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0)) / (double) (1 << t);
	}
}