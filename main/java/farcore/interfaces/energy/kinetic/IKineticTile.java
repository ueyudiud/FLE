package farcore.interfaces.energy.kinetic;

import farcore.energy.kinetic.KineticPkg;
import farcore.enums.Direction;

public interface IKineticTile
{
	boolean canAccessKineticEnergyFromDirection(Direction direction);
	
	boolean isRotatable(Direction direction, KineticPkg pkg);
	
	void emitKineticEnergy(IKineticAccess access, Direction direction, KineticPkg pkg);

	void receiveKineticEnergy(IKineticAccess access, Direction direction, KineticPkg pkg);
	
	void onStuck(Direction direction, float speed, float torque);

	void kineticPreUpdate(IKineticAccess access);
}