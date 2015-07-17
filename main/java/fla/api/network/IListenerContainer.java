package fla.api.network;

public interface IListenerContainer extends INetWorkListener
{
	public void onPacketData(int x, int y, int z, byte type, short contain);
}
