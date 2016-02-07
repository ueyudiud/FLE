package farcore.net;

public interface INetEventListener
{
	void onReceive(byte type, Object contain);
}
