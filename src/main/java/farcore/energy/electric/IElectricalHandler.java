package farcore.energy.electric;

import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;

/**
 * @author ueyudiud
 */
public interface IElectricalHandler extends ICoord
{
	int getNodeSize();
	
	IElectricalNode getNode(int id);
	
	IElectricalElement getElement(int id1, int id2);
	
	ISidedElectricalElement getEnelementFromFace(Direction direction);
	
	void onElectricNetTicking(IElectricalNet net);
}