package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalTileEntity 
{
	public int getEnergyCurrect();
	
	public int getTemperature(ForgeDirection dir);
	
	public int getThermalConductivity(ForgeDirection dir);
	
	public int getThermalEnergyCurrect(ForgeDirection dir);
	
	public void onHeatReceive(int hearValue);
}
