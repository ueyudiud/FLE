package fle.core.util.noise;

import java.util.Arrays;

import net.minecraft.util.Vec3;

public abstract class VecNoiseBase
{
	protected long seed;
	
	public VecNoiseBase(long aSeed)
	{
		seed = aSeed;
	}
	
	public VecNoiseBase setSeed(long aSeed)
	{
		seed = aSeed;
		return this;
	}
	
	public abstract Vec noise(long x, long z);
	
	public abstract Vec noise(long x, long y, long z);

	public Vec[] noise(Vec[] values, long x, long z, int w, int l)
	{
		if(values == null || values.length < w * l) values = new Vec[w * l];
		else Arrays.fill(values, null);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				values[i + w * j] = noise(x + i, z + j);
		return values;
	}
	public Vec[] noise(Vec[] values, long x, long y, long z, int w, int h, int l)
	{
		if(values == null || values.length < w * l * h) values = new Vec[w * l * h];
		else Arrays.fill(values, null);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < l; ++j)
				for(int k = 0; k < h; ++k)
					values[i + w * j + k * w * l] = noise(x + i, y + j, z + k);
		return values;
	}
	
	public final long getSeed()
	{
		return seed;
	}
	
	@Override
	public final int hashCode()
	{
		return new Long(seed).intValue();
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		return obj.getClass() == getClass() ? ((VecNoiseBase) obj).seed == seed : false;
	}
}