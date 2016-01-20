package farcore.block;

public enum EnumRockState
{
	resource, smooth, cobble, crush;

	public String getUnlocalized()
	{
		return "state." + name();
	}
}