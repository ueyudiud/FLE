/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.energy.thermal;

import farcore.energy.thermal.ThermalNet;
import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.Maths;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
	float tempOffsetMultiplier;
	float tempOffsetScale;
	float tempLimit;
	float tempChangeSpeed;
	
	public ThermalEnergyHelper(float Tbm, float Mto, float Sot, float Tl, float vT)
	{
		this.baseMaxTemp = Tbm;
		this.tempOffsetMultiplier = Mto;
		this.tempOffsetScale = Sot;
		this.tempLimit = Tl;
		setTemperatureChangeSpeed(vT);
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
		return this.T + 3 * this.tempOffsetScale * (float) Math.cbrt(this.E / (1 + this.tempOffsetMultiplier * this.T * this.T));
	}
	
	public void recaculateTemperature(World world, BlockPos pos)
	{
		recaculateTemperature(ThermalNet.getEnviormentTemperature(world, pos));
	}
	
	public void recaculateTemperature(float localTemp)
	{
		this.Tmax = MathHelper.sqrt(this.baseMaxTemp * this.baseMaxTemp + Maths.sq(localTemp / this.tempLimit));
		this.T += (this.Tmax - this.T) * this.tempChangeSpeed;
	}
	
	public float getHeatCapacity()
	{
		return (float) Math.cbrt(this.E * this.E * (1 + this.tempOffsetMultiplier * this.T * this.T)) / this.tempOffsetScale;
	}
	
	public long getInternalEnergy()
	{
		return this.E;
	}
}