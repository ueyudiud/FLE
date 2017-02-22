/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer;

import nebula.common.util.Maths;

/**
 * @author ueyudiud
 */
public abstract class Layer
{
	private static final double DOUBLE_UNIT = 0x1.0p-53;
	
	protected final long baseSeed;
	protected long worldSeed;
	protected long chunkSeed;
	protected long seed;
	
	public Layer(long seed)
	{
		this.baseSeed = seed;
	}
	
	public abstract double[] generateLayerData(long x, long z, int w, int h);
	
	public void setWorldSeed(long seed)
	{
		this.worldSeed = seed;
	}
	
	public void initChunkSeed(long x, long z)
	{
		this.chunkSeed = x * 6364136223846793005L + z * 3473849162847354191L + 1442695040888963407L;
		this.chunkSeed += this.baseSeed + this.worldSeed;
		this.seed = 483748124737182784L ^ this.chunkSeed;
	}
	
	public int nextInt(int x)
	{
		this.seed = this.seed * 483648791249246291L + this.baseSeed;
		this.seed <<= 8;
		this.seed ^= this.worldSeed;
		this.seed <<= 8;
		this.seed ^= this.chunkSeed;
		this.seed <<= 8;
		this.seed ^= 37473468401747L;
		return Maths.mod(x, (int) (this.seed >> 8));
	}
	
	public double nextDouble()
	{
		this.seed = this.seed * 483648791249246291L + this.baseSeed;
		this.seed <<= 8;
		this.seed ^= this.worldSeed;
		this.seed <<= 8;
		this.seed ^= this.chunkSeed;
		this.seed <<= 8;
		this.seed ^= 37473468401747L;
		return ((this.seed ^ (this.seed << 32)) & 0X7FFFFFFFFFFFFFFFL) * DOUBLE_UNIT;
	}
}