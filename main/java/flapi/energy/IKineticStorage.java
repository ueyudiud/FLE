package flapi.energy;

import farcore.util.Direction;

public interface IKineticStorage extends IEnergyStorage
{	
	long emmitKineticTo(Direction dir, KineticEnergyPacket pkt, boolean process);
	
	long recieveKineticFrom(Direction dir, KineticEnergyPacket pkt, boolean process);
}