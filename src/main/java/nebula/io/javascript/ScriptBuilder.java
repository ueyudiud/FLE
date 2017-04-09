/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.io.javascript;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
public class ScriptBuilder
{
	private ScriptEngine engine;
	private ImmutableList.Builder<IScriptObjectDecoder<?>> decoders = ImmutableList.builder();
	private ImmutableList.Builder<IScriptObjectEncoder<?>> encoders = ImmutableList.builder();
	private ImmutableMap.Builder<String, Object> globleValues = ImmutableMap.builder();
	private ByteArrayOutputStream functions = new ByteArrayOutputStream();
	
	public ScriptBuilder(InputStream stream, ScriptEngine engine) throws IOException, ScriptException
	{
		this(IOUtils.toByteArray(stream), engine);
	}
	public ScriptBuilder(String string, ScriptEngine engine) throws IOException, ScriptException
	{
		this(string.getBytes(), engine);
	}
	public ScriptBuilder(Reader reader, ScriptEngine engine) throws IOException, ScriptException
	{
		this(IOUtils.toByteArray(reader), engine);
	}
	public ScriptBuilder(byte[] values, ScriptEngine engine) throws ScriptException
	{
		this(engine);
		try
		{
			this.functions.write(values);
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}
	public ScriptBuilder(ScriptEngine engine)
	{
		this.engine = engine;
	}
	
	public ScriptBuilder registerCoder(Object...objects)
	{
		for(Object object : objects)
		{
			if(object instanceof IScriptObjectDecoder)
			{
				this.decoders.add((IScriptObjectDecoder<?>) object);
			}
			if(object instanceof IScriptObjectEncoder)
			{
				this.encoders.add((IScriptObjectEncoder<?>) object);
			}
		}
		return this;
	}
	
	public ScriptBuilder putGlobleValues(Map<String, Object> valueMap)
	{
		this.globleValues.putAll(valueMap);
		return this;
	}
	
	public ScriptBuilder putGlobleValue(String key, Object value)
	{
		this.globleValues.put(key, value);
		return this;
	}
	
	public ScriptBuilder addFunction(String function)
	{
		try
		{
			this.functions.write(function.getBytes());
		}
		catch (IOException exception)
		{
			;
		}
		return this;
	}
	
	public ScriptHandler build()
	{
		return new ScriptHandler(this.engine, this.encoders.build(), this.decoders.build(), this.globleValues.build(), this.functions.toByteArray());
	}
}