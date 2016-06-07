package farcore.util.matrix;

public class Gaussian implements MatrixResolver
{
	public static final double EPSILON = 1.0E-010D;
	double[][] matrix;
	int currentRow;
	int currentColumn;
	int nZ;
  
	public static double[] lsolve(double[][] A, double[] b)
	{
		int N = b.length;
		for (int p = 0; p < N; p++)
		{
			int max = p;
			for (int i = p + 1; i < N; i++)
			{
				if (Math.abs(A[i][p]) > Math.abs(A[max][p]))
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
			if (Math.abs(A[p][p]) <= EPSILON)
			{
				return null;
			}
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
				double sum = 0.0D;
				for (int j = i + 1; j < N; j++)
				{
					sum += A[i][j] * x[j];
				}
				x[i] = ((b[i] - sum) / A[i][i]);
			}
		}
		return x;
	}
  
	public void newMatrix(int size)
	{
		this.matrix = new double[size][size];
		this.currentRow = 0;
		this.currentColumn = 0;
		this.nZ = 0;
	}
	
	public void push(double value)
	{
		if (Math.abs(value) >= EPSILON)
		{
			this.nZ += 1;
		}
		this.matrix[this.currentColumn][this.currentRow] = value;
		this.currentRow += 1;
	}
  
	public void enter()
	{
		this.currentColumn += 1;
		this.currentRow = 0;
	}
  
	public void finalized()
	{
		this.currentColumn = -1;
		this.currentRow = -1;
	}
  
	public boolean solve(double[] b)
	{
		double[][] A = new double[b.length][b.length];
		for (int i = 0; i < b.length; i++)
		{
			for (int j = 0; j < b.length; j++)
			{
				A[i][j] = this.matrix[i][j];
			}
		}
		double[] x = lsolve(A, b);
		if (x == null)
		{
			return false;
		}
		for (int i = 0; i < b.length; i++)
		{
			b[i] = x[i];
		}
		return true;
	}
  
	public void selectY(int column)
	{
		this.currentColumn = column;
		this.currentRow = 0;
	}
	
	public void setCell(double value)
	{
		this.matrix[this.currentColumn][this.currentRow] = value;
		this.currentRow += 1;
	}
  
	public int getTotalNonZeros()
	{
		return this.nZ;
	}
}