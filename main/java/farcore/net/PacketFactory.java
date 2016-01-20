package farcore.net;

public class PacketFactory
{
	public static IPacket makeNBTPacket(INetEventHandler handler)
	{
		return new FleNBTPacket(handler);
	}

	public static IPacket makeTEPacket(INetEventHandler handler, byte type)
	{
		return new FleTEPacket(handler, type);
	}
}