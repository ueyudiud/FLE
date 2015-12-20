package fle.core.util;

import flapi.util.ITI;
import flapi.util.ITextureHandler;

public class ItemTextureHandler extends TextureLocation implements ITextureHandler<ITI>
{
	public ItemTextureHandler(String aTextureName) 
	{
		super(aTextureName);
	}
	public ItemTextureHandler(String aFileName, String aTextureName) 
	{
		super(aFileName, aTextureName);
	}
	public ItemTextureHandler(String...aTextureNames) 
	{
		super(aTextureNames);
	}
	public ItemTextureHandler(String aFileName, String[] aTextureNames)
	{
		super(aFileName, aTextureNames);
	}
	
	@Override
	public int getIconIndex(ITI infomation)
	{
		return infomation.pass;
	}
}