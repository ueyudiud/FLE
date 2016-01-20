package flapi.energy;

public class KineticEnergyPacket extends EnergyPacket
{
	public final double torque;
	public final double speed;
	
	public KineticEnergyPacket(double torque, double speed)
	{
		super(torque * speed);
		this.torque = torque;
		this.speed = speed;
	}
}