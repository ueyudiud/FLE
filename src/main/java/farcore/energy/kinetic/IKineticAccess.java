/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.kinetic;

import nebula.common.util.Direction;

/**
 * The kinetic energy handler interface.
 * 
 * @author ueyudiud
 *
 */
public interface IKineticAccess
{
	void sendEnergyTo(Direction direction, KineticPackage pkg);
}
