package fle.core.util.noise;

import java.util.Arrays;

public class NoiseFuzzy extends NoiseBase
{
	private long a;
	private long b;
	private float weak;
	private NoiseBase[] noises;
	private double count;
	
	public NoiseFuzzy(long aSeed, int octive, long startLevel, long increaseLevel, float weakness)
	{
		super(aSeed);
		a = startLevel;
		b = increaseLevel;
		weak = weakness;
		noises = new NoiseMix[octive];
		for(int i = 0; i < octive; ++i)
		{
			noises[i] = new NoiseMix(startLevel * Math.pow(increaseLevel, i), new NoisePerlin(aSeed * 24185151L + 51591761L, 1));
		}
		count = (Math.pow(weak, octive + 1) - 1) / (weak - 1);
	}

	@Override
	public double noise(long x, long z)
	{
		double ret = 0;
		for(int i = 0; i < noises.length; ++i)
			ret += noises[i].noise(x, z) * Math.pow(weak, i) / count;
		return ret;
	}

	@Override
	public double noise(long x, long y, long z)
	{
		double ret = 0;
		for(int i = 0; i < noises.length; ++i)
			ret += noises[i].noise(x, y, z) * Math.pow(weak, i) / count;
		return ret;
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		for(int k = 0; k < noises.length; ++k)
		{
			double[] ds = noises[k].noise(new double[0], x, z, w, l);
			for(int i = 0; i < w; ++i)
				for(int j = 0; j < l; ++j)
					values[i + w * j] += ds[i + w * j];
		}
		return values;
	}
	
	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new double[w * l * h];
		else Arrays.fill(values, 0);
		for(int s = 0; s < noises.length; ++s)
		{
			double[] ds = noises[s].noise(new double[0], x, y, z, w, h, l);
			for(int i = 0; i < w; ++i)
				for(int j = 0; j < l; ++j)
					for(int k = 0; k < h; ++k)
						values[i + w * j + k * w * l] += ds[i + w * j + k * w * l];
		}
		return values;
	}
}