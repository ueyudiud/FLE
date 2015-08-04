package fle.core.util;

import fle.api.FleValue;
import fle.api.util.ITextureLocation;

public class TextureLocation implements ITextureLocation
{
	private String fileName;
	private String textureName[];

	public TextureLocation(String aTextureName) 
	{
		this(FleValue.TEXTURE_FILE, aTextureName);
	}
	public TextureLocation(String aFileName, String aTextureName) 
	{
		this(aFileName, new String[]{aTextureName});
	}
	public TextureLocation(String...aTextureNames) 
	{
		this(FleValue.TEXTURE_FILE, aTextureNames);
	}
	public TextureLocation(String aFileName, String[] aTextureNames)
	{
		fileName = aFileName;
		textureName = aTextureNames;
	}

	@Override
	public int getLocateSize() 
	{
		return textureName.length;
	}

	@Override
	public String getTextureFileName(int pass) 
	{
		return fileName;
	}

	@Override
	public String getTextureName(int pass) 
	{
		return textureName[pass];
	}
}