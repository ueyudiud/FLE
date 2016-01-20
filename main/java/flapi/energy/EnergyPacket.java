package flapi.energy;

public class EnergyPacket
{
	double energy;
	
	public EnergyPacket(double energy)
	{
		this.energy = energy;
	}
	
	public EnergyPacket copy()
	{
		return new EnergyPacket(energy);
	}
}