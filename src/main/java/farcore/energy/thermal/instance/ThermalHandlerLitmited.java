/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.thermal.instance;

import farcore.energy.thermal.ThermalNet;
import nebula.common.util.Direction;
import nebula.common.world.ICoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

/**
 * @author ueyudiud
 */
public class ThermalHandlerLitmited extends ThermalHandlerAbstract
{
	private float Tlimit;
	public long energy1;
	
	public ThermalHandlerLitmited(ICoord coord)
	{
		super(coord);
	}
	
	public ThermalHandlerLitmited(ICoord coord, float temperatureLimit)
	{
		super(coord);
		this.Tlimit = temperatureLimit;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("Tlimit", this.Tlimit);
		nbt.setLong("energy1", this.energy1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.Tlimit = nbt.getFloat("Tlimit");
		this.energy1 = nbt.getLong("energy1");
	}
	
	public void setLimitTemperature(float Tlimit)
	{
		if (Tlimit != this.Tlimit)
		{
			float Tenv = ThermalNet.getEnvironmentTemperature(world(), pos());
			float Tmax = MathHelper.sqrt(Tenv * Tenv + this.Tlimit * this.Tlimit);
			float Treal = Tcurrent(Tenv, Tmax) + Tenv;
			long value = - (long) (Tmax * Math.log1p(- Treal / Tmax) * this.material.heatCapacity);
			this.energy1 = Math.min(value, this.energy1);
			this.energy = (long) ((Treal + Math.expm1(- ((this.energy1 / this.material.heatCapacity) / Tmax)) * Tmax) * this.material.heatCapacity);
			this.Tlimit = Tlimit;
		}
	}
	
	public void unsetLimitTemperature()
	{
		this.energy = (long) (getTemperatureDifference(Direction.Q) * this.material.heatCapacity);
		this.energy1 = 0L;
		this.Tlimit = 0;
	}
	
	private float Tcurrent(float Tenv, float Tmax)
	{
		Tmax -= Tenv;
		return (float) (this.energy / this.material.heatCapacity - Math.expm1(- ((this.energy1 / this.material.heatCapacity) / Tmax)) * Tmax);
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		if (this.Tlimit > 0)
		{
			float Tenv = ThermalNet.getEnvironmentTemperature(world(), pos());
			float Tmax = MathHelper.sqrt(Tenv * Tenv + this.Tlimit * this.Tlimit);
			
			return Tcurrent(Tenv, Tmax);
		}
		else
		{
			return (float) (this.energy / this.material.heatCapacity);
		}
	}
	
	@Override
	public double getHeatCapacity(Direction direction)
	{
		if (this.Tlimit > 0)
		{
			float Tenv = ThermalNet.getEnvironmentTemperature(world(), pos());
			float Tmax = MathHelper.sqrt(Tenv * Tenv + this.Tlimit * this.Tlimit);
			
			return this.material.heatCapacity / (1 - Tcurrent(Tenv, Tmax) / Tmax);
		}
		else
		{
			return this.material.heatCapacity;
		}
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		if (value < 0 && this.energy1 > 0)
		{
			long value1 = Math.min(- value, this.energy1);
			this.energy1 -= value1;
			value += value1;
		}
		super.onHeatChange(direction, value);
	}
}
