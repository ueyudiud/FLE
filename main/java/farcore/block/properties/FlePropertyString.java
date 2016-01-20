package farcore.block.properties;

import java.util.Collection;

import farcore.collection.abs.IRegister;

public class FlePropertyString<T> extends FleProperty<String>
{
	final IRegister<T> register;
	
	public FlePropertyString(String name, IRegister<T> register)
	{
		super(name, String.class);
		this.register = register;
	}
	
	@Override
	protected Collection<String> getCollection()
	{
		return register.asNameCollection();
	}

	@Override
	protected String name(String value)
	{
		return value;
	}
	
	public T getValue(String value)
	{
		return register.get(value);
	}
}