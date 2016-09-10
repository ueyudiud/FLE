package farcore.energy.electric;

import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;

/**
 * Use IACHandler for implement instead.
 * @author ueyudiud
 */
@Deprecated
public interface IElectricalHandler extends ICoord
{
	int getNodeSize();

	IElectricalNode getNode(int id);

	IElectricalElement getElement(int id1, int id2);

	ISidedElectricalElement getEnelementFromFace(Direction direction);
}