/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.wire;

import java.util.Arrays;
import java.util.List;

import farcore.energy.thermal.IThermalHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import nebula.common.tile.TE04Synchronization;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TEThermalWire extends TE04Synchronization
// TEWiring? No, you can't transfer heat through dimension :D.
implements IThermalHandler, IDebugableTile
{
	private Mat material;
	
	private long energy;
	
	long[] currentChangingHeat = new long[6], lastChangedHeat = new long[6];
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", this.material.name);
		nbt.setLong("energy", this.energy);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setThermalProperty(Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID));
		this.energy = nbt.getLong("energy");
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
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		// Reinitialize heat counter.
		System.arraycopy(this.currentChangingHeat, 0, this.lastChangedHeat, 0, 6);
		Arrays.fill(this.currentChangingHeat, 0);
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (this.energy / (float) this.material.heatCapacity);
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public double getHeatCapacity(Direction direction)
	{
		return this.material.heatCapacity;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.energy += value;
		this.currentChangingHeat[direction.ordinal()] += value;
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("Heat current: " + Arrays.toString(this.lastChangedHeat));
	}
}
