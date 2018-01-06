/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSString extends FSObject<String>
{
	FSString(String value)
	{
		super(value);
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOf(this.value.equals(Long.toString(((FSInt) object).value)));
		}
		else if (object instanceof FSFloat)
		{
			return FSObjects.valueOf(this.value.equals(Double.toString(((FSFloat) object).value)));
		}
		else if (object instanceof FSString)
		{
			return FSObjects.valueOf(this.value.equals(((FSString) object).value));
		}
		else if (object instanceof FSStringBuf)
		{
			return FSObjects.valueOf(this.value.equals(((FSStringBuf) object).builder.toString()));
		}
		else
		{
			return FSBool.FALSE;
		}
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOf(!this.value.equals(Long.toString(((FSInt) object).value)));
		}
		else if (object instanceof FSFloat)
		{
			return FSObjects.valueOf(!this.value.equals(Double.toString(((FSFloat) object).value)));
		}
		else if (object instanceof FSString)
		{
			return FSObjects.valueOf(!this.value.equals(((FSString) object).value));
		}
		else if (object instanceof FSStringBuf)
		{
			return FSObjects.valueOf(!this.value.equals(((FSStringBuf) object).builder.toString()));
		}
		else
		{
			return FSBool.TRUE;
		}
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		return new FSStringBuf(this.value).operator_shl(object);
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value.compareTo(object.asString()) > 0, object);
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value.compareTo(object.asString()) >= 0, object);
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value.compareTo(object.asString()) < 0, object);
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value.compareTo(object.asString()) <= 0, object);
	}
	
	@Override
	public String asString()
	{
		return this.value;
	}
}
