package farcore.lib.block;

import java.util.Random;

import farcore.lib.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IThermalCustomBehaviorBlock
{
	/**
	 * Called when fire spread on this block.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param burnHardness A value from 0 to 1000.
	 * @param direction
	 * @return True to prevent classic fire behavior.
	 */
	boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction);
	
	/**
	 * Called on burning tick.
	 * @param world
	 * @param fireState The state of fire block.
	 * @param x
	 * @param y
	 * @param z
	 * @return return true to prevent check whether the block will
	 *  be removed.
	 */
	boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState);
	
	/**
	 * Called when checking whether the block can be burned,
	 * if prevent check on burning tick, this method will not called
	 * when burning ticking.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	default boolean canBeBurned(World world, BlockPos pos)
	{
		return true;
	}
	
	/**
	 * Get conduct heat speed. Negative value means this
	 * block is not a normal block (Such as ender door).
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	double getThermalConduct(World world, BlockPos pos);
	
	/**
	 * Get encouragement of hardness of fire burning.<br>
	 * This method called each time update fire,
	 * suggest to let block waste some thing during encourage
	 * fire.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	int getFireEncouragement(World world, BlockPos pos);
	
	default boolean canFireBurnOn(World world, BlockPos pos, EnumFacing side, boolean isCatchRain)
	{
		return ((Block) this).isFireSource(world, pos, side) ||
				((Block) this).isFlammable(world, pos, side) && !isCatchRain;
	}
	
	default void onHeatChanged(World world, BlockPos pos, Direction direction, double amount)
	{

	}
}