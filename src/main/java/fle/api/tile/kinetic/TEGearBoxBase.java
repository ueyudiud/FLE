/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.tile.kinetic;

import javax.annotation.Nullable;

import farcore.energy.kinetic.IKineticAccess;
import farcore.energy.kinetic.IKineticHandler;
import farcore.energy.kinetic.KineticPackage;
import farcore.handler.FarCoreEnergyHandler;
import nebula.common.tile.TE04Synchronization;
import nebula.common.util.Direction;

/**
 * @author ueyudiud
 */
public class TEGearBoxBase extends TE04Synchronization implements IKineticHandler
{
	public static enum RotationType
	{
		EDGE_ROTATE, ROPE_ROTATE, AXIS_ROTATE;
	}
	
	public static class KineticPackageExt extends KineticPackage
	{
		public final RotationType	type;
		public final Direction		direction;
		
		protected KineticPackageExt(RotationType type, Direction direction, double t, double s)
		{
			super(t, s);
			this.type = type;
			this.direction = direction;
		}
		
		public static KineticPackageExt wrap(RotationType type, KineticPackage pkg, Direction direction)
		{
			return new KineticPackageExt(type, direction, pkg.torque, pkg.speed);
		}
	}
	
	public static class KineticPackageGearEdgeRotate extends KineticPackageExt
	{
		public int		gearTeethCount;
		public float	gearLen;
		public float	gearTeethLen;
		
		public KineticPackageGearEdgeRotate(Direction direction, double t, double s)
		{
			super(RotationType.EDGE_ROTATE, direction, t, s);
		}
		
		public KineticPackageGearEdgeRotate setGearProperty(IGearHandler handler, Direction direction)
		{
			this.gearLen = handler.getGearSize(direction);
			this.gearTeethLen = handler.getGearTeethSize(direction);
			this.gearTeethCount = handler.getGearTeethCount(direction);
			return this;
		}
	}
	
	public static class KineticPackageAxisRotate extends KineticPackageExt
	{
		public KineticPackageAxisRotate(Direction direction, double t, double s)
		{
			super(RotationType.AXIS_ROTATE, direction, t, s);
		}
	}
	
	public static class KineticPackageRopeRotate extends KineticPackageExt
	{
		public KineticPackageRopeRotate(Direction direction, double t, double s)
		{
			super(RotationType.ROPE_ROTATE, direction, t, s);
		}
	}
	
	@Nullable
	public RotationType getRotationType(Direction direction)
	{
		return null;
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	public void update()
	{
		super.update();
	}
	
	@Override
	public boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return true;
	}
	
	@Override
	public boolean isRotatable(Direction direction, KineticPackage pkg)
	{
		return false;
	}
	
	@Override
	public void emitKineticEnergy(IKineticAccess access, IKineticHandler destination, Direction direction, KineticPackage pkg)
	{
		
	}
	
	@Override
	public double receiveKineticEnergy(IKineticAccess access, IKineticHandler source, Direction direction, KineticPackage pkg)
	{
		return 0;
	}
	
	@Override
	public void onStuck(Direction direction, KineticPackage pkg)
	{
		
	}
	
	@Override
	public void kineticPreUpdate(IKineticAccess access)
	{
		
	}
}
