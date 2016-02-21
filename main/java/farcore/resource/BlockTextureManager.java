package farcore.resource;

import net.minecraftforge.common.util.ForgeDirection;
import farcore.util.U.N;
import flapi.util.BTI;
import flapi.util.FleValue;
import flapi.util.ITextureHandler;

public class BlockTextureManager implements ITextureHandler<BTI>
{
	String textureFileName;
	String[] textureNames;

	BlockTextureManager()
	{
		
	}
	public BlockTextureManager(String textureName) 
	{
		this(new String[]{textureName});
	}
	public BlockTextureManager(String[] textureName) 
	{
		this(FleValue.TEXTURE_FILE, textureName);
	}
	public BlockTextureManager(String fileName, String textureName) 
	{
		this(fileName, new String[]{textureName});
	}
	public BlockTextureManager(String fileName, String[] textureName) 
	{
		textureFileName = fileName;
		textureNames = textureName;
	}

	@Override
	public int getLocateSize()
	{
		return textureNames.length;
	}

	@Override
	public String getTextureFileName(int pass) 
	{
		return textureFileName;
	}

	@Override
	public String getTextureName(int pass) 
	{
		return textureNames[pass];
	}
	
	@Override
	public int getIconIndex(BTI infomation)
	{
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[infomation.side];
		switch(textureNames.length)
		{
		case 1 : return 0;
		case 2 : return dir == ForgeDirection.UP || dir == ForgeDirection.DOWN ? 0 : 1;
		case 3 : return dir == ForgeDirection.NORTH ? 0 : dir == ForgeDirection.UP || dir == ForgeDirection.DOWN ? 1 : 2;
		case 4 : return dir == ForgeDirection.NORTH ? 0 : dir == ForgeDirection.UP ? 1 : dir == ForgeDirection.DOWN ? 2 : 3;
		case 5 : return dir == ForgeDirection.NORTH ? 0 : dir == ForgeDirection.SOUTH ? 1 : dir == ForgeDirection.UP ? 2 : dir == ForgeDirection.DOWN ? 3 : 4;
		case 6 : return N.side(dir);
		case 7 : return N.side(dir);
		}
		return 0;
	}
}