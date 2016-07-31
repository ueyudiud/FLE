package farcore.energy.electric;

import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;

public interface IElectricalHandler extends ICoord
{
	int getNodeSize();

	IElectricalNode getNode(int id);

	IElectricalElement getElement(int id1, int id2);

	ISidedElectricalElement getEnelementFromFace(Direction direction);
}