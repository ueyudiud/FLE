package farcore.energy.thermal;

import farcore.lib.util.Direction;
import farcore.lib.world.IWorldSubContainer;
import net.minecraft.util.math.BlockPos;

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