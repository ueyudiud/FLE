/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import java.util.Arrays;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author ueyudiud
 */
class Resolvor
{
	/*
	 * δI = ΣI/Σ(dI/dU)
	 */
	
	boolean liner = true;
	
	int[][] linkmap;
	
	private int rule, nodeCount;
	int length;
	
	double[][][] As;
	double[][] ys;
	
	EquationResolvor resolvor;
	
	double[] getCoefficientPtr(int n)
	{
		return this.As[this.rule][n];
	}
	
	void setCoefficient(int n, double v)
	{
		this.As[this.rule][n][0] = v;
	}
	
	double[] getRightValuePtr()
	{
		return this.ys[this.rule];
	}
	
	void setRightValue(double v)
	{
		this.ys[this.rule][0] = v;
	}
	
	void initalize(Linker linker)
	{
		int N = linker.N;
		int G = linker.graphs.size();
		int L = linker.list.size();
		this.length = N + G + L;
		this.As = new double[this.length][N + L][];
		this.ys = new double[this.length][];
		this.resolvor = new EquationResolvor(N + L, this.As, this.ys);
		this.linkmap = new int[N][];
		for (int i = 0; i < N; ++i)
		{
			int[] m = this.linkmap[i] = new int[i];
			for (int j = 0; j < i; ++j)
			{
				m[j] = linker.list.indexOf((long) i << 32 | j) + N;
			}
		}
		int x = 0;
		for (x = 0; x < N; ++ x)
		{
			for (int i : linker.links[x])
			{
				this.As[x][i + N] = new double[] {i > x ? 1 : -1};
			}
		}
		x = N;
		for (IntSet set : linker.graphs)
		{
			this.As[x][set.iterator().nextInt()] = new double[] {1};
			x++;
		}
		this.nodeCount = N;
		this.rule = N + G;
	}
	
	void startRule()
	{
		if (this.rule > this.length)
			throw new RuntimeException();
	}
	
	void endRule()
	{
		this.rule ++;
	}
	
	private int linkid(int n1, int n2)
	{
		assert n1 != n2;
		return n1 > n2 ? this.linkmap[n1][n2] : this.linkmap[n2][n1];
	}
	
	void stampSimple(int n1, int n2, double[] c, double[] r)
	{
		startRule();
		int n3 = linkid(n1, n2);
		if (n1 > n2)
		{
			set(this.As[this.rule], n1, -1);
			set(this.As[this.rule], n2, 1);
		}
		else
		{
			set(this.As[this.rule], n1, 1);
			set(this.As[this.rule], n2, -1);
		}
		this.As[this.rule][n3] = c;
		this.ys[this.rule] = r;
		endRule();
	}
	
	private static void set(double[][] a, int i, double v)
	{
		(a[i] == null ? a[i] = new double[1] : a[i])[0] = v;
	}
	
	void solve()
	{
		if (this.length <= 1)
			return;
		if (this.liner)
		{
			this.resolvor.solve();
		}
		else
		{
			//TODO
		}
	}
	
	double result(int n)
	{
		return this.resolvor.x[n];
	}
	
	double result(int n1, int n2)
	{
		return this.resolvor.x[linkid(n1, n2)];
	}
	
	double voltage(int n1, int n2)
	{
		return result(n1) - result(n2);
	}
	
	double current(int n1, int n2)
	{
		double r = result(n1, n2);
		return n1 < n2 ? r : -r;
	}
	
	double[] results()
	{
		return this.resolvor.x;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.length; ++i)
		{
			builder.append(Arrays.toString(this.As[i][0]));
			for (int j = 1; j < this.As[i].length; ++j)
			{
				builder.append('+').append(Arrays.toString(this.As[i][j]));
			}
			builder.append('=').append(Arrays.toString(this.ys[i]));
			builder.append('\r');
		}
		return builder.toString();
	}
}
