package farcore.block;

public enum EnumDirtCover
{
	nothing, water, snow;

	public String getCoverName()
	{
		return "state.cover." + name();
	}
}