package nebula.common.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * You can not get position or world when getting
 * rain strength, is area enable snow, this interface
 * should extend by biome type.
 * @author ueyudiud
 *
 */
public interface IBiomeExtended
{
	/**
	 * Get the biome can rain at position, needn't
	 * check if position is block by solid above.
	 * @param world
	 * @param pos
	 * @return
	 */
	boolean canRainingAt(World world, BlockPos pos);
}