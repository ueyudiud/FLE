package farcore.lib.block;

import java.util.Random;

import farcore.lib.util.Direction;
import net.minecraft.world.World;

public interface IBurnCustomBehaviorBlock
{
	/**
	 * Called when fire spread on this block.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param burnHardness
	 * @param direction
	 * @return True to prevent classic fire behavior.
	 */
	boolean onBurn(World world, int x, int y, int z, float burnHardness, Direction direction);

	/**
	 * Called on burning tick.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return return true to prevent check whether the block will
	 *  be removed.
	 */
	boolean onBurningTick(World world, int x, int y, int z, Random rand, Direction fireSourceDir);

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
	default boolean canBeBurned(World world, int x, int y, int z)
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
	float getThermalConduct(World world, int x, int y, int z);

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
	int getFireEncouragement(World world, int x, int y, int z);
}