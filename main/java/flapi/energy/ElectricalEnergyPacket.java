package flapi.energy;

public class ElectricalEnergyPacket extends EnergyPacket
{
	public double voltage;
	public double current;

	public ElectricalEnergyPacket(double voltage, double current)
	{
		super(voltage * current);
		this.voltage = voltage;
		this.current = current;
	}
	
	public ElectricalEnergyPacket devide(double p)
	{
		return new ElectricalEnergyPacket(voltage, (long) (current * p));
	}
	
	public ElectricalEnergyPacket minus(double v)
	{
		voltage -= v;
		return this;
	}
}