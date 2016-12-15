package farcore.lib.collection;

import java.util.Map.Entry;

import farcore.lib.io.javascript.ScriptLoad;
import farcore.util.L;

public class Ety<K, V> implements Entry<K, V>
{
	@ScriptLoad
	public final K key;
	@ScriptLoad
	public V value;
	
	public Ety(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey()
	{
		return this.key;
	}
	
	@Override
	public V getValue()
	{
		return this.value;
	}
	
	@Override
	public V setValue(V value)
	{
		V ret = this.value;
		this.value = value;
		return ret;
	}
	
	@Override
	public int hashCode()
	{
		return this.key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		else if(!(obj instanceof Entry)) return false;
		else return L.equal(this.key, ((Entry) obj).getKey());
	}
	
	@Override
	public String toString()
	{
		return "K=" + this.key + ",V=" + this.value;
	}
}