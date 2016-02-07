package farcore.block;

import farcore.util.IUnlocalized;

public enum EnumRockState implements IUnlocalized
{
	resource, smooth, cobble, crush;
	
	public String getUnlocalized()
	{
		return "state.rock." + name();
	}
}