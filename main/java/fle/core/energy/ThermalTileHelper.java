package fle.core.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.material.MaterialAbstract;

public class ThermalTileHelper
{
	private final double hc;
	private final double sh;
	
	private double heatCurrect;
	private double preHeatEmit;

	public ThermalTileHelper(MaterialAbstract material)
	{
		this(material.getPropertyInfo().getThermalConductivity(), material.getPropertyInfo().getSpecificHeat() * 800);
	}
	public ThermalTileHelper(double hc, double sh)
	{
		this.hc = hc;
		this.sh = sh;
		heatCurrect = 0F;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		heatCurrect = nbt.getInteger("Heat");
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Heat", (int) heatCurrect);
	}
	
	public void update()
	{
		preHeatEmit = 0D;
	}
	
	public void reseaveHeat(double heat)
	{
		preHeatEmit -= heat;
		heatCurrect += heat;
	}
	
	public void emitHeat(double heat)
	{
		preHeatEmit += heat;
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