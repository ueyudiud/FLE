package farcore.energy.electric.instance;

import farcore.energy.electric.EnumCurrentType;
import farcore.energy.electric.IElectricalNode;

public class NodeDefault implements IElectricalNode
{
	int idx;
	int[] link;
	
	public NodeDefault(int idx, int...links)
	{
		this.idx = idx;
		link = links;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
	}
	
	@Override
	public int getIndex()
	{
		return idx;
	}
	
	@Override
	public int[] link()
	{
		return link;
	}

	@Override
	public double eVoltage(EnumCurrentType type)
	{
		return 0;
	}
}