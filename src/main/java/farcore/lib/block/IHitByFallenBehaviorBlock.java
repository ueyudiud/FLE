package farcore.lib.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHitByFallenBehaviorBlock
{
	/**
	 * Get should block fall down from this block, return true to ignore stay checking.
	 * @param world
	 * @param pos
	 * @param state
	 * @param fallen
	 * @param tileNBT
	 * @return
	 */
	boolean isPermeatableBy(World world, BlockPos pos, IBlockState state, IBlockState fallen, NBTTagCompound tileNBT);
	
	/**
	 * Called when block fall on, if return true, the falling block will try to replace block at this position.
	 * @param world
	 * @param pos
	 * @param state
	 * @param fallen
	 * @param tileNBT
	 * @return
	 */
	boolean onFallingOn(World world, BlockPos pos, IBlockState state, IBlockState fallen, NBTTagCompound tileNBT, int height);
}