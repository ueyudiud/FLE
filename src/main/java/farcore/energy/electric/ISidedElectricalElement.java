package farcore.energy.electric;

import farcore.lib.util.Direction;

/**
 * Use only at out-of-machine electrical link.
 * @author ueyudiud
 *
 */
public interface ISidedElectricalElement extends IElectricalElement
{
	IElectricalNode getNodeBase();
	
	Direction getSide();
}