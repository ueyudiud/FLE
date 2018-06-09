/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import javax.annotation.Nullable;

import nebula.common.util.Direction;
import nebula.common.world.ICoord;

/**
 * @author ueyudiud
 */
public interface IElectricNode extends ICoord
{
	/**
	 * Get handler of node.
	 * @return the handler.
	 */
	IElectricHandler handler();
	
	/**
	 * Get the node link.
	 * @return the facing of node.
	 */
	@Nullable Direction facing();
	
	/**
	 * Get internal links belong to this node.
	 * @return the links.
	 */
	IElectricLink[] getInternalLinks();
}
