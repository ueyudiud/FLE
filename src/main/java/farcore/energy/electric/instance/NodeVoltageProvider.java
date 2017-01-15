package farcore.energy.electric.instance;

import farcore.energy.electric.IElectricalNode;

public class NodeVoltageProvider implements IElectricalNode
{
	double voltage;
	
	IElectricalHandlerHelper helper;
	int index;
	int[] links;
	
	public NodeVoltageProvider(double voltage)
	{
		this.voltage = voltage;
	}
	
	public void setHelper(IElectricalHandlerHelper helper, int index, int...link)
	{
		this.helper = helper;
		this.index = index;
		this.links = link;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
		if(this.helper != null)
		{
			this.helper.providePowerByVoltage(current * voltage);
		}
	}
	
	@Override
	public int getIndex()
	{
		return this.index;
	}
	
	@Override
	public int[] link()
	{
		return this.links;
	}
	
	@Override
	public double voltage()
	{
		return this.voltage;
	}
}