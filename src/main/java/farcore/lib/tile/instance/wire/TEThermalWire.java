/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.tile.instance.wire;

import java.util.Arrays;
import java.util.List;

import farcore.energy.thermal.IThermalHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import fle.api.energy.thermal.ThermalEnergyHelper;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TEThermalWire extends TESynchronization//TEWiring? No, you can't transfer heat through dimension :D.
implements IThermalHandler, IDebugableTile
{
	private Mat material;
	
	private ThermalEnergyHelper helper = new ThermalEnergyHelper();
	
	long[] currentChangingHeat = new long[6],  lastChangedHeat = new long[6];
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", this.material.name);
		this.helper.writeToNBT(nbt, "energy");
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setThermalProperty(Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID));
		this.helper.readFromNBT(nbt, "energy");
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	private void setThermalProperty(Mat material)
	{
		this.material = material;
		this.helper.setProperties(0, material.heatCapacity, 1000, material.thermalConductivity);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		//Reinitialize heat counter.
		System.arraycopy(this.currentChangingHeat, 0, this.lastChangedHeat, 0, 6);
		Arrays.fill(this.currentChangingHeat, 0);
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return this.helper.getTemperature();
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.helper.addInternalEnergy(value);
		this.currentChangingHeat[direction.ordinal()] += value;
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("Heat current: " + Arrays.toString(this.lastChangedHeat));
	}
}