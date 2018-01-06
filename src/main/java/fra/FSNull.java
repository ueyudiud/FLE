/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
enum FSNull implements IFSObject
{
	NULL;
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		return FSObjects.valueOf(this == object);
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		return FSObjects.valueOf(this != object);
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		return new FSStringBuf("null").operator_shl(object);
	}
	
	@Override
	public String asString()
	{
		return "null";
	}
}
