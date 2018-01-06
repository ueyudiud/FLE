/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.Iterator;

/**
 * @author ueyudiud
 */
public abstract class WrapperFSObject implements IFSObject
{
	protected IFSObject object;
	
	protected WrapperFSObject()
	{
	}
	protected WrapperFSObject(IFSObject object)
	{
		this.object = object;
	}
	
	@Override
	public IFSObject eval()
	{
		this.object = object().eval();
		return this;
	}
	
	protected IFSObject object()
	{
		return this.object;
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		return object().apply(objects);
	}
	
	@Override
	public IFSObject update(IFSObject value, IFSObject...objects)
	{
		return object().update(value, objects);
	}
	
	@Override
	public IFSObject operator_ptr()
	{
		return object().operator_ptr();
	}
	
	@Override
	public IFSObject operator_positive()
	{
		return object().operator_positive();
	}
	
	@Override
	public IFSObject operator_negetive()
	{
		return object().operator_negetive();
	}
	
	@Override
	public IFSObject operator_invert()
	{
		return object().operator_invert();
	}
	
	@Override
	public IFSObject operator_not()
	{
		return object().operator_not();
	}
	
	@Override
	public IFSObject operator_lincr()
	{
		return object().operator_lincr();
	}
	
	@Override
	public IFSObject operator_rincr()
	{
		return object().operator_rincr();
	}
	
	@Override
	public IFSObject operator_ldesc()
	{
		return object().operator_ldesc();
	}
	
	@Override
	public IFSObject operator_rdesc()
	{
		return object().operator_rdesc();
	}
	
	@Override
	public IFSObject operator_add(IFSObject object)
	{
		return object().operator_add(object);
	}
	
	@Override
	public IFSObject operator_sub(IFSObject object)
	{
		return object().operator_sub(object);
	}
	
	@Override
	public IFSObject operator_mul(IFSObject object)
	{
		return object().operator_mul(object);
	}
	
	@Override
	public IFSObject operator_div(IFSObject object)
	{
		return object().operator_div(object);
	}
	
	@Override
	public IFSObject operator_rem(IFSObject object)
	{
		return object().operator_rem(object);
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		return object().operator_shl(object);
	}
	
	@Override
	public IFSObject operator_shr(IFSObject object)
	{
		return object().operator_shr(object);
	}
	
	@Override
	public IFSObject operator_ushr(IFSObject object)
	{
		return object().operator_ushr(object);
	}
	
	@Override
	public IFSObject operator_iand(IFSObject object)
	{
		return object().operator_iand(object);
	}
	
	@Override
	public IFSObject operator_ior(IFSObject object)
	{
		return object().operator_ior(object);
	}
	
	@Override
	public IFSObject operator_ixor(IFSObject object)
	{
		return object().operator_ixor(object);
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		return object().operator_objeq(object);
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		return object().operator_objne(object);
	}
	
	@Override
	public IFSObject operator_equal(IFSObject object)
	{
		return object().operator_equal(object);
	}
	
	@Override
	public IFSObject operator_noneq(IFSObject object)
	{
		return object().operator_noneq(object);
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		return object().operator_gt(object);
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		return object().operator_ge(object);
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		return object().operator_lt(object);
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		return object().operator_le(object);
	}
	
	@Override
	public IFSObject operator_array(IFSObject...objects)
	{
		return object().operator_array(objects);
	}
	
	@Override
	public void operator_delete(IFSObject object)
	{
		object().operator_delete(object);
	}
	
	@Override
	public IFSObject operator_range(IFSObject object)
	{
		return object().operator_range(object);
	}
	
	@Override
	public boolean isTrue()
	{
		return object().isTrue();
	}
	
	@Override
	public String asString()
	{
		return object().asString();
	}
	
	@Override
	public long asLong()
	{
		return object().asLong();
	}
	
	@Override
	public double asDouble()
	{
		return object().asDouble();
	}
	
	@Override
	public Iterator<?> asIterator()
	{
		return object().asIterator();
	}
}
