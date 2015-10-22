package fle.core.util.noise;

public class NoiseMix extends NoiseBase
{
	byte modeType;
	double size;
	NoiseBase noise;

	public NoiseMix(NoiseBase noise)
	{
		this(1, 4.0F, noise);
	}
	public NoiseMix(double aSize, NoiseBase noise)
	{
		this(1, aSize, noise);
	}
	public NoiseMix(int aType, double aSize, NoiseBase aNoise)
	{
		super(aNoise.getSeed());
		noise = aNoise;
		size = aSize;
		modeType = (byte) aType;
	}

	@Override
	public double noise(long x, long z)
	{
		long x1 = (long) Math.floor(x / size);
		long z1 = (long) Math.floor(z / size);
		double[] de = new double[4];
		for(int i = -1; i < 3; ++i)
		{
			double d0 = noise.noise(x1 - 1, z1 + i);
			double d1 = noise.noise(x1, z1 + i);
			double d2 = noise.noise(x1 + 1, z1 + i);
			double d3 = noise.noise(x1 + 2, z1 + i);
			de[i + 1] = a(d0, d1, d2, d3, mode(x, size) / size);
		}
		return a(de[0], de[1], de[2], de[3], mode(z, size) / size);
	}

	@Override
	public double noise(long x, long y, long z)
	{
		long x1 = (long) Math.floor(x / size);
		long y1 = (long) Math.floor(y / size);
		long z1 = (long) Math.floor(z / size);
		double[] de = new double[4];
		for(int i = -1; i < 3; ++i)
		{
			double[] de1 = new double[4];
			for(int j = -1; j < 3; ++j)
			{
				double d0 = noise.noise(x1 - 1, y1 + j, z1 + i);
				double d1 = noise.noise(x1, y1 + j, z1 + i);
				double d2 = noise.noise(x1 + 1, y1 + j, z1 + i);
				double d3 = noise.noise(x1 + 2, y1 + j, z1 + i);
				de1[j + 1] = a(d0, d1, d2, d3, mode(y, size) / size);
			}
			de[i + 1] = a(de1[0], de1[1], de1[2], de1[3], mode(z, size) / size);
		}
		return a(de[0], de[1], de[2], de[3], mode(x, size) / size);
	}

	private double a(double a, double b, double c, double d, double x)
	{
		switch(modeType)
		{
		case 0 : return b * (1 - x) + c * x;
		case 1 : double f = (1 - Math.cos(x * Math.PI)) * .5D;
		return b * (1 - f) + c * f;
		case 2 : double P,Q,R,S;
		P = (d - c) - (a - b);
		Q = (a - b) - P;
		R = c - a;
		S = b;
		return P * x * x * x + Q * x * x + R * x + S;
		default: return Double.NaN;
		}
	}
	
	private double mode(double a, double b)
	{
		double ret = a % b;
		return ret < 0 ? ret + b : ret;
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