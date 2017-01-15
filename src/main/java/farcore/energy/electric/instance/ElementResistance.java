package farcore.energy.electric.instance;

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
		if(this.helper != null)
		{
			this.helper.heatByCurrent(current * current / this.resistance);
		}
	}
	
	@Override
	public double resistance()
	{
		return this.resistance;
	}
}