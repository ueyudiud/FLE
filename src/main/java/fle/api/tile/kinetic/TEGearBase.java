/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.tile.kinetic;

import javax.annotation.Nullable;

import farcore.energy.kinetic.IKineticAccess;
import farcore.energy.kinetic.IKineticHandler;
import farcore.energy.kinetic.KineticPackage;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing.Axis;

/**
 * @author ueyudiud
 */
public abstract class TEGearBase extends TEGearBoxBase implements IGearHandler, IAxisHandler
{
	public Axis axis = Axis.Y;
	public double rotation;
	public double speed;
	
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
	
	protected abstract boolean isSideRotatable(Direction direction);
	
	@Override
	public Axis[] getAxisHandlerAxis()
	{
		return new Axis[] {this.axis};
	}
	
	@Override
	public Axis getGearAxis(Direction direction)
	{
		return this.axis;
	}
	
	@Override
	@Nullable
	public RotationType getRotationType(Direction direction)
	{
		return !isSideRotatable(direction) ? null : direction.axis == this.axis ?
				RotationType.AXIS_ROTATE : RotationType.EDGE_ROTATE;
	}
	
	@Override
	public boolean isRotatable(Direction direction, KineticPackage pkg)
	{
		return isSideRotatable(direction) && direction.axis == this.axis ? (!(pkg instanceof KineticPackageExt) ||
				(pkg instanceof KineticPackageAxisRotate)) : (pkg instanceof KineticPackageGearEdgeRotate);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		this.speed = Double.NaN;
	}
	
	@Override
	public double receiveKineticEnergy(IKineticAccess access, IKineticHandler source, Direction direction,
			KineticPackage pkg)
	{
		if (direction.axis == this.axis)
		{
			if (pkg instanceof KineticPackageAxisRotate)
			{
				rotateExclude(access, direction, (KineticPackageExt) pkg);
				return pkg.speed;
			}
			else if (!(pkg instanceof KineticPackageExt))
			{
				rotateExclude(access, direction, KineticPackageExt.wrap(RotationType.AXIS_ROTATE, pkg, direction));
				return pkg.speed;
			}
			else return 0;
		}
		else if (pkg instanceof KineticPackageGearEdgeRotate)
		{
			KineticPackageGearEdgeRotate pkg1 = (KineticPackageGearEdgeRotate) pkg;
			if(pkg1.direction.axis == this.axis)
			{
				float g1 = getGearSize(direction);
				float g2 = getGearTeethSize(direction);
				if (pkg1.gearTeethLen + g1 < 0 && pkg1.gearLen + g2 < 0 && pkg1.gearTeethLen + g2 > 0)
				{
					double total = pkg.speed * pkg.torque;
					pkg.speed = pkg.speed * getGearTeethCount(direction) / pkg1.gearTeethCount;
					pkg.torque = total / pkg.speed;
					rotateExclude(access, direction, (KineticPackageExt) pkg);
				}
				else
				{
					pkg1.torque = 0;//Can not touch.
				}
				return pkg1.speed;
			}
			else
			{
				return 0;
			}
		}
		else return 0;
	}
	
	protected void rotateExclude(IKineticAccess access, Direction source, KineticPackageExt pkg)
	{
		byte count = 0;
		Direction[] allows = new Direction[6];
		for (Direction direction : Direction.DIRECTIONS_3D)
		{
			if (direction == source) continue;
			if (!isSideRotatable(direction)) continue;
			TileEntity tile = getTE(direction);
			if (direction.axis == this.axis)
			{
				if (tile instanceof IAxisHandler)
				{
					IAxisHandler handler = (IAxisHandler) tile;
					if (L.contain(handler.getAxisHandlerAxis(), this.axis))
					{
						allows[count++] = direction;
					}
				}
			}
			else
			{
				if (tile instanceof IGearHandler)
				{
					IGearHandler handler = (IGearHandler) tile;
					Direction oppisite = direction.getOpposite();
					Axis axis = handler.getGearAxis(oppisite);
					if (axis != this.axis) continue;
					float g11 = getGearSize(direction);
					float g12 = getGearTeethSize(direction);
					float g21 = handler.getGearSize(oppisite);
					float g22 = handler.getGearTeethSize(oppisite);
					if (g11 + g22 < 0 && g21 + g12 < 0 && g21 + g22 > 0)
					{
						allows[count++] = direction;
					}
				}
			}
		}
		for (int i = 0; i < count; ++i)
		{
			Direction direction = allows[i];
			KineticPackage pkg1;
			if (direction.axis == this.axis)
			{
				pkg1 = new KineticPackageAxisRotate(pkg.direction, pkg.torque / count, pkg.speed);
			}
			else
			{
				pkg1 = new KineticPackageGearEdgeRotate(pkg.direction.getOpposite(), pkg.torque / count, pkg.speed).setGearProperty(this, direction);
			}
			access.sendEnergyTo(direction, pkg1);
		}
	}
	
	@Override
	public void emitKineticEnergy(IKineticAccess access, IKineticHandler destination, Direction direction,
			KineticPackage pkg)
	{
		;//Do nothing.
	}
}