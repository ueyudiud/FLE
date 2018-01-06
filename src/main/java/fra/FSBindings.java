/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.SimpleBindings;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

/**
 * @author ueyudiud
 */
class FSBindings implements Bindings
{
	Bindings parent = new SimpleBindings();
	
	Map<String, FSObjectInBinding> map = new HashMap<>();
	
	@Override
	public int size()
	{
		return this.parent.size() + this.map.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return this.parent.isEmpty() && this.map.isEmpty();
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return this.parent.containsKey(value);//Force to false.
	}
	
	@Override
	public void clear()
	{
		this.parent.clear();
		this.map.clear();
	}
	
	@Override
	public Set<String> keySet()
	{
		return this.map.keySet();
	}
	
	@Override
	public Collection<Object> values()
	{
		return Collections2.transform(this.map.values(), FSObjects::unpack);
	}
	
	@Override
	public Set<Entry<String, Object>> entrySet()
	{
		return Maps.transformValues(this.map, FSObjects::unpack).entrySet();
	}
	
	@Override
	public Object put(String name, Object value)
	{
		FSObjectInBinding object = this.map.computeIfAbsent(name, FSObjectInBinding::new);
		IFSObject old = object.object;
		object.object = FSObjects.pack(object);
		return old == uninitalized ? null : old;
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge)
	{
		for (Entry<? extends String, ?> entry : toMerge.entrySet())
		{
			FSObjectInBinding object = this.map.computeIfAbsent(entry.getKey(), FSObjectInBinding::new);
			object.object = FSObjects.pack(entry.getValue());
		}
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return this.parent.containsKey(key) || this.map.containsKey(key);
	}
	
	@Override
	public Object get(Object key)
	{
		return getOrDefault(key, null);
	}
	
	@Override
	public Object getOrDefault(Object key, Object defaultValue)
	{
		IFSObject value = this.map.get(key).object;
		return value == null ? this.parent.getOrDefault(key, defaultValue) : FSObjects.unpack(value);
	}
	
	@Override
	public Object remove(Object key)
	{
		IFSObject value = this.map.remove(key).object;
		return value == null ? this.parent.remove(key) : FSObjects.unpack(value);
	}
	
	IFSObject getOrCreate(String key)
	{
		return this.map.computeIfAbsent(key, FSObjectInBinding::new);
	}
	
	private static final IFSObject uninitalized = new FSObject<>(null);
	
	class FSObjectInBinding extends WrapperFSObject
	{
		String name;
		
		FSObjectInBinding(String name)
		{
			this(name, uninitalized);
		}
		FSObjectInBinding(String name, Object value)
		{
			this(name, FSObjects.pack(value));
		}
		FSObjectInBinding(String name, IFSObject value)
		{
			super(value);
			this.name = name;
		}
		
		@Override
		protected IFSObject object()
		{
			if (this.object == uninitalized)
			{
				throw new IllegalStateException();
			}
			return this.object;
		}
		
		@Override
		public IFSObject operator_lincr()
		{
			if (this.object instanceof FSInt)
			{
				((FSInt) this.object).value ++;
				return this.object;
			}
			else if (this.object instanceof FSFloat)
			{
				((FSFloat) this.object).value ++;
				return this.object;
			}
			return super.operator_lincr();
		}
		
		@Override
		public IFSObject operator_ldesc()
		{
			if (this.object instanceof FSInt)
			{
				((FSInt) this.object).value --;
				return this.object;
			}
			else if (this.object instanceof FSFloat)
			{
				((FSFloat) this.object).value --;
				return this.object;
			}
			return super.operator_ldesc();
		}
		
		@Override
		public IFSObject operator_rincr()
		{
			if (this.object instanceof FSInt)
			{
				FSInt result = ((FSInt) this.object).clone();
				((FSInt) this.object).value ++;
				return result;
			}
			else if (this.object instanceof FSFloat)
			{
				FSFloat result = ((FSFloat) this.object).clone();
				((FSFloat) this.object).value ++;
				return result;
			}
			return super.operator_rincr();
		}
		
		@Override
		public IFSObject operator_rdesc()
		{
			if (this.object instanceof FSInt)
			{
				FSInt result = ((FSInt) this.object).clone();
				((FSInt) this.object).value --;
				return result;
			}
			else if (this.object instanceof FSFloat)
			{
				FSFloat result = ((FSFloat) this.object).clone();
				((FSFloat) this.object).value --;
				return result;
			}
			return super.operator_rincr();
		}
		
		@Override
		public IFSObject operator_set(IFSObject object)
		{
			return this.object = object.eval();
		}
		
		@Override
		public void operator_delete(IFSObject object)
		{
			FSBindings.this.map.remove(this.name);
			this.object = uninitalized;
		}
	}
	
}