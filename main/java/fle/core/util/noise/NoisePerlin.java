package fle.core.util.noise;

import java.util.Arrays;

public class NoisePerlin extends NoiseBase
{
	public NoisePerlin(long aSeed, int aOctave)
	{
		this(aSeed);
	}
	public NoisePerlin(long aSeed)
	{
		super(aSeed);
	}

	public double noise(long x, long z)
	{
		return noise(x, -1, z);
	}
	public double noise(long x, long y, long z)
	{
		return next(x, y, z);
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
			{
				values[i + w * j] += next(x + i, 10, z + j);
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
					values[i + w * j + k * w * l] = next(x + i, y + j, z + k);
				}
		return values;
	}
	
	private double next(long x, long y, long z)
	{
		long ret = (x * 38591L + y) ^ (y * 37501L + z) ^ (z * 17419L + x) ^ seed;
	    return (1.0 - ((ret * (ret * (ret * 15731 + seed) + 782949221L) + 76312589L) & 0x7FFFFFFF) / 1073741824.0);   
	}
}