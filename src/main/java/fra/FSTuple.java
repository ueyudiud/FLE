/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import com.google.common.base.Joiner;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
class FSTuple implements IFSObject
{
	private static final Joiner JOINER = Joiner.on(", ");
	
	IFSObject[] elements;
	
	FSTuple(IFSObject[] elements)
	{
		this.elements = elements;
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		return object instanceof FSTuple ?
				FSObjects.valueOf(A.<IFSObject>and(this.elements, e->e.operator_objeq(object).isTrue())) :
					FSBool.FALSE;
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		return object instanceof FSTuple ?
				FSObjects.valueOf(!A.<IFSObject>and(this.elements, e->e.operator_objeq(object).isTrue())) :
					FSBool.FALSE;
	}
	
	@Override
	public IFSObject operator_equal(IFSObject object)
	{
		return object instanceof FSTuple ?
				FSObjects.valueOf(this.elements == ((FSTuple) object).elements) :
					FSBool.FALSE;
	}
	
	@Override
	public String asString()
	{
		return JOINER.appendTo(new StringBuilder().append('('), this.elements).append(')').toString();
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 1)
			throw new UnsupportedOperationException();
		int i = (int) objects[0].asLong();
		if (i >= this.elements.length || i < 0)
			throw new IndexOutOfBoundsException();
		return new WrapperFSObject()
		{
			@Override
			public IFSObject operator_set(IFSObject object)
			{
				return FSTuple.this.elements[i] = object;
			}
			
			@Override
			protected IFSObject object()
			{
				return FSTuple.this.elements[i];
			}
			
			@Override
			public IFSObject eval()
			{
				return FSTuple.this.elements[i];
			}
		};
	}
	
	@Override
	public IFSObject update(IFSObject value, IFSObject...objects)
	{
		if (objects.length != 1)
			throw new UnsupportedOperationException();
		int i = (int) objects[0].asLong();
		if (i >= this.elements.length || i < 0)
			throw new IndexOutOfBoundsException();
		return this.elements[i] = value;
	}
	
	@Override
	public IFSObject operator_set(IFSObject object)
	{
		if (object instanceof FSTuple)
		{
			FSTuple tuple = (FSTuple) object;
			int i = 0;
			final int l1 = this.elements.length;
			final int l2 = Math.min(l1, tuple.elements.length);
			while (i < l2)
			{
				this.elements[i].operator_set(tuple.elements[i]);
			}
			while (i < l1)
			{
				this.elements[i].operator_set(null);
			}
			return this;
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}
}

