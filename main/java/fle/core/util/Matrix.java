package fle.core.util;

public class Matrix
{
	private static final double EPSILON = 1E-12;
	
	private int size;
	private double[][] m;
	public double[] result;
	private int x;
	private int y;
	private boolean solved;
	
	public void reset(int size)
	{
		this.size = size;
		m = new double[size][size];
		result = null;
		x = 0;
		y = 0;
		solved = false;
	}
	
	public double[] solve()
	{
		if(solved) throw new RuntimeException("The matrix had already solved!");
		result = new double[size];
		
		int N = result.length;
		double[][] A = new double[size][size];
		for(int i = 0; i < N; ++i)
			A[i] = m[i].clone();
		
		for (int p = 0; p < N; p++) 	
		{
			// find pivot row and swap
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
            double t = result[p];
            result[p] = result[max];
            result[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON)
            {
                return null;
            }

            // pivot within A and result
            for (int i = p + 1; i < N; i++)
            {
                if (A[p][p] != 0)
                {
                	//Ignore any line with all zero
                    double alpha = A[i][p] / A[p][p];
                    result[i] -= alpha * result[p];
                    for (int j = p; j < N; j++)
                    {
                        A[i][j] -= alpha * A[p][j];
                    }
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--)
        {
            if (A[i][i] != 0)
            {
            	//Ignore any line with all zero
                double sum = 0.0;
                for (int j = i + 1; j < N; j++)
                {
                    sum += A[i][j] * x[j];
                }
                x[i] = (result[i] - sum) / A[i][i];
            }
        }
        for(int i = 0; i < N; ++i) result[i] = x[i];
        return x;
	}
	
	public void push()
	{
		assert(x < size);
		++x;
	}
	
	public void push(double value)
	{
		solved = false;
		m[y][x] = value;
		push();
	}
	
	public void enter()
	{
		assert(y < size);
		++y;
	}
	
	public void finalized()
	{
		x = -1;
		y = -1;
	}
	
	public void selectY(int y)
	{
		this.y = y;
		this.x = 0;
	}
	
	public void selectX(int x)
	{
		this.x = x;
	}
	
	public double get()
	{
		return m[y][x];
	}
}