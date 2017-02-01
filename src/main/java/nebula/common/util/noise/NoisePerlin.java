package nebula.common.util.noise;

import java.util.Random;

public class NoisePerlin extends NoiseBase
{
	private double frequency;
	private double persistence;
	
	private NoiseBase[] octaves;
	
	public NoisePerlin(Random random, double persistence, NoiseBase...octaves)
	{
		super(random.nextLong());
		this.frequency = 1.0F;
		this.persistence = persistence;
		this.octaves = octaves;
	}
	
	/**
	 *
	 * @param random
	 * @param size
	 * @param start
	 * @param frequency The noise changed speed increase, the value is from 0.0 to infinity (more than 1.0 default).
	 * @param persistence The noise effect for each layer, the value is from 0.0 to 1.0.
	 */
	public NoisePerlin(Random random, int size, double start, double frequency, double persistence)
	{
		super(random.nextLong());
		this.frequency = frequency;
		this.persistence = persistence;
		this.octaves = new NoiseCoherent[size];
		double t = start;
		for(int i = 0; i < size; this.octaves[i++] = new NoiseCoherent(this.seed + i, t),
				t *= frequency);
	}
	
	public NoisePerlin(long seed, double persistence, NoiseBase...octaves)
	{
		super(seed);
		this.frequency = 1.0F;
		this.persistence = persistence;
		this.octaves = octaves;
	}
	
	/**
	 *
	 * @param seed
	 * @param size
	 * @param start
	 * @param frequency The noise changed speed increase, the value is from 0.0 to infinity (more than 1.0 default).
	 * @param persistence The noise effect for each layer, the value is from 0.0 to 1.0.
	 */
	public NoisePerlin(long seed, int size, double start, double frequency, double persistence)
	{
		super(seed);
		this.frequency = frequency;
		this.persistence = persistence;
		this.octaves = new NoiseCoherent[size];
		double t = start;
		for(int i = 0;
				i < size;
				this.octaves[i++] = new NoiseCoherent(seed + i, t),
						t *= frequency);
	}
	
	@Override
	public NoiseBase setSeed(long seed)
	{
		for(int i = 0; i < this.octaves.length; this.octaves[i++].setSeed(seed + i));
		return super.setSeed(seed);
	}
	
	@Override
	public double[] noise(double[] array, int u, int v, int w, double x, double y, double z, double xScale,
			double yScale, double zScale)
	{
		if(array == null || array.length < u * v * w)
		{
			array = new double[u * v * w];
		}
		double[] array1 = null;
		double a = this.persistence + 1D;
		for(int i = 0; i < this.octaves.length; ++i)
		{
			array1 = this.octaves[i].noise(array1, u, v, w, x, y, z, xScale, yScale, zScale);
			for(int p = 0; p < w; ++p)
			{
				for(int q = 0; q < v; ++q)
				{
					for(int r = 0; r < u; ++r)
					{
						int id = (p * v + q) * u + r;
						if(i == 0)
						{
							array[id] += array1[id];
						}
						else
						{
							array[id] += array1[id] * this.persistence;
							array[id] /= a;
						}
					}
				}
			}
		}
		return array;
	}
	
	@Override
	public double noise(double x, double y, double z)
	{
		final double a = this.persistence + 1D;
		double ret = this.octaves[0].noise(x, y, z);
		for(int i = 1; i < this.octaves.length; ++i)
		{
			ret += this.octaves[i].noise(x, y, z) * this.persistence;
			ret /= a;
		}
		return ret;
	}
}