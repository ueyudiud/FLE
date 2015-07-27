package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import fle.api.energy.RotationNet.RotationPacket;

public interface IRotationTileEntity 
{
	public int getEnergyCurrect();
	
	public int canReciveEnergy(ForgeDirection dir);

	public int canEmmitEnergy(ForgeDirection dir);
	
	public void onRotatainReceive(RotationPacket packet);
}
