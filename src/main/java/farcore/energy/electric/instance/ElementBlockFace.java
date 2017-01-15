package farcore.energy.electric.instance;

import farcore.energy.electric.IElectricalNode;
import farcore.energy.electric.ISidedElectricalElement;
import farcore.lib.util.Direction;

public class ElementBlockFace implements ISidedElectricalElement
{
	IElectricalNode node;
	Direction dir;
	
	public void setHelper(IElectricalNode node, Direction direction)
	{
		this.dir = direction;
		this.node = node;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
		
	}
	
	@Override
	public double resistance()
	{
		return 0E-8D;
	}
	
	@Override
	public IElectricalNode getNodeBase()
	{
		return this.node;
	}
	
	@Override
	public Direction getSide()
	{
		return this.dir;
	}
}