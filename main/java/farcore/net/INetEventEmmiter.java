package farcore.net;

public interface INetEventEmmiter
{
	/**
	 * Get emmit event contains.
	 * @param aType
	 * @return the contain of this event.
	 */
	Object onEmit(byte aType);
}