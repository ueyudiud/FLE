package fle.core.cover;

import fle.api.cover.Cover;
import fle.api.cover.CoverRegistry;

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