/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import static java.lang.Math.abs;

import java.util.Arrays;

/**
 * @author ueyudiud
 */
class EquationResolvor
{
	/*
	 * Ax = y
	 * The 'A' is coefficients of liner equation.
	 * The 'x' is parameter of voltage and current.
	 * The 'y' is expected result of voltage and current.
	 */
	
	private final int length;
	private final double[][][] As;
	private final double[][] ys;
	private double[][] A;
	private double[] y;
	final double[] x;
	final boolean[] free;
	
	EquationResolvor(int length, double[][][] As, double[][] ys)
	{
		assert As.length == ys.length;
		this.length = length;
		this.As = As;
		this.ys = ys;
		this.x = new double[length];
		this.A = new double[As.length][length];
		this.y = new double[As.length];
		this.free = new boolean[length];
	}
	
	void solve()
	{
		if (this.length <= 1)
			return;
		for (int i = 0; i < this.A.length; ++i)
		{
			this.y[i] = get(this.ys[i]);
			for (int j = 0; j < this.length; ++j)
			{
				this.A[i][j] = get(this.As[i][j]);
			}
		}
		Arrays.fill(this.free, false);
		if (!gauss(this.length, this.A, this.x, this.y, this.free))
		{
			throw new RuntimeException();
		}
	}
	
	static void set(double[][] Vs, int i, double v)
	{
		if (Vs[i] == null)
		{
			Vs[i] = new double[1];
		}
		Vs[i][0] = v;
	}
	
	static double get(double[] v)
	{
		return v == null ? 0.0 : v[0];
	}
	
	static boolean gauss(final int length, double[][] A, double[] x, double[] y, boolean[] free_x)
	{
		int i, j, k, r = 0;
		int m;
		double v, w, mul;
		
		//Gauss Elimination
		for (i = 0; i < length; ++i)
		{
			w = 0.0;
			m = -1;
			for (j = i; j < A.length; ++j)
			{
				if ((v = abs(A[j][i])) > w)
				{
					w = v;
					m = j;
				}
			}
			
			if (m == -1)
			{
				if (free_x == null)
					return false;
				free_x[i] = true;
				continue;
			}
			w = A[m][i];
			
			//Swap Row.
			if (r != m)
			{
				double[] a = A[m]; A[m] = A[r]; A[r] = a;
				double   t = y[m]; y[m] = y[r]; y[r] = t;
			}
			
			for (j = r + 1; j < A.length; ++j)
			{
				mul = A[j][i] / w;
				A[j][r] = 0.0;
				if (mul == 0.0 || mul == -.0)
					continue;
				for (k = r + 1; k < length; ++k)
				{
					A[j][k] -= mul * A[r][k];
				}
				y[j] -= mul * y[r];
			}
			
			r ++;
		}
		
		//Check Solution Count
		for (i = A.length - 1; i >= r; --i)
		{
			if (Math.abs(y[i]) > 1E-10)
			{
				return false;
			}
		}
		
		//Forward Substitution
		k = i;
		i = length - 1;
		for (; i >= 0; --i)
		{
			if (free_x != null && free_x[i])
			{
				x[i] = 0.0;
			}
			else
			{
				x[i] = y[k];
				for (j = i + 1; j < length; ++j)
				{
					x[i] -= A[k][j] * x[j];
				}
				x[i] /= A[k][i];
			}
			if (free_x == null || !free_x[i])
				k --;
		}
		
		return true;
	}
}
