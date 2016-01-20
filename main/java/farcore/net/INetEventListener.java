package farcore.net;

public interface INetEventListener
{
	/**
	 * Receive a small packet (send by emmiter).
	 * @param type
	 * @param contain
	 */
	void onReceive(byte type, Object contain);
}
