package fle.api.util;

public interface ILanguageManager
{
	String translateToLocal(String str, Object...objects);

	void registerLocal(String unlocalizedName, String localizedName);
}
