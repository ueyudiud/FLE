package farcore.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import farcore.collection.abs.IRegister;
import flapi.util.FleLog;

/**
 * The register of object with name.
 * @author ueyudiud
 *
 * @param <T> The register obj super type.
 */
public class Register<T> implements IRegister<T>
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
		for(int i = 0; i < oL.length; ++i)
			if(oL[i] != null && oL[i].equals(t))
				return i;
		return -1;
	}
	public int serial(String name)
	{
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
	
	@Override
	public Iterator<T> iterator() 
	{
		return new RegisterIterator();
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
	
	public String[] keySet()
	{
		return sL.clone();
	}

	public class RegisterIterator implements Iterator<T>
	{
		private int length = 0;
		private int id = -1;

		@Override
		public boolean hasNext()
		{
			return length < size;
		}

		@Override
		public T next()
		{
			while(id < oL.length)
			{
				++id;
				if(get(id) != null) break;
			}
			++length;
			return get(id);
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	public List<T> toList()
	{
		return new ArrayList(Arrays.asList(oL));
	}
}