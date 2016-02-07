package farcore.util;

import java.util.Random;

/**
 * Far core random generator.
 * @author ueyudiud
 * @see java.util.Random
 */
public class FleRandom extends Random
{
	public static final long sercilizedUUID = 38591741041781L;
	
	public FleRandom()
	{
		
	}
	public FleRandom(long seed)
	{
		super(seed);
	}
	
	/**
	 * Returns a pseudorandom, uniformly distributed <code>long</code> value between 0 (inclusive) and 
	 * the specified value (exclusive).
	 * @param n The bound on the random number to be returned. Must be positive.
	 * @return the next pseudorandom, uniformly distributed <code>long</code> value between <code>0</code> (inclusive) 
	 * and <code>n</code> (exclusive) from this random number generator's sequence.
	 */
	public long nextLong(long n)
	{
		if(n < 1) throw new IllegalArgumentException("Number must be a positive number!");
		return nextLong() % n;
	}
	
	/**
	 * Return a pseudorandom, uniformly disteributed <code>double</code> value between min value and
	 * max value.
	 * Get opposite value when max less than min value.
	 * @param min The min value of double.
	 * @param max The max value of double.
	 * @return the next pseudorandom, uniformly distributed <code>double</code> value between <code>min</code> 
	 * and <code>max</code> from this random number generator's sequence.
	 */
	public double nextDouble(double min, double max)
	{
		return min + (max - min) * nextDouble();
	}
}