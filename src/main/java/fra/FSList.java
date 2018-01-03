/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
class FSList extends FSObject<List<?>>
{
	FSList(List<?> list)
	{
		super(list);
	}
	
	@Override
	public IFSObject operator_addset(IFSObject object)
	{
		return FSObjects.valueOf(this.value.add(L.castAny(FSObjects.unpack(object))));
	}
	
	@Override
	public IFSObject operator_subset(IFSObject object)
	{
		return FSObjects.valueOf(this.value.remove(L.castAny(FSObjects.unpack(object))));
	}
	
	@Override
	public IFSObject operator_add(IFSObject object)
	{
		if (object instanceof FSList)
		{
			List list = new ArrayList<>();
			list.addAll(this.value);
			list.addAll(((FSList) object).value);
			return new FSList(list);
		}
		return super.operator_add(object);
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 1)
			throw new UnsupportedOperationException();
		int i = (int) objects[0].asLong();
		if (i >= this.value.size() || i < 0)
			throw new IndexOutOfBoundsException();
		return new WrapperFSObject()
		{
			@Override
			public IFSObject operator_set(IFSObject object)
			{
				return FSObjects.pack(FSList.this.value.set(i, L.castAny(FSObjects.unpack(object))));
			}
			
			@Override
			protected IFSObject object()
			{
				return FSObjects.pack(FSList.this.value.get(i));
			}
			
			@Override
			public IFSObject eval()
			{
				return object();
			}
		};
	}
	
	@Override
	public Iterator<?> asIterator()
	{
		return this.value.iterator();
	}
}
