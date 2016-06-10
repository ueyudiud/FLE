package fle.api.tile;

import farcore.energy.thermal.ThermalHelper;
import farcore.enums.Direction;
import farcore.event.EnergyEvent;
import farcore.handler.FarCoreEnergyHandler;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.lib.tile.TileEntitySyncable;
import farcore.util.U;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityThermalable extends TileEntitySyncable implements IThermalTile
{
	protected ThermalHelper helper;
	private float tempCache = -1;
	
	public TileEntityThermalable(ThermalHelper helper)
	{
		this.helper = helper;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		helper.writeToNBT(nbt);
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		helper.writeToNBT(nbt, "t");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		helper.readFromNBT(nbt);
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		helper.readFromNBT(nbt, "t");
	}
	
	@Override
	protected boolean init()
	{
		if(super.init())
		{
			FarCoreEnergyHandler.BUS.post(new EnergyEvent.Add(this));
			return true;
		}
		return false;
	}
	
	@Override
	protected final void updateServer1()
	{
		tempCache = -1;
		helper.update(this);
		super.updateServer1();
		updateServer2();
	}
	
	protected void updateServer2()
	{
		
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return true;
	}

	@Override
	public float getTemperature(Direction direction)
	{
		if(tempCache == -1)
		{
			tempCache = U.Worlds.getEnviormentTemp(worldObj, xCoord, yCoord, zCoord);
		}
		return helper.temperature() + tempCache;
	}

	@Override
	public float getThermalConductivity(Direction direction)
	{
		return helper.thermalConductivity;
	}

	@Override
	public void receiveThermalEnergy(Direction direction, float value)
	{
		helper.receive(value);
	}

	@Override
	public void emitThermalEnergy(Direction direction, float value)
	{
		helper.emit(value);
	}
	
	@Override
	public float getDeltaHeat()
	{
		return helper.getDeltaHeat();
	}
	
	public void setTemp(float temp)
	{
		helper.setTemperature(temp);
	}
}