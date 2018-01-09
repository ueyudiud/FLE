/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.tile.kinetic;

import javax.annotation.Nullable;

import farcore.energy.kinetic.IKineticAccess;
import farcore.energy.kinetic.IKineticHandler;
import farcore.energy.kinetic.KineticPackage;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;

/**
 * @author ueyudiud
 */
public class TEAxisBase extends TEGearBoxBase implements IAxisHandler
{
	public Axis		axis	= Axis.Z;
	public double	rotation;
	public double	speed;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		NBTs.setEnum(nbt, "axis", this.axis);
		NBTs.setNumber(nbt, "rotation", this.rotation);
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.axis = NBTs.getEnumOrDefault(nbt, "axis", this.axis);
		this.rotation = NBTs.getDoubleOrDefault(nbt, "rotation", 0);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		
		NBTs.setEnum(nbt, "a", this.axis);
		NBTs.setNumber(nbt, "r", this.rotation);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		
		this.axis = NBTs.getEnumOrDefault(nbt, "a", this.axis);
		this.rotation = NBTs.getDoubleOrDefault(nbt, "r", this.rotation);
	}
	
	@Override
	public Axis[] getAxisHandlerAxis()
	{
		return new Axis[] { this.axis };
	}
	
	@Override
	@Nullable
	public RotationType getRotationType(Direction direction)
	{
		return canAccessKineticEnergyFromDirection(direction) ? RotationType.AXIS_ROTATE : null;
	}
	
	@Override
	public boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return direction.axis == this.axis;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		this.speed = Double.NaN;
	}
	
	@Override
	public double receiveKineticEnergy(IKineticAccess access, IKineticHandler source, Direction direction, KineticPackage pkg)
	{
		if (direction.axis == this.axis)
		{
			if (pkg instanceof KineticPackageExt)
			{
				if (pkg instanceof KineticPackageAxisRotate)
				{
					if (((KineticPackageExt) pkg).direction.isNegative())
					{
						pkg = new KineticPackageAxisRotate(((KineticPackageExt) pkg).direction.opposite(), pkg.torque, -pkg.speed);
					}
					if (this.speed != 0)
					{
						onStuck(direction, pkg);
					}
				}
			}
			else
			{
				
			}
		}
		return super.receiveKineticEnergy(access, source, direction, pkg);
	}
}
