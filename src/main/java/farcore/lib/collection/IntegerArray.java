package farcore.lib.collection;

import java.util.Arrays;
import java.util.Objects;

/**
 * The integer array, used when needed add
 * array to collection.<p>
 * 
 * @author ueyudiud
 *
 */
public class IntegerArray
{
	public final int[] array;
	
	public IntegerArray(int length)
	{
		this.array = new int[length];
	}
	
	public IntegerArray(int[] array)
	{
		this.array = Objects.requireNonNull(array);
	}
	
	public int length()
	{
		return this.array.length;
	}
	
	public IntegerArray set(int id, int value)
	{
		this.array[id] = value;
		return this;
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(this.array);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == null ? false :
			obj == this ? true :
				!(obj instanceof IntegerArray) ?
						!(obj instanceof int[] ? false : Arrays.equals(this.array, (int[]) obj)) : Arrays.equals(this.array, ((IntegerArray) obj).array);
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(this.array);
	}
	
	public IntegerArray copy()
	{
		return new IntegerArray(this.array.clone());
	}
}