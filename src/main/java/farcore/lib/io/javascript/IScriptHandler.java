/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.io.javascript;

import java.lang.reflect.Type;

import javax.script.ScriptException;

/**
 * @author ueyudiud
 */
public interface IScriptHandler
{
	Object decode(Object value, Type type) throws ScriptException;
	
	Object encode(Object value) throws ScriptException;
}
