package farcore.collection;

import java.util.Arrays;
import java.util.Set;

import farcore.collection.abs.AbstractRegister;
import farcore.util.FleLog;

/**
 * The register of object with name.
 * @author ueyudiud
 *
 * @param <T> The register o
 * j super type.
 */
public class Register<T> extends AbstractRegister<T>
{
	private int size = 0;
	private int i = 0;
	private Object[] oL;
	private String[] sL;

	public Register()
	{
		this(16);
	}
	public Register(int size)
	{
		oL = new Object[size];
		sL = new String[size];
	}
	
	private synchronized void addSize(int size)
	{
		oL = Arrays.copyOf(oL, size);
		sL = Arrays.copyOf(sL, size);
	}
	private synchronized int getNextAccessID()
	{
		while(contain(i)) ++i;
		return i;
	}

	/**
	 * Register new object to register.
	 * @param t
	 * @param name
	 * @return
	 */
	public int register(T t, String name)
	{
		getNextAccessID();
		register(i, t, name);
		return i;
	}
	public void register(int i, T t, String name)
	{
		if(oL.length <= i) 
		{
			addSize(i * 2);
		}
		if(oL[i] != null || sL[i] != null) 
		{
			FleLog.getLogger().throwing(new RuntimeException("Fla API : Registry has tag name " + name + "."));
			return;
		}
		oL[i] = t;
		sL[i] = name;
		++size;
	}

	public int serial(T t)
	{
		if(t == null) return -1;
		for(int i = 0; i < oL.length; ++i)
			if(oL[i] != null && oL[i].equals(t))
				return i;
		return -1;
	}
	public int serial(String name)
	{
		if(name == null) return -1;
		for(int i = 0; i < sL.length; ++i)
			if(sL[i] != null && sL[i].equals(name))
				return i;
		return -1;
	}
	
	public String name(T t)
	{
		return name(serial(t));
	}
	public String name(int i)
	{
		return i < 0 || i > sL.length ? null : sL[i];
	}

	public T get(String tag)
	{
		return get(serial(tag));
	}
	public T get(int i)
	{
		return i < 0 || i > oL.length ? null : (T) oL[i];
	}

	public int size() 
	{
		return size;
	}

	public synchronized boolean remove(int i)
	{
		T target = get(i);
		String str = name(i);
		if(target == null || str == null) return false;
		sL[i] = null;
		oL[i] = null;
		this.i = Math.min(this.i, i);
		--size;
		return true;
	}
	
	@Override
	public boolean empty()
	{
		return size == 0;
	}

	public boolean contain(String aName) 
	{
		return serial(aName) != -1;
	}

	public boolean contain(int id) 
	{
		return id < oL.length ? oL[id] != null : false;
	}
	
	public void clear()
	{
		Arrays.fill(oL, null);
		Arrays.fill(sL, null);
		size = 0;
		i = 0;
	}
	
	public Set<String> keySet()
	{
		return CollectionUtil.asSet(sL);
	}
	
	@Override
	public Set<T> targetSet()
	{
		return CollectionUtil.<T>asSetWith(oL);
	}
	
	@Override
	public String toString()
	{
		String str = "[";
		int s = 0;
		for(int i = 0; i < oL.length; ++i)
		{
			if(oL[i] == null) continue;
			str += i + "|\"" + sL[i] + "\"" + ":" + oL[i].toString();
			++s;
			if(s < size) str += ", ";
		}
		str += "]";
		return str;
	}
	
	@Override
	protected Object[] getObjectList()
	{
		return oL;
	}
	
	@Override
	protected boolean remove(String name)
	{
		if(name == null) return false;
		int i;
		if((i = serial(name)) != -1)
		{
			remove(i);
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean remove(T target)
	{
		if(target == null) return false;
		int i;
		if((i = serial(target)) != -1)
		{
			remove(i);
			return true;
		}
		return false;
	}
}