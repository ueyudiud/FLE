package farcore.interfaces;

public interface IRegisterExceptionHandler
{
	void onIDContain(int id, Object registered);
	
	void onNameContain(int id, String registered);
}