/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.blocks;

import farcore.data.Materials;
import fle.core.FLE;
import nebula.base.IRegister;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

/**
 * @author ueyudiud
 */
public class BlockRockyMiscMachine extends BlockTE
{
	public BlockRockyMiscMachine()
	{
		super(FLE.MODID, "rocky.misc.machine", Materials.ROCK);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
