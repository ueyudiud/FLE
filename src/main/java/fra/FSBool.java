/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
enum FSBool implements IFSObject
{
	TRUE(true),
	FALSE(false);
	
	final boolean value;
	
	FSBool(boolean value)
	{
		this.value = value;
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		return FSObjects.valueOf(object == this ||
				Boolean.toString(this.value).equals(((FSString) object).value));
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		return FSObjects.valueOf(object != this &&
				!Boolean.toString(this.value).equals(((FSString) object).value));
	}
	
	@Override
	public IFSObject operator_not()
	{
		return this.value ? FALSE : TRUE;
	}
	
	@Override
	public String asString()
	{
		return Boolean.toString(this.value);
	}
	
	@Override
	public boolean isTrue()
	{
		return this.value;
	}
}
