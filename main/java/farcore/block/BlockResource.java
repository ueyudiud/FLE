package farcore.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import farcore.block.item.ItemBlockBase;
import farcore.util.Direction;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockResource extends BlockBase
{
	public BlockResource(String unlocalized,
			Class<? extends ItemBlockBase> clazz, Material materialIn)
	{
		super(unlocalized, clazz, materialIn);
		this.fullBlock = true;
		setLightOpacity(0);
	}
	
	public BlockResource(String unlocalized, Material materialIn)
	{
		super(unlocalized, materialIn);
		setLightOpacity(255);
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
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos,
			EnumFacing side)
	{
		return true;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube()
	{
		return false;
	}
	
	@Override
	public boolean isSideSolide(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		return true;
	}
}