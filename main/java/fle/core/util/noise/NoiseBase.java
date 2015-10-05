package fle.core.util.noise;

import java.util.Arrays;
import java.util.Random;

public abstract class NoiseBase
{
	protected long seed;
	protected Random rand;
	protected Random rand1;
	
	public NoiseBase(long aSeed)
	{
		seed = aSeed;
		rand = new Random(seed);
		rand1 = new Random();
	}
	
	public abstract double noise(long x, long z);
	
	public abstract double noise(long x, long y, long z);

	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				values[i + w * j] = noise(x + i, z + j);
		return values;
	}
	public double[] noise(double[] values, long x, long y, long z, int w, int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new double[w * l * h];
		else Arrays.fill(values, 0);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				for(int k = 0; k < h; ++k)
					values[i + w * j + k * w * l] = noise(x + i, y + h, z + j);
		return values;
	}
	
	protected double distance(double x, double z)
	{
		return distance(x, 0.0D, z);
	}
	
	protected double distance(double x, double y, double z)
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public NoiseBase setSeed(long aSeed)
	{
		seed = aSeed;
		return this;
	}
	
	public final long getSeed()
	{
		return seed;
	}
	
	@Override
	public int hashCode()
	{
		return new Long(seed).intValue();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass() == getClass() ? ((NoiseBase) obj).seed == seed : false;
	}
}