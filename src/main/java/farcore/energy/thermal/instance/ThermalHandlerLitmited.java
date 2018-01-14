/*
 * copyright© 2016-2018 ueyudiud
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
	public float Tlimit;
	
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
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.Tlimit = nbt.getFloat("Tlimit");
	}
	
	private float Tcurrent(float Tenv, float Tmax)
	{
		Tmax -= Tenv;
		return (float) - Math.expm1(- ((this.energy / this.material.heatCapacity) / Tmax)) * Tmax;
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
}
