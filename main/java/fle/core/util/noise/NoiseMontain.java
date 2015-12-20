package fle.core.util.noise;

import java.util.Arrays;

import net.minecraft.world.gen.layer.GenLayer;
import fle.core.world.layer.FLELayerItrZoom;
import fle.core.world.layer.FLELayerSinglePixel;

public class NoiseMontain extends NoiseBase
{
	private static final double[] ds;
	private static final double f;
	private static final double g;
	
	static
	{
		ds = new double[5 * 5 * 5];
		double a = 0;
		for(int x = 0; x < 5; ++x)
			for(int z = 0; z < 5; ++z)
				a += (ds[5 * z + x] = 1D / (1D + (double) ((x - 2) * (x - 2) + (z - 2) * (z - 2))));
		f = a;
		g = 4D / (f * f);
	}
	
	private double level;
	private GenLayer noise;
	
	public NoiseMontain(long aSeed, int size, int genChance, double level)
	{
		super(aSeed);
		this.noise = new FLELayerItrZoom(size, aSeed * 27591741L + 3475917591L, new FLELayerSinglePixel(false, genChance, 1L));
		this.level = level;
	}

	@Override
	public double noise(long x, long z)
	{
		int[] is = noise.getInts((int) x - 2, (int) z - 2, 5, 5);
		double c = 0.0D;
		for(int i = 0; i < 5; ++i)
			for(int j = 0; j < 5; ++j)
				for(int k = 0; k < 5; ++k)
					c += ds[k * 25 + i * 5 + j] * is[25 * k + 5 * j + i];
		return Math.pow(c * (c - f) * g, level);
	}

	@Override
	public double noise(long x, long y, long z)
	{
		return noise(x, z);
	}
	
	@Override
	public double[] noise(double[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new double[w * l];
		else Arrays.fill(values, 0);
		int[] is = noise.getInts((int) x - 2, (int) z - 2, 4 + w, 4 + l);
		for(int i = 0; i < w; ++i)
			for(int k = 0; k < l; ++k)
			{
				for(int i1 = 0; i1 < 5; ++i1)
					for(int k1 = 0; k1 < 5; ++k1)
						values[w * k + i] += ds[k1 * 5 + i1] * is[(w + 4) * (k + k1) + (i + i1)];
			}
		for(int i = 0; i < values.length; ++i)
			values[i] = Math.pow(values[i] * (f - values[i]) * g, level);
		return values;
	}
	
	@Override
	public double[] noise(double[] values, long x, long y, long z, int w,
			int h, int l)
	{
		throw new IllegalArgumentException("FLE can not noise montain with 3D graph.");
	}
	
	@Override
	public NoiseBase setSeed(long seed)
	{
		noise.initWorldGenSeed(seed * 27591741L + 3475917591L);
		return super.setSeed(seed);
	}
}