/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.tile.kinetic;

import javax.annotation.Nullable;

import farcore.energy.kinetic.IKineticHandler;
import nebula.common.util.A;
import nebula.common.util.Direction;
import net.minecraft.util.EnumFacing.Axis;

/**
 * @author ueyudiud
 */
public interface IAxisHandler extends IKineticHandler
{
	@Override
	default boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return A.or(getAxisHandlerAxis(), axis -> direction.axis == axis);
	}
	
	@Nullable
	Axis[] getAxisHandlerAxis();
}
