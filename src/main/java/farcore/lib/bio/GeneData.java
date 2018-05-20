/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Random;

/**
 * @author ueyudiud
 */
public class GeneData
{
	public static final int TOTAL = 10000;
	
	public static int[][] m(int[]... map) { return map; }
	public static int[]   v(int...   map) { return map; }
	
	private final int length;
	private final int[] buf;
	public final int[][] simple;
	public final int[] def;
	
	public GeneData(int[] def, int[][] matrix)
	{
		this.def = def;
		this.buf = new int[this.length = matrix.length];
		this.simple = matrix;
	}
	
	public int[] next(int index)
	{
		int[] in = new int[this.length];
		in[index] = TOTAL;
		return next(in, in);
	}
	
	public byte nextObserve(byte index, Random rand)
	{
		int r = rand.nextInt(TOTAL);
		int i = 0;
		while ((r -= this.simple[i][index]) >= 0)
		{
			i ++;
		}
		return (byte) i;
	}
	
	public byte firstObserve(Random rand)
	{
		return nextObserve(this.def, rand);
	}
	
	public byte nextObserve(int[] in, Random rand)
	{
		assert this.length == in.length;
		for (int i = 0; i < this.length; ++i)
		{
			this.buf[i] = 0;
			for (int j = 0; j < this.length; ++j)
			{
				this.buf[i] += this.simple[i][j] * in[j];
			}
		}
		int r = rand.nextInt(TOTAL * TOTAL);
		int i = 0;
		while ((r -= this.buf[i]) >= 0)
		{
			i ++;
		}
		return (byte) i;
	}
	
	public int[] next(int[] in)
	{
		int[] out = new int[this.length];
		for (int i = 0; i < this.length; ++i)
		{
			for (int j = 0; j < this.length; ++j)
			{
				out[i] += this.simple[i][j] * in[j];
			}
		}
		return out;
	}
	
	public int[] next(int[] in, int[] out)
	{
		assert this.length == in.length && this.length == out.length;
		for (int i = 0; i < this.length; ++i)
		{
			this.buf[i] = 0;
			for (int j = 0; j < this.length; ++j)
			{
				this.buf[i] += this.simple[i][j] * in[j];
			}
		}
		System.arraycopy(this.buf, 0, out, 0, this.length);
		return out;
	}
}
