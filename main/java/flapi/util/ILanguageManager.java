package flapi.util;

public interface ILanguageManager
{
	String translateToLocal(String str, Object...objects);

	void registerLocal(String unlocalizedName, String localizedName);
}
