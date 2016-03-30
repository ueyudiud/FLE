package farcore.lib.collection;

import java.util.Arrays;

public class IntArray
{
	public int[] array;
	
	public IntArray(int length)
	{
		this.array = new int[length];
	}
		
	public IntArray(int[] array)
	{
		this.array = array;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(array);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == null ? false :
				obj == this ? true :
					!(obj instanceof IntArray) ? false : Arrays.equals(array, ((IntArray) obj).array);
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(array);
	}
	
	public IntArray copy()
	{
		return new IntArray(array.clone());
	}
}