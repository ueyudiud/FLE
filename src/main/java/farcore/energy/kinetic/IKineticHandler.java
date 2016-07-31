package farcore.energy.kinetic;

import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;

public interface IKineticHandler extends ICoord
{
	boolean canAccessKineticEnergyFromDirection(Direction direction);
	
	boolean isRotatable(Direction direction, double speed, double torque);
	
	void emitKineticEnergy(IKineticAccess access, Direction direction, double speed, double torque);

	double receiveKineticEnergy(IKineticAccess access, Direction direction, double speed, double torque);
	
	void onStuck(Direction direction, double speed, double torque);
	
	void kineticPreUpdate(IKineticAccess access);
}