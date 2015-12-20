package fle.core.cover;

import flapi.cover.Cover;
import flapi.cover.CoverRegistry;

public class CoverBase extends Cover
{
	public CoverBase(String unlocalized)
	{
		super(unlocalized);
	}
	
	public CoverBase register()
	{
		CoverRegistry.registerCover(this, getUnlocalizedName());
		return this;
	}
}