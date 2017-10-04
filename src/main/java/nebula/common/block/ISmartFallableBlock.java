/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import nebula.common.entity.EntityFallingBlockExtended;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The custom falling block event handler.<p>
 * Use to handle the <tt>falling</tt> event which falling block
 * contains the block implements this interface.<p>
 * Here are the following events can be handled:
 * <li><tt>start_falling</tt> - called when block start falling.
 * <li><tt>checking_stay</tt> - called when block checking for place to stay.
 * <li><tt>fall_on_ground</tt> - called when block try falling.
 * <li><tt>drop_as_item</tt>
 * <li><tt>fall_on_entity</tt>
 * </li><p>
 * For some method you can't get EntityFallingBlockExtended for that
 * the block falling event will still be handled when block are out
 * of updating range, and the static falling handler instead.
 * @author ueyudiud
 * @see nebula.common.entity.EntityFallingBlockExtended
 */
public interface ISmartFallableBlock extends IUpdateDelayBlock
{
	/**
	 * Called when entity spawning in world, and block are already
	 * started falling (the source block is already removed from world).
	 * @param world the world.
	 * @param pos the position which falling block start at.
	 */
	default void onStartFalling(World world, BlockPos pos) {}
	
	/**
	 * Check can falling block stay at checking position.<p>
	 * Return <code>true</code> the block will began start <tt>stay</tt>
	 * action at this position in world.
	 * @param world the world.
	 * @param pos the position.
	 * @param state the falling block state.
	 * @return <code>true</code> if block can stay at position, and <code>false</code>
	 *         for otherwise.
	 */
	boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state);
	
	/**
	 * Move falling block entity in world.
	 * @param world the world.
	 * @param entity the entity.
	 */
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
	
	/**
	 * Called when <tt>stay</tt> action start.<p>
	 * The block will try to fallen on the ground, you may use this method to
	 * canceled the vanilla falling block replace <tt>behavior</tt> and use
	 * your custom replace behavior instead. If do it, return <code>true</code>
	 * to cancel vanilla <tt>replace</tt> behavior.
	 * @param world the world.
	 * @param pos the position the entity fallen at.
	 * @param state the block state.
	 * @param height the height the entity block fallen from world.
	 * @param tileNBT the cached tile NBT, which loaded from source tile before falling
	 *                block fallen.
	 * @return return <code>true</code> to cancel vanilla <tt>replace</tt> behavior,
	 *         or <code>false</code> to handle by vanilla <tt>replace</tt> behavior.
	 */
	default boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	/**
	 * Called when block failed to ground (Such as a entity fill in the place).
	 * @param world
	 * @param pos
	 * @param state
	 * @param tileNBT
	 * @return
	 */
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