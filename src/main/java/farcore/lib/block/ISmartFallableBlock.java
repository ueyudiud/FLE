package farcore.lib.block;

import farcore.lib.entity.EntityFallingBlockExtended;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISmartFallableBlock extends IUpdateDelayBlock
{
	void onStartFalling(World world, BlockPos pos);
	
	boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state);
	
	boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT);
	
	boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT);
	
	/**
	 * Called when block fall on an entity.
	 * @param world
	 * @param block
	 * @param target
	 * @return The attack damage of block.
	 */
	float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target);
}