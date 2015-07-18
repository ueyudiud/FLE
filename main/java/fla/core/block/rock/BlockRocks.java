package fla.core.block.rock;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.world.BlockPos;
import fla.core.block.BlockBaseRock;

public class BlockRocks extends BlockBaseRock
{
	protected BlockRocks()
	{
		super(Material.rock);
	}

	@Override
	protected int getMaxDamage() 
	{
		return 0;
	}

	@Override
	public int getRenderType() 
	{
		return 0;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z,
			Explosion explosion) 
	{
		
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return blockIcon;
	}
}