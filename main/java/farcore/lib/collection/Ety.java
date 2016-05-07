package farcore.lib.collection;

import java.util.Map.Entry;

import farcore.util.U;

public class Ety<K, V> implements Entry<K, V>
{
	K key;
	V value;
	
	public Ety(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey()
	{
		return key;
	}

	@Override
	public V getValue()
	{
		return value;
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
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		else if(!(obj instanceof Entry)) return false;
		else return U.Lang.equal(key, ((Entry) obj).getKey());
	}
	
	@Override
	public String toString()
	{
		return "K=" + key + ",V=" + value;
	}
}