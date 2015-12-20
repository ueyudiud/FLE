package fle.core.block.machine;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.block.interfaces.IBlockWithTileBehaviour;
import flapi.collection.Register;
import flapi.util.BlockTextureHandler;
import fle.core.block.BlockSubTile;
import fle.core.block.ItemSubTile;

public class ItemClayInventory extends ItemSubTile
{
	public ItemClayInventory(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 1;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		Register<IBlockWithTileBehaviour<BlockSubTile>> register1 = ((BlockSubTile) block).getRegister();
		for(IBlockWithTileBehaviour<BlockSubTile>  tBehavior : register1)
		{
			String name = register1.name(tBehavior);
			BlockTextureHandler locate = ((BlockSubTile) block).getTextureHandler(name);
			locate.registerIcon(register);
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return ((BlockSubTile) block).getTextureHandler(((BlockSubTile) block).getRegister().name(meta)).getIcon(block, meta, 3);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return false;
	}
}