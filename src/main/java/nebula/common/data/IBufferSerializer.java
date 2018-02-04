/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.data;

import java.io.IOException;

import com.google.common.reflect.TypeToken;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public interface IBufferSerializer<B, T>
{
	default boolean changed(T oldValue, T newValue)
	{
		return L.equals(oldValue, newValue);
	}
	
	void write(B buffer, T value);
	
	T read(B buffer) throws IOException;
	
	Class<T> getTargetClass();
	
	abstract class Impl<B, T> implements IBufferSerializer<B, T>
	{
		private final Class<T> token = (Class<T>) new TypeToken<T>(getClass())
		{
			private static final long serialVersionUID = -7631524159611506004L;
		}.getRawType();
		
		public final Class<T> getTargetClass()
		{
			return token;
		}
	}
}