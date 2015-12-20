package fle.core.util.noise;

public class NoiseGauss extends NoiseBase
{
	int l;
	NoiseBase noise;
	
	public NoiseGauss(long aSeed, int size, NoiseBase noise)
	{
		super(aSeed);
		this.noise = noise;
		this.noise.setSeed(aSeed);
		l = size;
	}

	@Override
	public double noise(long x, long z)
	{
		return (double) ((int) (noise.noise(x, z) * l)) / (double) l;
	}

	@Override
	public double noise(long x, long y, long z)
	{
		return (double) ((int) (noise.noise(x, y, z) * l)) / (double) l;
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		double[] ret = noise.noise(values, x, z, w, l);
		for(int i = 0; i < ret.length; ++i)
			ret[i] = (double) ((int) (ret[i] * l)) / (double) l;
		return ret;
	}
	
	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		double[] ret = noise.noise(values, x, y, z, w, h, l);
		for(int i = 0; i < ret.length; ++i)
			ret[i] = (double) ((int) (ret[i] * l)) / (double) l;
		return ret;
	}
	
	@Override
	public NoiseBase setSeed(long seed)
	{
		noise.setSeed(seed);
		return super.setSeed(seed);
	}
}