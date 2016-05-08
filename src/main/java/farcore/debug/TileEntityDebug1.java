package farcore.debug;

import java.util.Arrays;
import java.util.List;

import farcore.energy.electric.ElectricHelper;
import farcore.energy.electric.ElectricNet;
import farcore.energy.electric.ElectricalPkt;
import farcore.energy.kinetic.KineticHelper;
import farcore.energy.kinetic.KineticPkg;
import farcore.enums.Direction;
import farcore.event.EnergyEvent;
import farcore.interfaces.energy.electric.IElectricTile;
import farcore.interfaces.energy.kinetic.IKineticAccess;
import farcore.interfaces.energy.kinetic.IKineticTile;
import farcore.interfaces.tile.IDebugableTile;
import farcore.lib.tile.TileEntitySyncable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityDebug1 extends TileEntitySyncable implements IKineticTile, IDebugableTile
{
	private KineticHelper helper = new KineticHelper(100F, 100F, 10000F);
	
	public TileEntityDebug1()
	{
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	
	@Override
	protected boolean init()
	{
		super.init();
		MinecraftForge.EVENT_BUS.post(new EnergyEvent.Add(this));
		return true;
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		MinecraftForge.EVENT_BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		MinecraftForge.EVENT_BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	protected void updateServer1()
	{
		super.updateServer1();
		helper.update(this);
	}

	@Override
	public void addDebugInformation(List<String> list)
	{
		list.add("Delta Energy : " + helper.getDeltaEnergy());
	}

	@Override
	public boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return true;
	}

	@Override
	public boolean isRotatable(Direction direction, KineticPkg pkg)
	{
		return helper.rotatable(pkg);
	}

	@Override
	public void emitKineticEnergy(IKineticAccess access, Direction direction, KineticPkg pkg)
	{
		helper.emit(pkg);
	}

	@Override
	public void receiveKineticEnergy(IKineticAccess access, Direction direction, KineticPkg pkg)
	{
		helper.receive(pkg);
		access.sendEnergyTo(direction.getOpposite(), pkg);
	}

	@Override
	public void onStuck(Direction direction, float speed, float torque)
	{
		helper.stuck(speed, torque);
	}

	@Override
	public void kineticPreUpdate(IKineticAccess access)
	{
		if(helper.getEnergy() > 0)
		{
			helper.setEnergy(0);
		}
	}
}