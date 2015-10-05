package fle.core.util.noise;

public class NoisePerlin extends NoiseBase
{
	private final int octave;
	
	public NoisePerlin(long aSeed, int aOctave)
	{
		super(aSeed);
		octave = aOctave;
	}

	public double noise(long x, long z)
	{
		return noise(x, z, octave);
	}
	public double noise(long x, long y, long z)
	{
		return noise(x, y, z, octave);
	}
	public double noise(long x, long z, int size)
	{
		return noise(x, 10, z, size);
	}
	public double noise(long x, long y, long z, int size)
	{
		double ret = 0D;
		double d1 = (double) (Math.pow(2, size) - 1) / (double) Math.pow(2, size);
		for(int i = 0; i < size; ++i)
			ret += setupSeed(x, y, z, i) * d1;
		return ret;
	}
	
	private double setupSeed(long x, long y, long z, long t)
	{
		x = ((x << 15) + y * seed) ^ Math.abs(x);
		y = ((z << 14) + z * y) ^ Math.abs(y);
		z = ((y << 13) + x * y) ^ Math.abs(z);
		long ret = x * 1843271941L + y * 492471019L + z * 4292525927L + t * 141241L;
		ret = ret * (ret * ret * seed + 19990303L) + 1376312589L;
		return (1.0D - ((double)((ret * (ret * ret * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0)) / (double) (1 << t);
	}
}