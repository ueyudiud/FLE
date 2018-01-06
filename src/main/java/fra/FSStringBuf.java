/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSStringBuf extends WrapperFSObject
{
	StringBuilder builder;
	
	FSStringBuf(int length)
	{
		this.builder = new StringBuilder(length);
	}
	
	FSStringBuf(String source)
	{
		this.builder = new StringBuilder(source);
	}
	
	@Override
	protected IFSObject object()
	{
		return eval();
	}
	
	@Override
	public IFSObject eval()
	{
		return new FSString(this.builder.toString());
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		this.builder.append(object.asString());
		return this;
	}
	
	@Override
	public String asString()
	{
		return this.builder.toString();
	}
}
