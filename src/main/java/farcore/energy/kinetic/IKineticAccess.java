package farcore.energy.kinetic;

import farcore.lib.util.Direction;

/**
 * The kinetic energy handler interface.
 * @author ueyudiud
 *
 */
public interface IKineticAccess
{
	void sendEnergyTo(Direction direction, double speed, double torque);
}