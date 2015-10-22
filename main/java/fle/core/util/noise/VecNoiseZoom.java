package fle.core.util.noise;

import java.util.Arrays;

import fle.core.util.FLEMath;
import net.minecraft.util.Vec3;

public class VecNoiseZoom extends VecNoiseBase
{
	byte modeType;
	int size;
	VecNoiseBase noise;

	public VecNoiseZoom(VecNoiseBase aNoise)
	{
		this(4, aNoise);
	}
	public VecNoiseZoom(int aSize, VecNoiseBase aNoise)
	{
		super(aNoise.getSeed());
		size = aSize;
		noise = aNoise;
		modeType = 0;
	}

	@Override
	public Vec noise(long x, long z)
	{
		Vec3 v1 = noise.noise(FLEMath.iDivide(x, size)    , FLEMath.iDivide(z, size));
		Vec3 v2 = noise.noise(FLEMath.iDivide(x, size) + 1, FLEMath.iDivide(z, size));
		Vec3 v3 = noise.noise(FLEMath.iDivide(x, size)    , FLEMath.iDivide(z, size) + 1);
		Vec3 v4 = noise.noise(FLEMath.iDivide(x, size) + 1, FLEMath.iDivide(z, size) + 1);

		double d1 = 1D - g(FLEMath.gRound(x, size));
		double d2 = 1D - g(FLEMath.gRound(z, size));

		double xO = d2 * (v1.xCoord * d1 + v2.xCoord * (1 - d1)) + (1 - d2) * (v3.xCoord * d1 + v4.xCoord * (1 - d1));
		double zO = d2 * (v1.zCoord * d1 + v2.zCoord * (1 - d1)) + (1 - d2) * (v3.zCoord * d1 + v4.zCoord * (1 - d1));
		
		return new Vec(xO, 0, zO);
	}

	@Override
	public Vec noise(long x, long y, long z)
	{
		Vec3 v1 = noise.noise(x / size    , y / size    , z / size);
		Vec3 v2 = noise.noise(x / size + 1, y / size    , z / size);
		Vec3 v3 = noise.noise(x / size    , y / size    , z / size + 1);
		Vec3 v4 = noise.noise(x / size + 1, y / size    , z / size + 1);
		Vec3 v5 = noise.noise(x / size    , y / size + 1, z / size);
		Vec3 v6 = noise.noise(x / size + 1, y / size + 1, z / size);
		Vec3 v7 = noise.noise(x / size    , y / size + 1, z / size + 1);
		Vec3 v8 = noise.noise(x / size + 1, y / size + 1, z / size + 1);

		double d1 = 1D - g(FLEMath.gRound(x, size));
		double d3 = 1D - g(FLEMath.gRound(y, size));
		double d2 = 1D - g(FLEMath.gRound(z, size));

		double xO = d3 * (d2 * (v1.xCoord * d1 + v2.xCoord * (1 - d1)) + (1 - d2) * (v3.xCoord * d1 + v4.xCoord * (1 - d1))) +
				(1 - d3) * (d2 * (v5.xCoord * d1 + v6.xCoord * (1 - d1)) + (1 - d2) * (v7.xCoord * d1 + v8.xCoord * (1 - d1)));
		double yO = d3 * (d2 * (v1.yCoord * d1 + v2.yCoord * (1 - d1)) + (1 - d2) * (v3.yCoord * d1 + v4.yCoord * (1 - d1))) +
				(1 - d3) * (d2 * (v5.yCoord * d1 + v6.yCoord * (1 - d1)) + (1 - d2) * (v7.yCoord * d1 + v8.yCoord * (1 - d1)));
		double zO = d3 * (d2 * (v1.zCoord * d1 + v2.zCoord * (1 - d1)) + (1 - d2) * (v3.zCoord * d1 + v4.zCoord * (1 - d1))) +
				(1 - d3) * (d2 * (v5.zCoord * d1 + v6.zCoord * (1 - d1)) + (1 - d2) * (v7.zCoord * d1 + v8.zCoord * (1 - d1)));
		
		return new Vec(xO, yO, zO);
	}
	
	@Override
	public Vec[] noise(Vec[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new Vec[w * l];
		else Arrays.fill(values, null);
		long x1 = FLEMath.iDivide(x, size);
		long z1 = FLEMath.iDivide(z, size);
		long w1 = FLEMath.iDivide(w, size) + 3;
		long l1 = FLEMath.iDivide(l, size) + 3;
		
		int xOffset = (int) FLEMath.mod(x, size);
		int zOffset = (int) FLEMath.mod(z, size);
		
		Vec[] b = noise.noise(new Vec[(int) (w1 * l1)], x1, z1, (int) w1, (int) l1);
		final double b0 = 1D / size;
		for(int i = 0; i < w1 - 1; ++i)
			for(int j = 0; j < l1 - 1; ++j)
			{
				Vec v1 = b[(int) (i + w1 * j)];
				Vec v2 = b[(int) (i + 1 + w1 * j)];
				Vec v3 = b[(int) (i + w1 * (j + 1))];
				Vec v4 = b[(int) (i + 1 + w1 * (j + 1))];
				
				label:
				for(int m = 0; m <= size; ++m)
				{
					for(int n = 0; n <= size; ++n)
					{
						if(j * size + n - xOffset < 0) continue;
						if(i * size + m - zOffset < 0) continue label;
						if(j * size + n - xOffset >= l) break;
						if(i * size + m - zOffset >= w) break label;
						double d1 = 1D - m * b0;
						double d2 = 1D - n * b0;

						double xO = d2 * (v1.xCoord * d1 + v2.xCoord * (1 - d1)) + (1 - d2) * (v3.xCoord * d1 + v4.xCoord * (1 - d1));
						double zO = d2 * (v1.zCoord * d1 + v2.zCoord * (1 - d1)) + (1 - d2) * (v3.zCoord * d1 + v4.zCoord * (1 - d1));
						values[i * size + m - xOffset + (j * size + n - zOffset) * w] = new Vec(xO, 0, zO);
					}
				}
			}
		return values;
	}
	
	@Override
	public Vec[] noise(Vec[] values, long x, long y, long z, int w, int h, int l)
	{
		return super.noise(values, x, y, z, w, h, l);
	}
	
	protected double g(double a)
	{
		return a;
	}
	
	private double mode(double a, double b)
	{
		double ret = a % b;
		return ret < 0 ? ret + b : ret;
	}
	
	private int mode(long a, long b)
	{
		long ret = a % b;
		return (int) (ret < 0 ? ret + b : ret);
	}
}