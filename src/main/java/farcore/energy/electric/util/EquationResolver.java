package farcore.energy.electric.util;

public class EquationResolver
{
	public static final double EPSILON = 1.0E-16D;
	double[][] A;
	int x;
	int y;
	int nZ;

	public void rewind(int size)
	{
		A = new double[size][size];
		nZ = 0;
	}
	
	public void push(double v)
	{
		if (v >= EPSILON)
		{
			nZ += 1;
		}
		A[y][x] = v;
		x += 1;
	}

	public void enter()
	{
		y += 1;
		x = 0;
	}
	
	public void finalized()
	{
		x = -1;
		y = -1;
	}
	
	public boolean solve(double[] b)
	{
		double[][] A = new double[b.length][b.length];
		for (int i = 0; i < b.length; i++)
		{
			for (int j = 0; j < b.length; j++)
			{
				A[i][j] = this.A[i][j];
			}
		}
		double[] x = lsolve(A, b);
		if (x == null)
			return false;
		for (int i = 0; i < b.length; i++)
		{
			b[i] = x[i];
		}
		return true;
	}
	
	private static double[] lsolve(double[][] A, double[] b)
	{
		int N = b.length;
		for (int p = 0; p < N; p++)
		{
			int max = p;
			for (int i = p + 1; i < N; i++)
			{
				if (A[i][p] > A[max][p])
				{
					max = i;
				}
			}
			double[] temp = A[p];
			A[p] = A[max];
			A[max] = temp;
			double t = b[p];
			b[p] = b[max];
			b[max] = t;
			if (A[p][p] <= EPSILON)
				return null;
			for (int i = p + 1; i < N; i++)
			{
				if (A[p][p] != 0.0D)
				{
					double alpha = A[i][p] / A[p][p];
					b[i] -= alpha * b[p];
					for (int j = p; j < N; j++)
					{
						A[i][j] -= alpha * A[p][j];
					}
				}
			}
		}
		double[] x = new double[N];
		for (int i = N - 1; i >= 0; i--)
		{
			if (A[i][i] != 0.0D)
			{
				double sum = 0;
				for (int j = i + 1; j < N; j++)
				{
					sum += A[i][j] * x[j];
				}
				x[i] = b[i] - sum / A[i][i];
			}
		}
		return x;
	}

	/**
	 * Use in complex number.
	 * @param A
	 * @param b
	 * @return
	 */
	@Deprecated
	private static double[][] lsolve(double[][][] A, double[][] b)
	{
		int N = b.length;
		for (int p = 0; p < N; p++)
		{
			int max = p;
			for (int i = p + 1; i < N; i++)
			{
				if (absSq(A[i][p]) > absSq(A[max][p]))
				{
					max = i;
				}
			}
			double[][] temp = A[p];
			A[p] = A[max];
			A[max] = temp;
			double[] t = b[p];
			b[p] = b[max];
			b[max] = t;
			if (absSq(A[p][p]) <= EPSILON)
				return null;
			for (int i = p + 1; i < N; i++)
			{
				if (absSq(A[p][p]) != 0.0D)
				{
					double[] alpha = divide(A[i][p], A[p][p]);
					minusEq(b[i], multiply(alpha, b[p]));
					for (int j = p; j < N; j++)
					{
						minusEq(A[i][j], multiply(alpha, A[p][j]));
					}
				}
			}
		}
		double[][] x = new double[N][];
		for (int i = N - 1; i >= 0; i--)
		{
			if (absSq(A[i][i]) != 0.0D)
			{
				double[] sum = new double[2];
				for (int j = i + 1; j < N; j++)
				{
					addEq(sum, multiply(A[i][j], x[j]));
				}
				x[i] = divide(minus(b[i], sum), A[i][i]);
			}
		}
		return x;
	}
	
	public static double re(double[] d)
	{
		return d[0];
	}
	public static double im(double[] d)
	{
		return d[1];
	}
	public static double[] add(double[] d1, double[] d2)
	{
		return new double[]{d1[0] + d2[0], d1[1] + d2[1]};
	}
	public static double[] minus(double[] d1, double[] d2)
	{
		return new double[]{d1[0] - d2[0], d1[1] - d2[1]};
	}
	public static double[] multiply(double[] d1, double[] d2)
	{
		return new double[]{d1[0] * d2[0] - d1[1] * d2[1], d1[0] * d2[1] + d2[0] * d1[1]};
	}
	public static double[] divide(double[] d1, double[] d2)
	{
		double r = d1[1] * d1[1] + d2[1] * d2[1];
		return new double[]{(d1[0] * d2[0] + d1[1] * d2[1]) / r,
				(d1[1] * d2[0] - d1[0] * d2[1]) / r};
	}
	public static double[] addEq(double[] d1, double[] d2)
	{
		d1[0] += d2[0];
		d1[1] += d2[1];
		return d1;
	}
	public static double[] minusEq(double[] d1, double[] d2)
	{
		d1[0] -= d2[0];
		d1[1] -= d2[1];
		return d1;
	}
	public static double[] multiplyEq(double[] d1, double[] d2)
	{
		double a = d1[0], b = d1[1];
		d1[0] = a * d2[0] - b * d2[1];
		d1[1] = a * d2[1] + b * d2[0];
		return d1;
	}
	public static double[] divideEq(double[] d1, double[] d2)
	{
		double a = d1[0], b = d1[1];
		double r = d1[1] * d1[1] + d2[1] * d2[1];
		d1[0] = a * d2[0] + b * d2[1];
		d1[1] = b * d2[0] - a * d2[1];
		d1[0] /= r;
		d1[1] /= r;
		return d1;
	}
	public static double[] divide1Eq(double[] d)
	{
		double r = d[0] * d[0] + d[1] * d[1];
		d[0] /= r;
		d[1] /= -r;
		return d;
	}
	public static double[] divideRealEq(double d0, double[] d)
	{
		double r = d[0] * d[0] + d[1] * d[1];
		d[0] *= d0 / r;
		d[1] *= -d0 / r;
		return d;
	}
	public static double absSq(double[] d)
	{
		return d[0] * d[0] + d[1] * d[1];
	}
	public static double abs(double[] d)
	{
		return Math.sqrt(absSq(d));
	}
}