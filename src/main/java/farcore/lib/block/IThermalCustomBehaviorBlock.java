/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block;

import java.util.Random;

import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IThermalCustomBehaviorBlock
{
	/**
	 * Called when fire spread on this block.
	 * 
	 * @param coord the coordinate helper, please use implements
	 *            <tt>setBlockState()</tt> in <tt>coord</tt> instead to get
	 *            world and set.
	 * @param burnHardness a value from 0 to 1000.
	 * @param direction the direction of the fire come from.
	 * @return <code>true</code> to prevent classic fire behavior.
	 */
	boolean onBurn(IModifiableCoord coord, float burnHardness, Direction direction);
	
	/**
	 * Called on burning tick.
	 * 
	 * @param coord
	 * @param fireState The state of fire block.
	 * @return return true to prevent check whether the block will be removed.
	 */
	boolean onBurningTick(IModifiableCoord coord, Random rand, Direction fireSourceDir, IBlockState fireState);
	
	/**
	 * Called when checking whether the block can be burned, if prevent check on
	 * burning tick, this method will not called when burning ticking.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	default boolean canBeBurned(World world, BlockPos pos)
	{
		return true;
	}
	
	/**
	 * Get conduct heat speed. Negative value means this block is not a normal
	 * block (Such as ender door).
	 * 
	 * @param world
	 * @param state
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	double getThermalConduct(World world, BlockPos pos, IBlockState state);
	
	// double getHeatCapacity(World world, BlockPos pos, IBlockState state);
	
	/**
	 * Get encouragement of hardness of fire burning.<br>
	 * This method called each time update fire, suggest to let block waste some
	 * thing during encourage fire.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	int getFireEncouragement(World world, BlockPos pos);
	
	default boolean canFireBurnOn(World world, BlockPos pos, EnumFacing side, boolean isCatchRain)
	{
		return // ((Block) this).isFireSource(world, pos, side) ||
		((Block) this).isFlammable(world, pos, side) && !isCatchRain;
	}
	
	default void onHeatChanged(World world, BlockPos pos, Direction direction, double amount)
	{
		
	}
}
