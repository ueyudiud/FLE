package farcore.lib.collection;

import java.util.Arrays;
import java.util.Objects;

/**
 * The integer array, used when needed add
 * array to collection.
 * @author ueyudiud
 *
 */
public class IntegerArray
{
	public final int[] array;
	
	public IntegerArray(int length)
	{
		array = new int[length];
	}
	
	public IntegerArray(int[] array)
	{
		this.array = Objects.requireNonNull(array);
	}
	
	public int length()
	{
		return array.length;
	}
	
	public IntegerArray set(int id, int value)
	{
		array[id] = value;
		return this;
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
				!(obj instanceof IntegerArray) ?
						!(obj instanceof int[] ? false : Arrays.equals(array, (int[]) obj)) : Arrays.equals(array, ((IntegerArray) obj).array);
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(array);
	}
	
	public IntegerArray copy()
	{
		return new IntegerArray(array.clone());
	}
}