/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.Iterator;

/**
 * @author ueyudiud
 */
public interface IFSObject
{
	default IFSObject eval()
	{
		return this;
	}
	
	default IFSObject child(String name)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ptr()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_not()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_positive()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_negetive()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_invert()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_add(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_sub(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_mul(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_div(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_rem(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_shl(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_shr(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ushr(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_iand(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ior(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ixor(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_range(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_lincr()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_rincr()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ldesc()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_rdesc()
	{
		throw new UnsupportedOperationException();
	}
	
	IFSObject operator_objeq(IFSObject object);
	
	default IFSObject operator_objne(IFSObject object)
	{
		return FSObjects.valueOf(!operator_objeq(object).isTrue());
	}
	
	default IFSObject operator_equal(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_noneq(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_gt(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_ge(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_lt(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_le(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_set(IFSObject object)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_addset(IFSObject object)
	{
		IFSObject result = operator_add(object);
		return operator_set(result);
	}
	
	default IFSObject operator_subset(IFSObject object)
	{
		IFSObject result = operator_sub(object);
		return operator_set(result);
	}
	
	default IFSObject operator_mulset(IFSObject object)
	{
		IFSObject result = operator_mul(object);
		return operator_set(result);
	}
	
	default IFSObject operator_divset(IFSObject object)
	{
		IFSObject result = operator_div(object);
		return operator_set(result);
	}
	
	default IFSObject operator_remset(IFSObject object)
	{
		IFSObject result = operator_rem(object);
		return operator_set(result);
	}
	
	default IFSObject operator_shlset(IFSObject object)
	{
		IFSObject result = operator_shl(object);
		return operator_set(result);
	}
	
	default IFSObject operator_shrset(IFSObject object)
	{
		IFSObject result = operator_shr(object);
		return operator_set(result);
	}
	
	default IFSObject operator_ushrset(IFSObject object)
	{
		IFSObject result = operator_ushr(object);
		return operator_set(result);
	}
	
	default IFSObject operator_andset(IFSObject object)
	{
		IFSObject result = operator_iand(object);
		return operator_set(result);
	}
	
	default IFSObject operator_orset(IFSObject object)
	{
		IFSObject result = operator_ior(object);
		return operator_set(result);
	}
	
	default IFSObject operator_xorset(IFSObject object)
	{
		IFSObject result = operator_ixor(object);
		return operator_set(result);
	}
	
	default void operator_delete(IFSObject objects)
	{
		throw new UnsupportedOperationException();
	}
	
	default boolean isTrue()
	{
		throw new UnsupportedOperationException();
	}
	
	default long asLong()
	{
		throw new UnsupportedOperationException();
	}
	
	default double asDouble()
	{
		throw new UnsupportedOperationException();
	}
	
	String asString();
	
	default Iterator<?> asIterator()
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject apply(IFSObject...objects)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject update(IFSObject value, IFSObject...objects)
	{
		throw new UnsupportedOperationException();
	}
	
	default IFSObject operator_array(IFSObject...objects)
	{
		throw new UnsupportedOperationException();
	}
}
