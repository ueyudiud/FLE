package farcore.lib.collection;

public class IntegerEntry<K>
{
	K key;
	int value;
	
	public IntegerEntry(K key, int value)
	{
		this.key = key;
		this.value = value;
	}
	
	public K getKey()
	{
		return this.key;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public int setValue(int value)
	{
		int old = this.value;
		this.value = value;
		return old;
	}
}