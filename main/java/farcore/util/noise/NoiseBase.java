package farcore.util.noise;

import farcore.interfaces.INoise;
import net.minecraft.world.gen.layer.GenLayer;

public abstract class NoiseBase implements INoise
{
	protected NoiseBase parent;
	private long seed;
	private long cache;
	
	public NoiseBase(long seed)
	{
		this.seed = seed;
	}
	
	public void setSeed(long seed)
	{
		this.seed = seed;
		if(parent != null)
		{
			parent.setSeed(38459479174L * seed + 419475919204L);
		}
	}

	@Override
	public long seed()
	{
		return seed;
	}

	private void recache()
	{
		cache = 0L;
	}
	
	public long nextLong(int x, int y, int z)
	{
		cache *= (((x ^ y) << 14) + 475917494L);
		cache *= (((y ^ z) << 14) + seed);
		cache *= (((z ^ x) << 14) + 917494179L);
		return cache * 6364136223846793005L + 1442695040888963407L;
	}

	public double next(int x, int y, int z)
	{
		long ret = nextLong(x, y, z);
		return 1.0D - (ret * (ret * (ret * 15731L + this.seed) + 782949221L) + 76312589L & 0x7FFFFFFF) / 1073741824.0D;
	}
	
	public int expand(int x, int y)
	{
		return x >= 0 ? x / y : x / y - 1;
	}
	
	public int mode(int x, int y)
	{
		return x >= 0 ? x % y : y + (x % y);
	}
	
	public double scale(int x, int y)
	{
		return (double) mode(x, y) / (double) y;
	}
}