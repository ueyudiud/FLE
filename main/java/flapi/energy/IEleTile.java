package flapi.energy;

import farcore.util.Direction;
import farcore.world.IObjectInWorld;
import net.minecraft.util.BlockPos;

public interface IEleTile extends IObjectInWorld
{
	/**
	 * Based on 8 direction (Require 4-Dim).
	 * @return
	 */
	Nodable getNodes();
	
	void onNetUpdate();

	void onElectricalUpdate(ElectricalEnergyPacket pkt);
	
	public static interface Nodable
	{		
		BlockPos getTransTarget(Direction dir);
		
		double getResistance(Direction dir);
		
		boolean isConnectable(Direction dir);
		
		boolean isConnect(Direction dir);
		
		double getBasicCurrent(Direction dir);
	}
}