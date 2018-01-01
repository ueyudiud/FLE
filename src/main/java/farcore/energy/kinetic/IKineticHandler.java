/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.energy.kinetic;

import nebula.common.util.Direction;
import nebula.common.world.ICoord;

/**
 * The kinetic energy handler. Implements by tile entity.
 * 
 * @author ueyudiud
 *
 */
public interface IKineticHandler extends ICoord
{
	/**
	 * Can kinetic energy input or output.
	 * 
	 * @param direction The asked side.
	 * @return
	 */
	boolean canAccessKineticEnergyFromDirection(Direction direction);
	
	/**
	 * Is speed fast enough or torque big enough to rotate machine.
	 * 
	 * @param direction The energy from.
	 * @param speed The speed from source.
	 * @param torque The torque from source.
	 * @return
	 */
	boolean isRotatable(Direction direction, KineticPackage pkg);
	
	/**
	 * Emit kinetic energy to target.
	 * 
	 * @param access
	 * @param direction The energy recieve to.
	 * @param speed
	 * @param torque
	 */
	void emitKineticEnergy(IKineticAccess access, IKineticHandler destination, Direction direction, KineticPackage pkg);
	
	/**
	 * Recieve kinetic energy from target.
	 * 
	 * @param access
	 * @param direction The energy emit from.
	 * @param speed
	 * @param torque
	 * @return
	 */
	double receiveKineticEnergy(IKineticAccess access, IKineticHandler source, Direction direction, KineticPackage pkg);
	
	/**
	 * Called when energy fail to send to next kinetic handler.
	 * 
	 * @param direction
	 * @param speed
	 * @param torque
	 */
	void onStuck(Direction direction, KineticPackage pkg);
	
	/**
	 * Called when kinetic access pre update. The kinetic energy generator
	 * should mark to send energy in this time.
	 * 
	 * @param access
	 */
	void kineticPreUpdate(IKineticAccess access);
}
