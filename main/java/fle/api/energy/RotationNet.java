package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

public abstract class RotationNet extends IEnergyNet
{
	public abstract int getWindSpeed();

	public abstract void emmitRotationTo(BlockPos pos, ForgeDirection dir, RotationPacket packet);
	
	public static class RotationPacket
	{
		private double torque;
		private double speed;
		
		public RotationPacket(double aTorque, double aSpeed) 
		{
			torque = aTorque;
			speed = aSpeed;
		}
		
		public double getTorque()
		{
			return torque;
		}
		
		public double getSpeed()
		{
			return speed;
		}
		
		public double getEnergy()
		{
			return torque * speed;
		}
	}
}