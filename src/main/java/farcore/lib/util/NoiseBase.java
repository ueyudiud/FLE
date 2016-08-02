package farcore.lib.util;

public abstract class NoiseBase
{
	protected long seed;

	public NoiseBase(long seed)
	{
		this.seed = seed;
	}

	public NoiseBase setSeed(long seed)
	{
		this.seed = seed;
		return this;
	}
	
	public double[] noise(double[] array, int u, int w, double x, double z)
	{
		return noise(array, u, w, x, z, 1D, 1D);
	}

	public double[] noise(double[] array, int u, int w, double x, double z, double xScale, double zScale)
	{
		return noise(array, u, w, 1, x, z, 0D, xScale, zScale, 1D);
	}
	
	public double[] noise(double[] array, int u, int v, int w, double x, double y, double z)
	{
		return noise(array, u, v, w, x, y, z, 1D, 1D, 1D);
	}

	public abstract double[] noise(double[] array, int u, int v, int w, double x, double y, double z, double xScale, double yScale, double zScale);

	public abstract double noise(double x, double y, double z);

	protected double lerp(double x, double l, double a, double b)
	{
		return lerp(x / l, a, b);
	}

	protected double lerp(double x, double a, double b)
	{
		return a + x * (b - a);
	}

	protected double s_curve(double x)
	{
		return x * x * (3D - x - x);
	}

	protected double point(double x)
	{
		return x - Math.floor(x);
	}
}