/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.energy.thermal;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.Maths;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ThermalEnergyHelper implements INBTCompoundReaderAndWritter<ThermalEnergyHelper>
{
	float T;
	float Tmax;
	long E;
	
	float baseMaxTemp;
	float heatCapacity;
	float tempLimit;
	float tempChangeSpeed;
	
	public ThermalEnergyHelper()
	{
	}
	
	public ThermalEnergyHelper(double Tbm, double c, double Tl, double vT)
	{
		setProperties(Tbm, c, Tl, vT);
	}
	
	public final void setProperties(double Tbm, double c, double Tl, double vT)
	{
		this.baseMaxTemp = (float) Tbm;
		setHeatCapacity((float) c);
		this.tempLimit = (float) Tl;
		setTemperatureChangeSpeed((float) vT);
	}
	
	public void setHeatCapacity(float heatCapacity)
	{
		this.heatCapacity = heatCapacity;
	}
	
	@Override
	public ThermalEnergyHelper readFromNBT(NBTTagCompound nbt)
	{
		this.T = nbt.getFloat("t");
		this.Tmax = nbt.getFloat("tmax");
		this.E = nbt.getLong("e");
		this.baseMaxTemp = nbt.getFloat("tbm");
		return this;
	}
	
	@Override
	public void writeToNBT(ThermalEnergyHelper target, NBTTagCompound nbt)
	{
		nbt.setFloat("t", this.T);
		nbt.setFloat("tmax", this.Tmax);
		nbt.setLong("e", target.E);
		nbt.setFloat("tbm", this.baseMaxTemp);
	}
	
	public void setBaseMaxTemperature(float baseMaxTemp)
	{
		this.baseMaxTemp = baseMaxTemp;
	}
	
	public void setTemperatureChangeSpeed(float tempChangeSpeed)
	{
		assert tempChangeSpeed >= 0;
		this.tempChangeSpeed = tempChangeSpeed / (1 + tempChangeSpeed);//Rescaled speed.
	}
	
	public void setInternalEnergy(long energy)
	{
		this.E = energy;
	}
	
	public void addInternalEnergy(long energy)
	{
		this.E += energy;
	}
	
	public float getTemperature()
	{
		return this.T + (float) Maths.asinh(this.E / this.tempLimit) * this.tempLimit / this.heatCapacity;
	}
	
	public void recaculateTemperature(World world, BlockPos pos)
	{
		recaculateTemperature(0F);
	}
	
	public void recaculateTemperature(float localTemp)
	{
		this.Tmax = this.baseMaxTemp;
		this.T += (this.Tmax - this.T) * this.tempChangeSpeed;
	}
	
	public long getInternalEnergy()
	{
		return this.E;
	}
}