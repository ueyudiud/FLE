package farcore.util.matrix;

import java.util.LinkedList;

import edu.emory.mathcs.csparsej.tdouble.Dcs_common;
import edu.emory.mathcs.csparsej.tdouble.Dcs_qrsol;
import edu.emory.mathcs.csparsej.tdouble.Dcs_util;

public class QR implements MatrixResolver
{
	public static final double EPSILON = 1.0E-010D;
	int size;
	int[] Ap;
	LinkedList<Integer> AiList;
	LinkedList<Double> AxList;
	int currentColumn;
	int currentRow;
	int nZ;
	Dcs_common.Dcs matrix;
	  
	public void newMatrix(int size)
	{
		this.size = size;
	    this.Ap = new int[size + 1];
	    this.Ap[0] = 0;
	    this.AiList = new LinkedList();
	    this.AxList = new LinkedList();
	    this.currentColumn = 0;
	    this.currentRow = 0;
	    this.nZ = 0;
	}
	  
	public void push(double value)
	{
		if (Math.abs(value) < EPSILON)
	    {
			this.currentRow += 1;
	    }
	    else
	    {
	    	this.AiList.add(Integer.valueOf(this.currentRow));
	    	this.AxList.add(Double.valueOf(value));
	    	this.currentRow += 1;
	    	this.nZ += 1;
	    }
	}
	  
	public void enter()
	{
		this.currentColumn += 1;
	    this.currentRow = 0;
	    this.Ap[this.currentColumn] = this.nZ;
	}
	  
	public void finalized()
	{
	    this.matrix = Dcs_util.cs_spalloc(this.size, this.size, this.nZ, true, false);
	    this.matrix.p = this.Ap;
	    

	    Integer i = (Integer)this.AiList.poll();
	    int j = 0;
	    while (i != null)
	    {
	    	this.matrix.i[j] = i.intValue();
	    	i = (Integer)this.AiList.poll();
	    	j++;
	    }
	    Double k = (Double)this.AxList.poll();
	    j = 0;
	    while (k != null)
	    {
	      this.matrix.x[j] = k.doubleValue();
	      k = (Double)this.AxList.poll();
	      j++;
	    }
	    this.size = -1;
	    this.Ap = null;
	    this.AiList = null;
	    this.AxList = null;
	    this.currentColumn = -1;
	    this.currentRow = -1;
	    this.nZ = -1;
	  }
	  
	public boolean solve(double[] b)
	{
		return Dcs_qrsol.cs_qrsol(1, this.matrix, b);
	}
	  
	public void selectY(int column)
	{
	    this.currentColumn = column;
	    this.currentRow = 0;
	    this.nZ = 0;
	}
	  
	public void setCell(double value)
	{
		if (Math.abs(value) < 1.0E-010D)
	    {
			this.currentRow += 1;
	    }
	    else
	    {
	    	this.matrix.x[(this.matrix.p[this.currentColumn] + this.nZ)] = value;
	    	this.currentRow += 1;
	    	this.nZ += 1;
	    }
	}
	  
	public int getTotalNonZeros()
	{
		if (this.matrix == null)
		{
			return 0;
	    }
	    return this.matrix.nzmax;
	}
}