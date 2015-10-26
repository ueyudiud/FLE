package fle.api.net;

public interface INetEventEmmiter
{
	/**
	 * Get emmit event contains.
	 * @param aType
	 * @return the contain of this event.
	 */
	short onEmmit(byte aType);
}