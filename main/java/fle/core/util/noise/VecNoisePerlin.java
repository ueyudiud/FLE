package fle.core.util.noise;

import java.util.Arrays;

public class VecNoisePerlin extends VecNoiseBase
{
	private final VecNoiseBase[] noises;
	
	public VecNoisePerlin(long aSeed, int aOctave, float length)
	{
		super(aSeed);
		noises = new VecNoiseBase[aOctave];
		double d1 = (double) ((2 << aOctave) - 1) / (double) (2 << aOctave);
		for(int i = 0; i < aOctave; ++i)
		{
			double d2 = 1D / (double) (1 << i);
			noises[i] = new VecNoiseZoom((1 << aOctave) >> i, 
					new VecNoiseSimple(aSeed * (aSeed * 23593752929L + i) + i * 38591L, (float) (d2 * 3F), new NoiseMix(2, (8 << aOctave) >> i, new NoisePerlin(1L, 2))));
		}
	}

	@Override
	public Vec noise(long x, long z)
	{
		Vec ret = new Vec(0, 0, 0);
		for(VecNoiseBase noise : noises)
			ret.add(noise.noise(x, z));
		return ret;
	}

	@Override
	public Vec noise(long x, long y, long z)
	{
		Vec ret = new Vec(0, 0, 0);
		for(VecNoiseBase noise : noises)
			ret.add(noise.noise(x, y, z));
		return ret;
	}
	
	private Vec add(Vec...vecs)
	{
		Vec ret = new Vec(0, 0, 0);
		for(int i = 0; i < vecs.length; ++i)
		{		
			ret.add(vecs[i]);
		}
		return ret;
	}
}