/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.io.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableList;

/**
 * @author ueyudiud
 */
public class ScriptBuilder
{
	private ScriptEngine engine;
	private ImmutableList.Builder<IScriptObjectDecoder> decoders = ImmutableList.builder();
	private ImmutableList.Builder<IScriptObjectEncoder> encoders = ImmutableList.builder();
	
	public ScriptBuilder(InputStream stream, ScriptEngine engine) throws IOException, ScriptException
	{
		this(IOUtils.toByteArray(stream), engine);
	}
	public ScriptBuilder(Reader reader, ScriptEngine engine) throws IOException, ScriptException
	{
		this(engine);
		engine.eval(reader);
	}
	public ScriptBuilder(byte[] values, ScriptEngine engine) throws ScriptException
	{
		this(engine);
		engine.eval(new String(values));
	}
	public ScriptBuilder(String string, ScriptEngine engine) throws IOException, ScriptException
	{
		this(engine);
		engine.eval(string);
	}
	public ScriptBuilder(ScriptEngine engine)
	{
		this.engine = engine;
	}
	
	public ScriptBuilder registerCoder(Object object)
	{
		if(object instanceof IScriptObjectDecoder)
		{
			this.decoders.add((IScriptObjectDecoder) object);
		}
		if(object instanceof IScriptObjectEncoder)
		{
			this.encoders.add((IScriptObjectEncoder) object);
		}
		return this;
	}
	
	public ScriptHandler build()
	{
		return new ScriptHandler(this.engine, this.encoders.build(), this.decoders.build());
	}
}