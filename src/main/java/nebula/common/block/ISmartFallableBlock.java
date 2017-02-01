package nebula.common.block;

import nebula.common.entity.EntityFallingBlockExtended;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISmartFallableBlock extends IUpdateDelayBlock
{
	default void onStartFalling(World world, BlockPos pos) {}
	
	boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state);
	
	default void moveEntity(World world, EntityFallingBlockExtended entity)
	{
		//Gravity
		if (!entity.hasNoGravity())
		{
			entity.motionY -= 0.04;
		}
		//Apply Motivation
		entity.move(entity.motionX, entity.motionY, entity.motionZ);
		//Air Resistance
		entity.motionX *= 0.98;
		entity.motionY *= 0.98;
		entity.motionZ *= 0.98;
	}
	
	default boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	default boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	/**
	 * Called when block fall on an entity.
	 * @param world
	 * @param block
	 * @param target
	 * @return The attack damage of block.
	 */
	float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target);
}