package fle.core.util;

import java.util.List;

public class Arrays
{
	public static <T> T[] toArray(T t, T[] ts)
	{
		for(int i = 0; i < ts.length; ++i)
		{
			ts[i] = t;
		}
		return ts;
	}
	public static <T> List<T> toArray(T...t)
	{
		return java.util.Arrays.asList(t);
	}
}
