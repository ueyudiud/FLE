package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalTileEntity 
{	
	public int getTemperature(ForgeDirection dir);
	
	public double getThermalConductivity(ForgeDirection dir);
	
	public double getThermalEnergyCurrect(ForgeDirection dir);
	
	public void onHeatReceive(ForgeDirection dir, double heatValue);

	public void onHeatEmmit(ForgeDirection dir, double heatValue);
}
