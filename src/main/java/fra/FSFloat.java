/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSFloat implements IFSObject
{
	double value;
	
	FSFloat(double value)
	{
		this.value = value;
	}
	
	@Override
	protected FSFloat clone()
	{
		try
		{
			return (FSFloat) super.clone();
		}
		catch (CloneNotSupportedException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	@Override
	public IFSObject operator_positive()
	{
		return this;
	}
	
	@Override
	public IFSObject operator_negetive()
	{
		return new FSFloat(-this.value);
	}
	
	@Override
	public IFSObject operator_add(IFSObject object)
	{
		return new FSFloat(this.value + object.asDouble());
	}
	
	@Override
	public IFSObject operator_sub(IFSObject object)
	{
		return new FSFloat(this.value - object.asDouble());
	}
	
	@Override
	public IFSObject operator_mul(IFSObject object)
	{
		return new FSFloat(this.value * object.asDouble());
	}
	
	@Override
	public IFSObject operator_div(IFSObject object)
	{
		return new FSFloat(this.value / object.asDouble());
	}
	
	@Override
	public IFSObject operator_rem(IFSObject object)
	{
		return new FSFloat(this.value % object.asDouble());
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		return new FSStringBuf(asString()).operator_shl(object);
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
			return FSObjects.valueOf(Double.toString(this.value).equals(object.asString()));
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
			return FSObjects.valueOf(!Double.toString(this.value).equals(object.asString()));
		}
	}
	
	@Override
	public IFSObject operator_equal(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value == object.asDouble(), object);
	}
	
	@Override
	public IFSObject operator_noneq(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value != object.asDouble(), object);
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value > object.asDouble(), object);
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value >= object.asDouble(), object);
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value < object.asDouble(), object);
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		return FSObjects.valueOfCompare(this.value <= object.asDouble(), object);
	}
	
	@Override
	public String asString()
	{
		return Double.toString(this.value);
	}
}
