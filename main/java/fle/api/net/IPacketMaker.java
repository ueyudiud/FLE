package fle.api.net;

import fle.api.te.ITEInWorld;

public interface IPacketMaker
{
	IPacket makeSolidTankPacket(ITEInWorld te);

	IPacket makeFluidTankPacket(ITEInWorld te);

	IPacket makeNBTPacket(INetEventHandler handler);

	IPacket makeInventoryPacket(ITEInWorld te);
	
	IPacket makeInventoryPacket(ITEInWorld te, int start, int end);

	IPacket makeGuiPacket(byte type, Object contain);
}