/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ueyudiud
 */
class FSFunc1 extends FSObject<Function>
{
	FSFunc1(Function value)
	{
		super(value);
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 1)
			throw new UnsupportedOperationException();
		return FSObjects.pack(this.value.apply(FSObjects.unpack(objects[0])));
	}
}

class FSFunc2 extends FSObject<BiFunction>
{
	FSFunc2(BiFunction value)
	{
		super(value);
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 2)
			throw new UnsupportedOperationException();
		return FSObjects.pack(this.value.apply(FSObjects.unpack(objects[0]), FSObjects.unpack(objects[1])));
	}
}

class FSSupplier extends FSObject<Supplier>
{
	FSSupplier(Supplier value)
	{
		super(value);
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 0)
			throw new UnsupportedOperationException();
		return FSObjects.pack(this.value.get());
	}
}
