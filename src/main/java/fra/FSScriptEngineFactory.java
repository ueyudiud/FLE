/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
public enum FSScriptEngineFactory implements ScriptEngineFactory
{
	INSTANCE;
	
	private static final Map<String, Object> PROPERTIES =
			ImmutableMap.of(
					ScriptEngine.ENGINE, INSTANCE.getEngineName(),
					ScriptEngine.ENGINE_VERSION, INSTANCE.getEngineVersion(),
					ScriptEngine.LANGUAGE, INSTANCE.getLanguageName(),
					ScriptEngine.LANGUAGE_VERSION, INSTANCE.getLanguageVersion(),
					ScriptEngine.NAME, INSTANCE.getLanguageName());
	
	private static final Joiner JOINER = Joiner.on(';');
	
	@Override
	public String getEngineName()
	{
		return "Fra FS Factory";
	}
	
	@Override
	public String getEngineVersion()
	{
		return "0.1";
	}
	
	@Override
	public List<String> getExtensions()
	{
		return ImmutableList.of();
	}
	
	@Override
	public List<String> getMimeTypes()
	{
		return ImmutableList.of("fs");
	}
	
	@Override
	public List<String> getNames()
	{
		return ImmutableList.of("fs", "farscript");
	}
	
	@Override
	public String getLanguageName()
	{
		return "farscript";
	}
	
	@Override
	public String getLanguageVersion()
	{
		return "0.1";
	}
	
	@Override
	public Object getParameter(String key)
	{
		return PROPERTIES.get(key);
	}
	
	@Override
	public String getMethodCallSyntax(String obj, String m, String...args)
	{
		return null;
	}
	
	@Override
	public String getOutputStatement(String toDisplay)
	{
		return "print(" + toDisplay + ");";
	}
	
	@Override
	public String getProgram(String...statements)
	{
		return JOINER.join(statements);
	}
	
	@Override
	public ScriptEngine getScriptEngine()
	{
		return new FarScriptEngine();
	}
}
