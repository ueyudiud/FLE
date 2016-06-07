package farcore.util.matrix;

import farcore.util.FleLog;
import farcore.util.V;

public interface MatrixResolver
{
	public static MatrixResolver newResolver(String name)
	{
		try
		{
			return (MatrixResolver) Class.forName(MatrixResolver.class.getPackage().getName() + "." + name).newInstance();
		}
		catch (Exception e)
		{
			FleLog.getCoreLogger().error("Invalid Matrix Solver! Please check your config settings!");
			e.printStackTrace();
		}
		return null;
	}
	
	public static MatrixResolver newResolver()
	{
		return newResolver(V.matrixSolver);
	}
	
	void newMatrix(int paramInt);
	
	void push(double paramDouble);
	
	void enter();
	
	void finalized();
	
	boolean solve(double[] array);
	
	void selectY(int column);
	
	void setCell(double value);
	
	int getTotalNonZeros();
}