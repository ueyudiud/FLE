package fle.core.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import fle.api.FleValue;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.util.IBlockTextureManager;
import fle.api.util.Register;
import fle.api.world.BlockPos;
import fle.core.block.BlockSubTile;
import fle.core.block.behaviour.BehaviourClay;
import fle.core.te.argil.TileEntityArgilUnsmelted;
import fle.core.util.BlockTextureManager;

public class BlockClayInventory extends BlockSubTile
{
	public BlockClayInventory init()
	{
		registerSub(0, "terrine_unsmelted", new BlockTextureManager("clay/1"), new BehaviourClay());
		return this;
	}
	
	public final void registerSub(int index, String aName,
			IBlockWithTileBehaviour<BlockSubTile> blockBehavior)
	{
		registerSub(index, aName, null, blockBehavior);
	}
	
	public BlockClayInventory(String aName)
	{
		super(ItemClayInventory.class, aName, Material.clay);
		setResistance(1.0F);
		setHardness(1.0F);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/argil");
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return blockIcon;
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return blockIcon;
	}
	
	@Override
	public IIcon getIcon(int aPass, BlockPos aPos, int aSide)
	{
		return blockIcon;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_NOINV_RENDER_ID;
	}
}