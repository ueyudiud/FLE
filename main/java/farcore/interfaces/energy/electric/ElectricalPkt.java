package farcore.interfaces.energy.electric;

public class ElectricalPkt
{
	public double voltage;
	public double current;
	
	public ElectricalPkt(double voltage, double current)
	{
		this.voltage = voltage;
		this.current = current;
	}

	public double energy()
	{
		return voltage * current;
	}
}