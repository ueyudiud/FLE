/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.io.javascript;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptException;

import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
class SimpleObjectDecoder implements IScriptObjectDecoder
{
	static final SimpleObjectDecoder DECODER = new SimpleObjectDecoder();
	
	private SimpleObjectDecoder() { }
	
	@Override
	public boolean access(Type type)
	{
		return true;
	}
	
	@Override
	public Object apply(Object code, Type type, IScriptHandler handler) throws ScriptException
	{
		if(code == null) return null;
		if(type instanceof Class)
		{
			Class class1 = (Class) type;
			if(class1.isAssignableFrom(Byte.class))
			{
				return ((Number) code).byteValue();
			}
			else if(class1.isAssignableFrom(Short.class))
			{
				return ((Number) code).shortValue();
			}
			if(class1.isAssignableFrom(Integer.class))
			{
				return ((Number) code).intValue();
			}
			if(class1.isAssignableFrom(Long.class))
			{
				return ((Number) code).longValue();
			}
			if(class1.isAssignableFrom(Float.class))
			{
				return ((Number) code).floatValue();
			}
			if(class1.isAssignableFrom(Double.class))
			{
				return ((Number) code).doubleValue();
			}
			if(class1.isAssignableFrom(Boolean.class))
			{
				return ((Boolean) code).booleanValue();
			}
			if(class1.isAssignableFrom(String.class))
			{
				return code;
			}
			if(class1.isArray())
			{
				Class<?> elementClass = class1.getComponentType();
				if(!code.getClass().isArray())
				{
					Object[] array = (Object[]) Array.newInstance(elementClass, 1);
					array[0] = handler.decode(code, elementClass);
					return array;
				}
				Object[] source = (Object[]) code;
				Object[] array = (Object[]) Array.newInstance(elementClass, source.length);
				for(int i = 0; i < source.length; handler.decode(array[i++], elementClass));
				return array;
			}
			if(class1.isAssignableFrom(List.class))
			{
				Type elementClass = ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[0];
				if(!code.getClass().isArray())
				{
					return Arrays.asList(handler.decode(code, elementClass));
				}
				Object[] source = (Object[]) code;
				Object[] array = new Object[source.length];
				for(int i = 0; i < source.length; handler.decode(array[i++], elementClass));
				return Arrays.asList(array);
			}
			if(class1.isAssignableFrom(Map.class))
			{
				ParameterizedType type2 = (ParameterizedType) class1.getGenericSuperclass();
				Type keyType = type2.getActualTypeArguments()[0];
				Type valueType = type2.getActualTypeArguments()[1];
				if(code instanceof Bindings)
				{
					return ImmutableMap.of(handler.decode(((Bindings) code).get("key"), keyType), handler.decode(((Bindings) code).get("value"), valueType));
				}
				else if(code.getClass().isArray())
				{
					ImmutableMap.Builder builder = ImmutableMap.builder();
					for(Object object : (Object[]) code)
					{
						Bindings bindings = (Bindings) object;
						builder.put(handler.decode(bindings.get("key"), keyType), handler.decode(bindings.get("value"), valueType));
					}
					return builder.build();
				}
				throw new ScriptException("No allow type detected.");
			}
			try
			{
				Bindings bindings = (Bindings) code;
				Object object = class1.newInstance();
				for(Field field : class1.getFields())
				{
					if(field.isAnnotationPresent(ScriptLoad.class))
					{
						field.set(field.getName(), handler.decode(bindings.get(field.getName()), field.getType()));
					}
				}
			}
			catch (Exception exception)
			{
				throw new ScriptException(exception);
			}
		}
		throw new ScriptException("No allow type detected.");//If no allowable type detected, throw an exception.
	}
}