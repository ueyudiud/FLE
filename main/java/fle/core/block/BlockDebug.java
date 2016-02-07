package fle.core.block;

import flapi.block.BlockFle;
import flapi.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockDebug extends BlockFle
{
	public BlockDebug()
	{
		super("block.debug", Material.rock);
		setBlockUnbreakable();
		setResistance(Float.MAX_VALUE);
		setLightLevel(1F);
		maxSize = 1;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		Values.EMPTY_BLOCK_ICON = register.registerIcon(Values.TEXTURE_FILE + ":void");
	}
	
	@Override
	public IIcon getBlockIcon(int side, int meta)
	{
		return Values.EMPTY_BLOCK_ICON;
	}
}