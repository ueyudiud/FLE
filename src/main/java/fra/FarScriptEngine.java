/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
public class FarScriptEngine extends AbstractScriptEngine implements Invocable
{
	public FarScriptEngine()
	{
		this.context.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE);
	}
	
	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException
	{
		return eval(new StringReader(script), context);
	}
	
	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException
	{
		Scanner scanner = new Scanner(reader);
		Parser parser = new Parser(scanner);
		return parser.eval(context);
	}
	
	@Override
	public FSBindings createBindings()
	{
		return new FSBindings();
	}
	
	@Override
	public void setBindings(Bindings bindings, int scope)
	{
		if (!(bindings instanceof FSBindings))
			throw new IllegalArgumentException();
		super.setBindings(bindings, scope);
	}
	
	@Override
	public ScriptEngineFactory getFactory()
	{
		return FSScriptEngineFactory.INSTANCE;
	}
	
	@Override
	public Object invokeMethod(Object thiz, String name, Object...args) throws ScriptException, NoSuchMethodException
	{
		throw new NoSuchMethodException();
	}
	
	@Override
	public Object invokeFunction(String name, Object...args) throws ScriptException, NoSuchMethodException
	{
		Object value = this.context.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
		if (value == null)
			throw new NoSuchMethodError("Method " + name + " not found.");
		try
		{
			return FSObjects.pack(value).apply(A.transform(args, IFSObject.class, FSObjects::pack));
		}
		catch (Exception exception)
		{
			throw new ScriptException(exception);
		}
	}
	
	@Override
	public <T> T getInterface(Class<T> clasz)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> T getInterface(Object thiz, Class<T> clasz)
	{
		throw new UnsupportedOperationException();
	}
}
