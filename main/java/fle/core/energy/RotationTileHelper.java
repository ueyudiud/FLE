package fle.core.energy;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.energy.RotationNet.RotationPacket;

public class RotationTileHelper
{
	private final double maxSpeed;
	private final double maxTorque;
	private final double maxEnergy;
	private final double maxEnergyCurrect;

	private double energyCurrect;
	private double touqueConduct;
	private double speedConduct;
	private double preTouqueConduct;
	private double preSpeedConduct;
	private double preShowTouqueConduct;
	private double preShowSpeedConduct;
	
	public RotationTileHelper()
	{
		this(-1, -1);
	}
	public RotationTileHelper(double aMaxS, double aMaxT)
	{
		this(aMaxS, aMaxT, -1, 1048576.0D);
	}
	public RotationTileHelper(double aMaxS, double aMaxT, double aMaxE, double aMaxC)
	{
		maxEnergy = aMaxE;
		maxSpeed = aMaxS;
		maxTorque = aMaxT;
		maxEnergyCurrect = aMaxC;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		energyCurrect = nbt.getInteger("Rotation");
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Rotation", (int) energyCurrect);
	}
	
	public void update()
	{
		preShowSpeedConduct = preSpeedConduct = speedConduct;
		preShowTouqueConduct = preTouqueConduct = touqueConduct;
		speedConduct = 0D;
		touqueConduct = 0D;
	}
	
	public int getStuck(RotationPacket pkg)
	{
		double level = 0;
		if(pkg.getEnergy() + energyCurrect > maxEnergyCurrect)
		{
			level += 2;
		}
		if(pkg.getEnergy() > maxEnergy && maxEnergy > 0)
		{
			level += Math.log10(pkg.getEnergy() - maxEnergy + 1) * 5;
		}
		if(pkg.getSpeed() > maxSpeed && maxSpeed > 0)
		{
			level += Math.log10(pkg.getSpeed() - maxSpeed + 1) * 3;
		}
		if(pkg.getTorque() > maxTorque && maxTorque > 0)
		{
			level += Math.log10(pkg.getTorque() - maxTorque + 1) * 2;
		}
		return (int) level;
	}
	
	public void reseaveRotation(RotationPacket pkg)
	{
		speedConduct += pkg.getSpeed();
		touqueConduct += pkg.getTorque();
		energyCurrect += pkg.getEnergy();
	}
	
	public void emitRotation(RotationPacket pkg)
	{
		energyCurrect -= pkg.getEnergy();
	}

	public RotationPacket createRotationPacket(boolean isPassive)
	{
		return createRotationPacket(isPassive, 1);
	}
	
	public RotationPacket createRotationPacket(boolean isPassive, float rotationTransfer)
	{
		double torque = isPassive ? Math.min(preTouqueConduct, maxTorque) : maxTorque;
		double speed = isPassive ? Math.min(preSpeedConduct * rotationTransfer, maxSpeed) : maxSpeed;
		speed = isPassive ? Math.min(energyCurrect / torque, speed) : Math.min(energyCurrect * rotationTransfer / torque, speed);
		return new RotationPacket(torque,speed);
	}
	
	public void addInnerEnergy(double d)
	{
		energyCurrect += Math.min(d, maxEnergyCurrect - energyCurrect);
	}

	public void minusInnerEnergy(double d)
	{
		energyCurrect -= Math.min(d, energyCurrect);
	}
	
	public double getRotationEnergy()
	{
		return energyCurrect;
	}
	
	@SideOnly(Side.CLIENT)
	public void syncHeat(double crt)
	{
		energyCurrect = crt;
	}
	
	public double getShowSpeedConduct()
	{
		return preShowSpeedConduct;
	}
	
	public double getShowTouqueConduct()
	{
		return preShowTouqueConduct;
	}
}