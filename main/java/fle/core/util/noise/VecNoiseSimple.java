package fle.core.util.noise;

import java.util.Arrays;

import net.minecraft.util.Vec3;

public class VecNoiseSimple extends VecNoiseBase
{
	float baseLength;
	NoiseBase randLength;
	
	public VecNoiseSimple(long aSeed, float aLength)
	{
		super(aSeed);
		baseLength = aLength;
	}
	
	public VecNoiseSimple(long aSeed, float aLength, NoiseBase aRandLength)
	{
		this(aSeed, aLength);
		randLength = aRandLength;
		randLength.setSeed(aSeed * 3759275907L + 29829588671L);
	}
	
	private Vec v()
	{
		return new Vec(baseLength, 0, 0);
	}

	@Override
	public Vec noise(long x, long z)
	{
		Vec v0 = v();
		v0.rotateAroundY((float) (next(x, 10, z, 284L) * Math.PI * 2));
		if(randLength != null) v0.mutiply(randLength.noise(x, z));
		return v0;
	}

	@Override
	public Vec noise(long x, long y, long z)
	{
		Vec v0 = v();
		v0.rotateAroundX((float) (next(x, y, z, 283L) * Math.PI * 2));
		v0.rotateAroundY((float) (next(x, y, z, 699L) * Math.PI * 2));
		v0.rotateAroundZ((float) (next(x, y, z, 481L) * Math.PI * 2));
		if(randLength != null) v0.mutiply(randLength.noise(x, y, z));
		return v0;
	}
	
	@Override
	public Vec[] noise(Vec[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new Vec[w * l];
		else Arrays.fill(values, null);
		double[] length = randLength.noise(new double[w * l], x, z, w, l);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
			{
				Vec v0 = v();
				v0.rotateAroundY((float) (next(x, 10, z, 284L) * Math.PI * 2));
				if(randLength != null) v0.mutiply(length[i + j * w]);
				values[i + w * j] = v0;
			}
		return values;
	}
	
	@Override
	public Vec[] noise(Vec[] values, long x, long y, long z, int w, int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new Vec[w * l * h];
		else Arrays.fill(values, null);
		double[] length = randLength.noise(new double[w * h * l], x, y, z, w, h, l);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				for(int k = 0; k < h; ++k)
				{
					Vec v0 = v();
					v0.rotateAroundX((float) (next(x, y, z, 283L) * Math.PI * 2));
					v0.rotateAroundY((float) (next(x, y, z, 699L) * Math.PI * 2));
					v0.rotateAroundZ((float) (next(x, y, z, 481L) * Math.PI * 2));
					if(randLength != null) v0.mutiply(length[i + w * j + k * w * l]);
					values[i + w * j + k * w * l] = v0;
				}
		return values;
	}
	
	private double next(long x, long y, long z, long s)
	{
		x = ((x << 15) + y * seed) ^ x + s;
		y = ((z << 14) + z * y) ^ y + s;
		z = ((y << 13) + x * y) ^ z + s;
		long ret = x * 1843271941L + y * 492471019L + z * 4292525927L + s * 274827423L;
		return (1.0D - ((double)((ret * (ret * ret * 61493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0));
	}
}