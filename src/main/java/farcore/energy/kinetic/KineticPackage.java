/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.kinetic;

/**
 * @author ueyudiud
 */
public class KineticPackage
{
	public double	torque;
	public double	speed;
	
	public KineticPackage(double t, double s)
	{
		this.torque = t;
		this.speed = s;
	}
	
	public boolean isUsable()
	{
		return this.torque > 0;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
}
