/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSBoolLazyCompute extends WrapperFSObject
{
	IFSObject lazy;
	
	protected FSBoolLazyCompute(IFSObject object)
	{
		super(FSBool.TRUE);
		this.lazy = object;
	}
	
	@Override
	public IFSObject eval()
	{
		return this.object;//FSBool.TRUE
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		return this.lazy.operator_gt(object);
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		return this.lazy.operator_ge(object);
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		return this.lazy.operator_lt(object);
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		return this.lazy.operator_le(object);
	}
}

class FSFailedCompute extends WrapperFSObject
{
	static final FSFailedCompute INSTANCE = new FSFailedCompute();
	
	private FSFailedCompute()
	{
		super(FSBool.FALSE);
	}
	
	@Override
	public IFSObject eval()
	{
		return this.object;//FSBool.TRUE
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		return this;
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		return this;
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		return this;
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		return this;
	}
}
