package fle.core.energy;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.material.MaterialAbstract;

public class ThermalTileHelper
{
	private final double hc;
	private final double sh;
	
	private double heatCurrect;
	private double heatEmit;
	private double preHeatEmit;

	public ThermalTileHelper(MaterialAbstract material)
	{
		this(material.getPropertyInfo().getThermalConductivity() * 10, material.getPropertyInfo().getSpecificHeat() * 10000);
	}
	public ThermalTileHelper(double hc, double sh)
	{
		this.hc = hc;
		this.sh = sh;
		heatCurrect = 0F;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		heatCurrect = nbt.getDouble("Heat");
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("Heat", heatCurrect);
	}
	
	public void update()
	{
		preHeatEmit = heatEmit;
		heatEmit = 0D;
	}
	
	public void reseaveHeat(double heat)
	{
		heatEmit -= heat;
		heatCurrect += heat;
	}
	
	public void emitHeat(double heat)
	{
		heatEmit += heat;
		heatCurrect -= heat;
	}
	
	public int getTempreture()
	{
		return (int) (heatCurrect / sh);
	}
	
	public double getHeat()
	{
		return heatCurrect;
	}
	
	public double getThermalConductivity()
	{
		return hc;
	}
	
	@SideOnly(Side.CLIENT)
	public void syncHeat(double heat)
	{
		heatCurrect = heat;
	}
	
	public double getPreHeatEmit()
	{
		return preHeatEmit;
	}
}