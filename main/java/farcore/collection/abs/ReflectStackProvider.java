package farcore.collection.abs;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;

public abstract class ReflectStackProvider<S, T> implements IStackProvider<S, T>
{
	Class<S> sClass;
	Class<T> tClass;
	
	public ReflectStackProvider()
	{
		ParameterizedType type = (ParameterizedType) getClass()
				.getGenericSuperclass();
		sClass = (Class<S>) type.getActualTypeArguments()[0];
		tClass = (Class<T>) type.getActualTypeArguments()[1];
	}
	
	@Override
	public S[] newArray(int size)
	{
		return (S[]) Array.newInstance(sClass, size);
	}
}