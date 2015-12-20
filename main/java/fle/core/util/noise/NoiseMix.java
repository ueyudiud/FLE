package fle.core.util.noise;

public class NoiseMix extends NoiseBase
{
	private NoiseBase noise1;
	private NoiseBase noise2;
	private double weight;

	public NoiseMix(long seed, NoiseBase noise1, NoiseBase noise2)
	{
		this(seed, 0.5, noise1, noise2);
	}
	public NoiseMix(long seed, double weight, NoiseBase noise1, NoiseBase noise2)
	{
		super(seed);
		this.noise1 = noise1.setSeed(seed * 375191749L + 501859135L);
		this.noise2 = noise2.setSeed(seed * 947195717L + 295719571L);
		this.weight = weight;
	}

	@Override
	public double noise(long x, long z)
	{
		return noise1.noise(x, z) * (1D - weight) + noise2.noise(x, z) * weight;
	}

	@Override
	public double noise(long x, long y, long z)
	{
		return noise1.noise(x, y, z) * (1D - weight) + noise2.noise(x, y, z) * weight;
	}

	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		double[] ds1 = noise1.noise(values, x, y, z, w, h, l);
		double[] ds2 = noise2.noise(new double[0], x, y, z, w, h, l);
		for(int i = 0; i < ds2.length; ++i)
		{
			ds1[i] *= 1 - weight;
			ds1[i] += ds2[i] * weight;
		}
		return ds1;
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		double[] ds1 = noise1.noise(values, x, z, w, l);
		double[] ds2 = noise2.noise(new double[0], x, z, w, l);
		for(int i = 0; i < ds2.length; ++i)
		{
			ds1[i] *= 1 - weight;
			ds1[i] += ds2[i] * weight;
		}
		return ds1;
	}
	
	@Override
	public NoiseBase setSeed(long seed)
	{
		noise1.setSeed(seed * 375191749L + 501859135L);
		noise2.setSeed(seed * 947195717L + 295719571L);
		return super.setSeed(seed);
	}
}