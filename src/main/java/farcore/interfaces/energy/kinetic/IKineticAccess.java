package farcore.interfaces.energy.kinetic;

import farcore.energy.kinetic.KineticPkg;
import farcore.enums.Direction;

public interface IKineticAccess
{
	void sendEnergyTo(Direction direction, KineticPkg pkg);
}