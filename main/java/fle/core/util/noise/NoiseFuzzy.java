package fle.core.util.noise;

import java.util.Arrays;

public class NoiseFuzzy extends NoiseBase
{
	private float weak;
	private NoiseBase[] noises;
	private double count;
	
	public NoiseFuzzy(long aSeed, int octive, double startLevel, double increaseLevel, float weakness)
	{
		super(aSeed);
		weak = weakness;
		noises = new NoiseSmooth[octive];
		for(int i = 0; i < octive; ++i)
		{
			noises[i] = new NoiseSmooth(startLevel * Math.pow(increaseLevel, i), new NoisePerlin(1L, 1));
			noises[i].setSeed(aSeed * 4517591481L + i * 84195819L + 51704L);
		}
		count = (Math.pow(weak, octive) - 1) / (weak - 1);
	}

	@Override
	public double noise(long x, long z)
	{
		double ret = 0;
		double w = 1;
		for(int i = 0; i < noises.length; ++i)
		{
			ret += noises[i].noise(x, z) * w;
			w *= weak;
		}
		ret /= count;
		return ret;
	}

	@Override
	public double noise(long x, long y, long z)
	{
		double ret = 0;
		double w = 1;
		for(int i = 0; i < noises.length; ++i)
		{
			ret += noises[i].noise(x, y, z) * w;
			w *= weak;
		}
		ret /= count;
		return ret;
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		double we = 1;
		for(int k = 0; k < noises.length; ++k)
		{
			double[] ds = noises[k].noise(new double[0], x, z, w, l);
			for(int i = 0; i < w; ++i)
				for(int j = 0; j < l; ++j)
					values[i + w * j] += ds[i + w * j] * we;
			we *= weak;
		}
		for(int c = 0; c < values.length; ++c)
			values[c] /= count;
		return values;
	}
	
	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new double[w * l * h];
		else Arrays.fill(values, 0);
		double we = 1;
		for(int s = 0; s < noises.length; ++s)
		{
			double[] ds = noises[s].noise(new double[0], x, y, z, w, h, l);
			for(int i = 0; i < w; ++i)
				for(int j = 0; j < l; ++j)
					for(int k = 0; k < h; ++k)
						values[i + w * j + k * w * l] += ds[i + w * j + k * w * l] * we;
			we *= weak;
		}
		for(int c = 0; c < values.length; ++c)
			values[c] /= count;
		return values;
	}
	
	@Override
	public NoiseBase setSeed(long aSeed)
	{
		super.setSeed(aSeed);
		for(int i = 0; i < noises.length; ++i)
		{
			noises[i].setSeed(aSeed * 4517591481L + i * 84195819L + 51704L);
		}
		return this;
	}
}