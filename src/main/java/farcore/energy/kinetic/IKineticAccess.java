package farcore.energy.kinetic;

import farcore.lib.util.Direction;

public interface IKineticAccess
{
	void sendEnergyTo(Direction direction, double speed, double torque);
}