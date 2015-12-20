package flapi.net;

public interface IPacketMaker
{
	IPacket makeSolidTankPacket(INetEventHandler handler);

	IPacket makeFluidTankPacket(INetEventHandler handler);

	IPacket makeNBTPacket(INetEventHandler handler);

	IPacket makeInventoryPacket(INetEventHandler handler);
	
	IPacket makeInventoryPacket(INetEventHandler handler, int start, int end);

	IPacket makeGuiPacket(byte type, Object contain);
}