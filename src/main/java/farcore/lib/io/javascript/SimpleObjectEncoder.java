/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.io.javascript;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import farcore.lib.collection.Ety;
import farcore.util.L;

/**
 * @author ueyudiud
 */
public class SimpleObjectEncoder implements IScriptObjectEncoder
{
	private static final Set<String> MAP_KEY_SET = ImmutableSet.of("key", "value");
	
	static final SimpleObjectEncoder ENCODER = new SimpleObjectEncoder();
	
	private SimpleObjectEncoder() { }
	
	@Override
	public boolean access(Type type)
	{
		return true;
	}
	
	@Override
	public @Nullable Object apply(@Nullable Object code, IScriptHandler handler) throws ScriptException
	{
		if(code == null)
		{
			return null;
		}
		else if(code.getClass().isArray())
		{
			Object[] objects = new Object[((Object[]) code).length];
			for(int i = 0; i < objects.length; ++i)
			{
				objects[i] = handler.encode(((Object[]) code)[i]);
			}
			return objects;
		}
		else if(code instanceof List)
		{
			Object[] objects = new Object[((List) code).size()];
			for(int i = 0; i < objects.length; ++i)
			{
				objects[i] = handler.encode(((List) code).get(i));
			}
			return objects;
		}
		else if(code instanceof Map)
		{
			List<Object> list = new ArrayList();
			for(Entry entry : ((Map<Object, Object>) code).entrySet())
			{
				Object k = handler.encode(entry.getKey());
				Object v = handler.encode(entry.getValue());
				list.add(new Bindings()
				{
					public Collection<Object> values() { return ImmutableList.of(k, v); }
					public int size() { return 2; }
					public Set<String> keySet() { return MAP_KEY_SET; }
					public boolean isEmpty() { return false; }
					public Set<Entry<String, Object>> entrySet() { return ImmutableSet.of(new Ety("key", k), new Ety("value", v)); }
					public boolean containsValue(Object value) { return L.equal(k, value) || L.equal(v, value); }
					public void clear() { throw new UnsupportedOperationException(); }
					public Object remove(Object key) { throw new UnsupportedOperationException(); }
					public void putAll(Map<? extends String, ? extends Object> toMerge) { throw new UnsupportedOperationException(); }
					public Object put(String name, Object value) { throw new UnsupportedOperationException(); }
					public Object get(Object key) { return "key".equals(key) ? k : "value".equals(key) ? v : null; }
					public boolean containsKey(Object key) { return MAP_KEY_SET.contains(k); }
				});
			}
			return list.toArray();
		}
		else if(code instanceof String || code instanceof Number || code instanceof Boolean)
		{
			return code;
		}
		Class<?> clazz = code.getClass();
		SimpleBindings bindings = new SimpleBindings();
		for(Field field : clazz.getFields())
		{
			if(field.isAnnotationPresent(ScriptLoad.class))
			{
				try
				{
					bindings.put(field.getName(), field.get(code));
				}
				catch (Exception exception)
				{
					throw new ScriptException(exception);
				}
			}
		}
		return bindings;
	}
}