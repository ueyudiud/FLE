package farcore.block;

import farcore.resource.BlockTextureManager;
import flapi.util.BTI;
import flapi.util.BlockTextureHandler;
import flapi.util.FleValue;
import flapi.util.ITextureHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockFleTexture4 extends BlockFleBehavior3
{
	protected BlockTextureHandler iconHandler;
	
	protected BlockFleTexture4(String unlocalized, Material material)
	{
		super(unlocalized, material);
	}
	protected BlockFleTexture4(Class<? extends ItemBlock> clazz, String unlocalized, Material material)
	{
		super(clazz, unlocalized, material);
	}
	
	protected boolean useClassicIconHandler()
	{
		return false;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		if(useClassicIconHandler())	super.registerBlockIcons(register);
		else
		{
			try
			{
				iconHandler.registerIcon(register);
			}
			catch(Throwable throwable)
			{
				blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":void");
			}
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		try
		{
			return useClassicIconHandler() ? blockIcon :
				iconHandler.getIcon(this, world, x, y, z, side);
		}
		catch(Throwable throwable)
		{
			return blockIcon;
		}
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		try
		{
			return useClassicIconHandler() ? blockIcon :
				iconHandler.getIcon(this, meta, side);
		}
		catch(Throwable throwable)
		{
			return blockIcon;
		}
	}
}