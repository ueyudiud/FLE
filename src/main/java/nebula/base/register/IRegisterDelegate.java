/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.register;

/**
 * @author ueyudiud
 */
public interface IRegisterDelegate<T>
{
	T get();
	
	String name();
	
	int id();
	
	void changeRef(T value);
	
	void changeID(int id);
	
	boolean isDelegateEqual(IRegisterDelegate<T> delegate);
	
	class SimpleRegisterDelegate<T> implements IRegisterDelegate<T>
	{
		public T value;
		public String name;
		
		public SimpleRegisterDelegate(T value, String name)
		{
			this.name = name;
			this.value = value;
		}
		
		@Override
		public T get()
		{
			return value;
		}
		
		@Override
		public String name()
		{
			return name;
		}
		
		@Override
		public int id()
		{
			return -1;
		}
		
		@Override
		public void changeRef(T value)
		{
			this.value = value;
		}
		
		@Override
		public void changeID(int id)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean isDelegateEqual(IRegisterDelegate<T> delegate)
		{
			return delegate.get() != null ? delegate.get() == value : delegate.name() == name;
		}
	}
}