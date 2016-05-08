package farcore.energy.kinetic;

public class KineticPkg
{
	public float torque;
	public float speed;
	
	public KineticPkg(float torque, float speed)
	{
		this.torque = torque;
		this.speed = speed;
	}
	
	public KineticPkg init(float t, float s)
	{
		this.torque = t;
		this.speed = s;
		return this;
	}
	
	public float energy()
	{
		return torque * speed;
	}
}