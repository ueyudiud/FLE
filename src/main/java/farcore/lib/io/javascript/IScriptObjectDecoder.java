/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.io.javascript;

import java.lang.reflect.Type;

import javax.annotation.Nullable;
import javax.script.ScriptException;

/**
 * @author ueyudiud
 */
public interface IScriptObjectDecoder<T>
{
	boolean access(Type type);
	
	@Nullable T apply(Object code, Type type, IScriptHandler handler) throws ScriptException;
}