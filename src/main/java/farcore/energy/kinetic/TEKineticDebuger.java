/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.kinetic;

import farcore.handler.FarCoreEnergyHandler;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;

/**
 * @author ueyudiud
 */
public class TEKineticDebuger extends TESynchronization implements IKineticHandler
{
	@Override
	public boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return direction == Direction.D;
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
	public boolean isRotatable(Direction direction, KineticPackage pkg)
	{
		return true;
	}
	
	@Override
	public void emitKineticEnergy(IKineticAccess access, IKineticHandler destination, Direction direction, KineticPackage pkg)
	{
		;
	}
	
	@Override
	public double receiveKineticEnergy(IKineticAccess access, IKineticHandler source, Direction direction, KineticPackage pkg)
	{
		return pkg.speed;
	}
	
	@Override
	public void onStuck(Direction direction, KineticPackage pkg)
	{
		
	}
	
	@Override
	public void kineticPreUpdate(IKineticAccess access)
	{
		access.sendEnergyTo(Direction.U, new KineticPackage(100D, 100D));
	}
}
