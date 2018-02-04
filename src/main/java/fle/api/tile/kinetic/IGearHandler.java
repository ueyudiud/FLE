/*
 * copyright© 2016-2018 ueyudiud
 */

package fle.api.tile.kinetic;

import javax.annotation.Nullable;

import farcore.energy.kinetic.IKineticHandler;
import nebula.common.util.Direction;
import net.minecraft.util.EnumFacing.Axis;

/**
 * @author ueyudiud
 */
public interface IGearHandler extends IKineticHandler
{
	@Nullable
	Axis getGearAxis(Direction direction);
	
	float getGearSize(Direction direction);
	
	float getGearTeethSize(Direction direction);
	
	int getGearTeethCount(Direction direction);
}
