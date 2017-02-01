package farcore.energy.thermal;

import farcore.lib.world.IWorldSubContainer;
import nebula.common.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * The thermal handler box.
 * For a structure in local world, used by greenhouse, blast furnace nearby, etc.
 * For an area temperature control.
 * @author ueyudiud
 *
 */
public interface IThermalHandlerBox extends IWorldSubContainer
{
	float getTemperature(BlockPos pos);
	
	/**
	 * Called when heat change.
	 * @param source
	 * @param target
	 * @param sourceDir
	 * @param value
	 * @return Return true to prevent vanilla heat check.
	 */
	boolean onHeatChange(BlockPos source, BlockPos target, Direction sourceDir, double value);
}