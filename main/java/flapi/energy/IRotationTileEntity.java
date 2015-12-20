package flapi.energy;

import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.RotationNet.RotationPacket;

public interface IRotationTileEntity 
{
	double getEnergyCurrect();
	
	int getPreEnergyEmit();
	
	boolean canReciveEnergy(ForgeDirection dir);

	boolean canEmitEnergy(ForgeDirection dir);

	int getStuck(RotationPacket packet, ForgeDirection dir);
	
	void onRotationEmit(RotationPacket packet, ForgeDirection dir);
	
	void onRotationReceive(RotationPacket packet, ForgeDirection dir);
	
	void onRotationStuck(int stuck);
}
