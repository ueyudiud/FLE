package flapi.energy;

import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.RotationNet.RotationPacket;

public interface IRotationTileEntity 
{
	/**
	 * Get total rotation energy of tile.
	 * @return
	 */
	double getRotationEnergyCurrect();

	/**
	 * @return The total rotation energy ticked emit, 
	 * negative number mean tile receive energy more 
	 * than emit energy. (Unit : MJ)
	 */
	double getPreEnergyEmit();
	
	/**
	 * Can accept energy from this direction.
	 * @param dir
	 * @return
	 */
	boolean canReciveEnergy(ForgeDirection dir);

	/**
	 * Can emmit energy from this direction.
	 * @param dir
	 * @return
	 */
	boolean canEmitEnergy(ForgeDirection dir);

	/**
	 * When rotation energy stuck in this tile (something stuck 
	 * in machine), this event will send to last tile.
	 * @param packet
	 * @param dir
	 * @return The stuck level, 0 means no stuck, higher level
	 * means high stuck level.
	 */
	int getStuck(RotationPacket packet, ForgeDirection dir);
	
	void onRotationEmit(RotationPacket packet, ForgeDirection dir);
	
	void onRotationReceive(RotationPacket packet, ForgeDirection dir);
	
	/**
	 * Called when energy stuck in other tile.
	 * @param stuck The level of this event.
	 */
	void onRotationStuck(int stuck);
}