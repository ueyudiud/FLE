package farcore.enums;

public enum EnumUpdateType
{
	NO_UPDATE,	//Only set data at server side.
	SERVER_ONLY,//Only call at server side (Client side use this attribute will no effect).
	ONLY,		//Call at one side.
	CALL_CLIENT;//Set data at server side and send this update to client.
}