package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

public abstract class RotationNet extends IEnergyNet
{
	@Override
	public final EnumEnergyType getEnergyType()
	{
		return EnumEnergyType.ROTATION;
	}
	
	public abstract int getWindSpeed(BlockPos pos);

	public abstract void emmitRotationTo(BlockPos pos, ForgeDirection dir, RotationPacket packet);
	
	public static class RotationPacket
	{
		final double torque;
		final double speed;
		
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
		
		public RotationPacket copy()
		{
			return new RotationPacket(torque, speed);
		}
		
		public RotationPacket access(double maxSpeed)
		{
			return new RotationPacket(torque, Math.min(maxSpeed, speed));
		}
	}
}