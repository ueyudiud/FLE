package farcore.energy.electric.instance;

import farcore.energy.electric.EnumCurrentType;
import farcore.energy.electric.IElectricalElement;

public class ElementResistance implements IElectricalElement
{
	private IElectricalHandlerHelper helper;
	public final double resistance;

	public ElementResistance(double resistance)
	{
		this.resistance = resistance;
	}
	
	public void setHelper(IElectricalHandlerHelper helper)
	{
		this.helper = helper;
	}
	
	@Override
	public void onUpdate(double voltage, double current)
	{
		if(helper != null)
		{
			helper.heatByCurrent(current * current / resistance);
		}
	}
	
	@Override
	public double eResistance(EnumCurrentType type)
	{
		return resistance;
	}
}