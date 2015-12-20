package flapi.net;

public interface INetEventListener
{
	void onReceive(byte type, Object contain);
}
