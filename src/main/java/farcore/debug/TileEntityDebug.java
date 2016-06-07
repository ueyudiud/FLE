package farcore.debug;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.config.Node;

import farcore.FarCore;
import farcore.FarCoreSetup;
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

public class TileEntityDebug extends TileEntitySyncable implements IKineticTile, IDebugableTile
{
	private KineticHelper helper = new KineticHelper(1E7F);
	
	public TileEntityDebug()
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
		helper.init(this);
		helper.setEnergy(1E7F);
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
		list.add("Energy : " + helper.getEnergy());
		list.add("Delta Energy : " + helper.getDeltaEnergy());
	}

	@Override
	public boolean canAccessKineticEnergyFromDirection(Direction direction)
	{
		return false;
	}

	@Override
	public boolean isRotatable(Direction direction, KineticPkg pkg)
	{
		return true;
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
	}

	@Override
	public void onStuck(Direction direction, float speed, float torque)
	{
		helper.stuck(speed, torque);
	}

	@Override
	public void kineticPreUpdate(IKineticAccess access)
	{
		access.sendEnergyTo(Direction.U, helper.send(10000F, 0.1F));
	}
}