package fla.core.block;

import fla.api.world.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockBaseSub extends BlockBase
{
	protected IIcon[] icons = new IIcon[16];
	
	protected BlockBaseSub(Material material)
	{
		super(material);
	}
	
	protected abstract int getMaxDamage();

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return getIcon(pos.getBlockMeta(), side);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float xPos, float yPos, float zPos, int meta) 
	{
		return meta;
	}

	@Override
	public boolean hasSubs() 
	{
		return true;
	}
}
