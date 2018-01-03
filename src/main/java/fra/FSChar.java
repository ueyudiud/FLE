/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
class FSChar extends FSInt
{
	FSChar(char value)
	{
		super(value);
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		FSStringBuf buf = new FSStringBuf(16);
		buf.builder.append((char) this.value);
		return buf.operator_shl(object);
	}
	
	@Override
	public String asString()
	{
		return Character.toString((char) this.value);
	}
}
