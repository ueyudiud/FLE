package flapi.cover;

import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.IRotationTileEntity;
import flapi.energy.RotationNet.RotationPacket;

public interface IRotationCover
{
	double getEnergyCurrect(IRotationTileEntity tile);
	
	boolean canReciveEnergy(IRotationTileEntity tile, ForgeDirection dir);

	boolean canEmitEnergy(IRotationTileEntity tile, ForgeDirection dir);

	int getStuck(IRotationTileEntity tile, RotationPacket packet, ForgeDirection dir);
	
	void onRotationEmit(IRotationTileEntity tile, RotationPacket packet, ForgeDirection dir);
	
	void onRotationReceive(IRotationTileEntity tile, RotationPacket packet, ForgeDirection dir);
	
	/**
	 * 
	 * @param tile
	 * @param stuck
	 * @return Return false to prevent tile active this method.
	 */
	boolean onRotationStuck(IRotationTileEntity tile, int stuck);
}