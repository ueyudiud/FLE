package flapi.block;

import farcore.block.BlockFleWithMetadata;
import farcore.block.TEBase;
import farcore.util.U;
import flapi.util.FleValue;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMachine extends BlockFleWithMetadata
{
	protected BlockMachine(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	protected BlockMachine(Class<? extends ItemBlock> clazz,
			String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}

	@Override
	public void onBlockPlacedBy(World world, int x,
			int y, int z, EntityLivingBase entity,
			ItemStack stack)
	{
		if(!world.isRemote)
		{
			if(world.getTileEntity(x, y, z) instanceof TEBase)
			{
				((TEBase) world.getTileEntity(x, y, z)).setDirction(U.W.initFacing(entity));
			}
		}
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side)
	{
		return super.canConnectRedstone(world, x, y, z, side);
	}
	
	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y, int z)
	{
		return FleValue.MACHINE_ROTATION;
	}
	
	@Override
	public boolean rotateBlock(World world, int x, int y, int z,
			ForgeDirection axis)
	{
		if(axis != ForgeDirection.UNKNOWN && axis != ForgeDirection.UP &&
				axis != ForgeDirection.DOWN)
		{
			a(world, x, y, z).setDirction(axis);
			return true;
		}
		return false;
	}
}