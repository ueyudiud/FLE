package farcore.lib.block.instance;

import farcore.FarCore;
import farcore.lib.block.BlockBase;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.material.Mat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDust extends BlockBase implements ISmartFallableBlock
{
	public BlockDust(Mat material)
	{
		super(FarCore.ID, "dust." + material.name, Material.CLAY);
	}

	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
		
	}

	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}

	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return 0;
	}
}