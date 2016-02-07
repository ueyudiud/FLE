package fle.core.enums;

import farcore.util.IUnlocalized;

public enum EnumDirtCover implements IUnlocalized
{
	nothing, water, snow;
	
	public String getUnlocalized()
	{
		return "state.cover." + name();
	}
}