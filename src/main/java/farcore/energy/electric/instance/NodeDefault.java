package farcore.energy.electric.instance;

import farcore.energy.electric.IElectricalNode;

public class NodeDefault implements IElectricalNode
{
	int idx;
	int[] link;
	
	public NodeDefault(int idx, int...links)
	{
		this.idx = idx;
		this.link = links;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
	}
	
	@Override
	public int getIndex()
	{
		return this.idx;
	}
	
	@Override
	public int[] link()
	{
		return this.link;
	}
	
	@Override
	public double voltage()
	{
		return 0;
	}
}