package farcore.energy.electric.instance;

import farcore.energy.electric.EnumCurrentType;
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
		links = link;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
		if(helper != null)
		{
			helper.providePowerByVoltage(current * voltage);
		}
	}
	
	@Override
	public int getIndex()
	{
		return index;
	}
	
	@Override
	public int[] link()
	{
		return links;
	}
	
	@Override
	public double eVoltage(EnumCurrentType type)
	{
		return voltage;
	}
}