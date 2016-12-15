/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.io.javascript;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import farcore.lib.util.Log;

/**
 * @author ueyudiud
 */
public class ScriptHandler implements IScriptHandler
{
	private final ScriptEngine engine;
	private final Collection<IScriptObjectDecoder> decoders;
	private final Collection<IScriptObjectEncoder> encoders;
	
	ScriptHandler(ScriptEngine engine, Collection<IScriptObjectEncoder> encoders, Collection<IScriptObjectDecoder> decoders, Map<String, Object> globleValues, byte[] values)
	{
		this.engine = engine;
		this.encoders = encoders;
		this.decoders = decoders;
		this.engine.setBindings(new SimpleBindings(globleValues), ScriptContext.GLOBAL_SCOPE);
		try
		{
			this.engine.eval(new String(values));
		}
		catch (ScriptException exception)
		{
			Log.error("Invalid javascript codes.", exception);
		}
	}
	
	public void test(String key, int length) throws ScriptException
	{
		try
		{
			Invocable invocable = (Invocable) this.engine;
			invocable.invokeFunction(key, new Object[length]);
		}
		catch (NoSuchMethodException exception)
		{
			throw new ScriptException(exception);
		}
		catch (ScriptException exception)
		{
			;
		}
	}
	
	public <T> T invoke(Class<T> clazz, String key, Object...objects) throws ScriptException, NoSuchMethodException
	{
		try
		{
			Invocable invocable = (Invocable) this.engine;
			Object[] input = new Object[objects.length];
			for(int i = 0; i < input.length; ++i)
			{
				input[i] = encode(objects[i]);
			}
			Object result = invocable.invokeFunction(key, input);
			return (T) decode(result, clazz);
		}
		catch (ClassCastException exception)
		{
			throw new ScriptException(exception);
		}
	}
	
	public Object getFieldRaw(String key)
	{
		return this.engine.get(key);
	}
	
	public <T> T getField(Class<T> clazz, String key) throws ScriptException
	{
		return (T) decode(getFieldRaw(key), clazz);
	}
	
	@Override
	public Object encode(Object value) throws ScriptException
	{
		IScriptObjectEncoder encoder = null;
		Class<?> type = value.getClass();
		for(IScriptObjectEncoder coder : this.encoders)
		{
			if(coder.access(type))
			{
				encoder = coder;
				break;
			}
		}
		if(encoder == null)
		{
			encoder = SimpleObjectEncoder.ENCODER;
		}
		return encoder.apply(value, this);
	}
	
	@Override
	public Object decode(Object value, Type type) throws ScriptException
	{
		IScriptObjectDecoder decoder = null;
		for(IScriptObjectDecoder coder : this.decoders)
		{
			if(coder.access(type))
			{
				decoder = coder;
				break;
			}
		}
		if(decoder == null)
		{
			decoder = SimpleObjectDecoder.DECODER;
		}
		return decoder.apply(value, type, this);
	}
}