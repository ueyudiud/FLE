package fle.core.util.noise;

public class NoiseSmooth extends NoiseBase
{
	byte modeType;
	double size;
	NoiseBase noise;

	public NoiseSmooth(NoiseBase noise)
	{
		this(1, 4.0F, noise);
	}
	public NoiseSmooth(double aSize, NoiseBase noise)
	{
		this(1, aSize, noise);
	}
	public NoiseSmooth(int aType, double aSize, NoiseBase aNoise)
	{
		super(aNoise.getSeed());
		noise = aNoise;
		size = aSize;
		modeType = (byte) aType;
		if(modeType == 2) throw new IndexOutOfBoundsException();//Not for type 2 now.
	}
	
	@Override
	public double noise(long x, long z)
	{
		long x1 = (long) Math.floor(x / size);
		long z1 = (long) Math.floor(z / size);
		double d1 = noise.noise(x1, z1);
		double d2 = noise.noise(x1 + 1, z1);
		double d3 = noise.noise(x1, z1 + 1);
		double d4 = noise.noise(x1 + 1, z1 + 1);
		return b(d1, d2, d3, d4, mode(x, size) / size, mode(z, size) / size);
	}

	@Override
	public double noise(long x, long y, long z)
	{
		long x1 = (long) Math.floor(x / size);
		long y1 = (long) Math.floor(y / size);
		long z1 = (long) Math.floor(z / size);
		double d1 = noise.noise(x1, y1, z1);
		double d2 = noise.noise(x1 + 1, y1, z1);
		double d3 = noise.noise(x1, y1, z1 + 1);
		double d4 = noise.noise(x1 + 1, y1, z1 + 1);
		double d5 = noise.noise(x1, y1 + 1, z1);
		double d6 = noise.noise(x1 + 1, y1 + 1, z1);
		double d7 = noise.noise(x1, y1 + 1, z1 + 1);
		double d8 = noise.noise(x1 + 1, y1 + 1, z1 + 1);
		return b(d1, d2, d3, d4, d5, d6, d7, d8, mode(x, size) / size, mode(y, size) / size, mode(z, size) / size);
	}

	private double b(double a, double b, double x)
	{
		switch(modeType)
		{
		case 0 : return a * (1 - x) + b * x;
		case 1 : double f = (1 - Math.cos(x * Math.PI)) * .5D;
		return a * (1 - f) + b * f;
		default: return Double.NaN;
		}
	}

	private double b(double a, double b, double c, double d, double x, double y)
	{
		double u = b(a, b, x), v = b(c, d, x);
		return b(u, v, y);
	}

	private double b(double a, double b, double c, double d,
			double e, double f, double g, double h, double x, double y, double z)
	{
		double u = b(a, b, c, d, x, y), v = b(e, f, g, h, x, y);
		return b(u, v, z);
	}
	
	private double mode(long a, double b)
	{
		double ret = a % b;
		return ret < 0 ? ret + b : ret;
	}
	
	@Override
	public NoiseBase setSeed(long aSeed)
	{
		noise.setSeed(aSeed);
		return super.setSeed(aSeed);
	}
}