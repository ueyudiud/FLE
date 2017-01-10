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
public interface IScriptObjectEncoder<T>
{
	boolean access(Type type);
	
	Object apply(@Nullable T target, IScriptHandler handler) throws ScriptException;
}