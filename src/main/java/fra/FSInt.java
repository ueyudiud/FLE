/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSInt implements IFSObject, Cloneable
{
	long value;
	
	FSInt(long value)
	{
		this.value = value;
	}
	
	@Override
	protected FSInt clone()
	{
		try
		{
			return (FSInt) super.clone();
		}
		catch (CloneNotSupportedException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	@Override
	public IFSObject operator_invert()
	{
		return new FSInt(~this.value);
	}
	
	@Override
	public IFSObject operator_positive()
	{
		return this;
	}
	
	@Override
	public IFSObject operator_negetive()
	{
		return new FSInt(-this.value);
	}
	
	@Override
	public IFSObject operator_add(IFSObject object)
	{
		try
		{
			return new FSInt(this.value + object.asLong());
		}
		catch (UnsupportedOperationException exception)
		{
			return new FSFloat(this.value + object.asDouble());
		}
	}
	
	@Override
	public IFSObject operator_sub(IFSObject object)
	{
		try
		{
			return new FSInt(this.value - object.asLong());
		}
		catch (UnsupportedOperationException exception)
		{
			return new FSFloat(this.value - object.asDouble());
		}
	}
	
	@Override
	public IFSObject operator_mul(IFSObject object)
	{
		try
		{
			return new FSInt(this.value * object.asLong());
		}
		catch (UnsupportedOperationException exception)
		{
			return new FSFloat(this.value * object.asDouble());
		}
	}
	
	@Override
	public IFSObject operator_div(IFSObject object)
	{
		try
		{
			return new FSInt(this.value / object.asLong());
		}
		catch (UnsupportedOperationException exception)
		{
			return new FSFloat(this.value / object.asDouble());
		}
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		try
		{
			return new FSInt(this.value << object.asLong());
		}
		catch (UnsupportedOperationException exception)
		{
			return new FSStringBuf(asString()).operator_shl(object);
		}
	}
	
	@Override
	public IFSObject operator_shr(IFSObject object)
	{
		return new FSInt(this.value >> object.asLong());
	}
	
	@Override
	public IFSObject operator_ushr(IFSObject object)
	{
		return new FSInt(this.value >>> object.asLong());
	}
	
	@Override
	public IFSObject operator_iand(IFSObject object)
	{
		return new FSInt(this.value & object.asLong());
	}
	
	@Override
	public IFSObject operator_ior(IFSObject object)
	{
		return new FSInt(this.value | object.asLong());
	}
	
	@Override
	public IFSObject operator_ixor(IFSObject object)
	{
		return new FSInt(this.value ^ object.asLong());
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOf(this.value == ((FSInt) object).value);
		}
		else if (object instanceof FSFloat)
		{
			return FSObjects.valueOf(this.value == object.asDouble());
		}
		else
		{
			return FSObjects.valueOf(Long.toString(this.value).equals(object.asString()));
		}
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOf(this.value != ((FSInt) object).value);
		}
		else if (object instanceof FSFloat)
		{
			return FSObjects.valueOf(this.value != object.asDouble());
		}
		else
		{
			return FSObjects.valueOf(!Long.toString(this.value).equals(object.asString()));
		}
	}
	
	@Override
	public IFSObject operator_equal(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value == ((FSInt) object).value, object);
		}
		else
		{
			return FSObjects.valueOfCompare(this.value == object.asDouble(), object);
		}
	}
	
	@Override
	public IFSObject operator_noneq(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value != ((FSInt) object).value, object);
		}
		else
		{
			return FSObjects.valueOfCompare(this.value != object.asDouble(), object);
		}
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value > ((FSInt) object).value, object);
		}
		else try
		{
			return FSObjects.valueOfCompare(this.value > object.asDouble(), object);
		}
		catch (UnsupportedOperationException exception)
		{
			return FSFailedCompute.INSTANCE;
		}
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value >= ((FSInt) object).value, object);
		}
		else try
		{
			return FSObjects.valueOfCompare(this.value >= object.asDouble(), object);
		}
		catch (UnsupportedOperationException exception)
		{
			return FSFailedCompute.INSTANCE;
		}
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value < ((FSInt) object).value, object);
		}
		else try
		{
			return FSObjects.valueOfCompare(this.value < object.asDouble(), object);
		}
		catch (UnsupportedOperationException exception)
		{
			return FSFailedCompute.INSTANCE;
		}
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		if (object instanceof FSInt)
		{
			return FSObjects.valueOfCompare(this.value <= ((FSInt) object).value, object);
		}
		else try
		{
			return FSObjects.valueOfCompare(this.value <= object.asDouble(), object);
		}
		catch (UnsupportedOperationException exception)
		{
			return FSFailedCompute.INSTANCE;
		}
	}
	
	@Override
	public String asString()
	{
		return Long.toString(this.value);
	}
	
	@Override
	public double asDouble()
	{
		return this.value;
	}
	
	@Override
	public long asLong()
	{
		return this.value;
	}
	
	@Override
	public boolean isTrue()
	{
		return this.value != 0;
	}
}
