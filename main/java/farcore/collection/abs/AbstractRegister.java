package farcore.collection.abs;

import java.util.Collection;
import java.util.Iterator;

import farcore.util.FleLog;

/**
 * Abstract register type.<br>
 * Provide default collection type and register iterator. It will
 * effect the list element.
 * You can not add element in collection, but remove element in collection.<br>
 * <code>
 * try<br>
 * {<br>
 * return AbstractRegister.this.remove((String) arg0);<br>
 * }<br>
 * catch(ClassCastException e)<br>
 * {<br>
 * return false;<br>
 * }<br>
 * </code>
 * 
 * @author ueyudiud
 * @see farcore.collection.abs.IRegister
 * @param <T> Target type in list.
 */
public abstract class AbstractRegister<T> implements IRegister<T>
{
	
	class RegisterCollection implements Collection<T>
	{
		@Override
		public boolean add(T arg0)
		{
			throw new UnsupportedOperationException("Can not add target to register by this method.");
		}

		@Override
		public boolean addAll(Collection<? extends T> arg0)
		{
			throw new UnsupportedOperationException("Can not add targets to register by this method.");
		}

		@Override
		public boolean contains(Object arg0)
		{
			try
			{
				int hash = arg0.hashCode();
				for(Object rawTarget : getObjectList())
				{
					T target = (T) rawTarget;
					if(target == null) continue;
					if(target.hashCode() == hash &&
							target.equals(arg0))
						return true;
				}
				return false;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		@Override
		public boolean containsAll(Collection<?> arg0)
		{
			if(arg0 == null) return true;
			for(Object obj : arg0)
			{
				if(!contains(obj)) return false;
			}
			return true;
		}

		@Override
		public boolean isEmpty()
		{
			try
			{
				return empty();
			}
			catch(Exception e)
			{
				FleLog.error("The register " + AbstractRegister.this.toString() + " contains throw a bug, return true default.", e);
				return true;
			}
		}

		@Override
		public boolean remove(Object arg0)
		{
			try
			{
				return remove((T) arg0);
			}
			catch(ClassCastException e)
			{
				return false;
			}
		}

		@Override
		public boolean removeAll(Collection<?> arg0)
		{
			boolean flag = true;
			for(Object target : arg0)
			{
				if(!remove(target)) flag = false;
			}
			return flag;
		}

		@Override
		public boolean retainAll(Collection<?> arg0)
		{
			boolean flag = true;
			for(String target : keySet())
			{
				if(!arg0.contains(target))
					remove(target);
			}
			return true;
		}

		@Override
		public Object[] toArray()
		{
			return targetSet().toArray();
		}

		@Override
		public <R> R[] toArray(R[] arg0)
		{
			return targetSet().toArray(arg0);
		}

		@Override
		public void clear()
		{
			AbstractRegister.this.clear();
		}

		@Override
		public Iterator<T> iterator()
		{
			return new RegisterIterator();
		}

		@Override
		public int size()
		{
			return AbstractRegister.this.size();
		}
	}
	
	class NameCollection implements Collection<String>
	{
		@Override
		public boolean add(String arg0)
		{
			throw new UnsupportedOperationException("Can not add target to register by this method.");
		}

		@Override
		public boolean addAll(Collection<? extends String> arg0)
		{
			throw new UnsupportedOperationException("Can not add targets to register by this method.");
		}

		@Override
		public boolean contains(Object arg0)
		{
			try
			{
				return contain((String) arg0);
			}
			catch(ClassCastException e)
			{
				return false;
			}
		}

		@Override
		public boolean containsAll(Collection<?> arg0)
		{
			if(arg0 == null) return true;
			for(Object obj : arg0)
			{
				if(!contains(obj)) return false;
			}
			return true;
		}

		@Override
		public boolean isEmpty()
		{
			try
			{
				return empty();
			}
			catch(Exception e)
			{
				FleLog.error("The register " + AbstractRegister.this.toString() + " contains throw a bug, return true default.", e);
				return true;
			}
		}

		@Override
		public boolean remove(Object arg0)
		{
			try
			{
				return AbstractRegister.this.remove((String) arg0);
			}
			catch(ClassCastException e)
			{
				return false;
			}
		}

		@Override
		public boolean removeAll(Collection<?> arg0)
		{
			boolean flag = true;
			for(Object target : arg0)
			{
				if(!remove(target)) flag = false;
			}
			return flag;
		}

		@Override
		public boolean retainAll(Collection<?> arg0)
		{
			boolean flag = true;
			for(String target : keySet())
			{
				if(!arg0.contains(target))
					remove(target);
			}
			return true;
		}

		@Override
		public Object[] toArray()
		{
			return keySet().toArray();
		}

		@Override
		public <R> R[] toArray(R[] arg0)
		{
			return keySet().toArray(arg0);
		}

		@Override
		public void clear()
		{
			AbstractRegister.this.clear();
		}

		@Override
		public Iterator<String> iterator()
		{
			return new RegisterStringIterator();
		}

		@Override
		public int size()
		{
			return AbstractRegister.this.size();
		}
	}

	abstract class RegItr
	{
		private int length = 0;
		private int id = -1;
		
		public boolean hasNext()
		{
			return length < AbstractRegister.this.size();
		}

		public int check()
		{
			while(id < AbstractRegister.this.getObjectList().length)
			{
				++id;
				if(name(id) != null) break;
			}
			++length;
			return id;
		}

		public void remove()
		{
			AbstractRegister.this.remove(id);
			--length;
		}
	}
	
	class RegisterIterator extends RegItr implements Iterator<T>
	{
		@Override
		public T next()
		{
			return get(check());
		}
	}
	
	class RegisterStringIterator extends RegItr implements Iterator<String>
	{
		@Override
		public String next()
		{
			return name(check());
		}
	}
	
	private NameCollection col1;
	private RegisterCollection col2;
	
	@Override
	public Collection<String> asNameCollection()
	{
		return col1 == null ? col1 = new NameCollection() : col1;
	}
	
	@Override
	public Collection<T> asCollection()
	{
		return col2 == null ? col2 = new RegisterCollection() : col2;
	}
	
	public abstract void clear();
	
	protected abstract Object[] getObjectList();
	
	protected abstract boolean remove(String name);	
	protected abstract boolean remove(T target);

	@Override
	public Iterator<T> iterator()
	{
		return new RegisterIterator();
	}
}