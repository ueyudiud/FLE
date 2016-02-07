package farcore.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import farcore.block.item.ItemBlockBase;
import farcore.util.Direction;

public class BlockResource extends BlockBase
{
	public BlockResource(String unlocalized,
			Class<? extends ItemBlockBase> clazz, Material materialIn)
	{
		super(unlocalized, clazz, materialIn);
	}
	
	public BlockResource(String unlocalized, Material materialIn)
	{
		super(unlocalized, materialIn);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		return false;
	}
	
	@Override
	public boolean canProvidePower()
	{
		return false;
	}
}