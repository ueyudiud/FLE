package fle.core.util;

import flapi.util.Values;

@Deprecated
public class TextureLocation
{
	private String fileName;
	private String textureName[];

	public TextureLocation(String aTextureName) 
	{
		this(Values.TEXTURE_FILE, aTextureName);
	}
	public TextureLocation(String aFileName, String aTextureName) 
	{
		this(aFileName, new String[]{aTextureName});
	}
	public TextureLocation(String...aTextureNames) 
	{
		this(Values.TEXTURE_FILE, aTextureNames);
	}
	public TextureLocation(String aFileName, String[] aTextureNames)
	{
		fileName = aFileName;
		textureName = aTextureNames;
	}

	public int getLocateSize() 
	{
		return textureName.length;
	}

	public String getTextureFileName(int pass) 
	{
		return fileName;
	}
	
	public String getTextureName(int pass) 
	{
		return textureName[pass];
	}
}