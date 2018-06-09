/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import nebula.common.world.ICoord;

/**
 * @author ueyudiud
 */
public interface IElectricHandler extends ICoord
{
	/**
	 * Get nodes owned by this node.
	 * @return
	 */
	IElectricNode[] getNodes();
}
