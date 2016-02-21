package farcore.util;

/**
 * Not use minecraft language manager.
 * @author ueyudiud
 *
 */
public interface ILanguageManager
{	
	/**
	 * Translate unlocalized name to localized name,
	 * use minecraft configure locale.
	 * @param str Translate name.
	 * @param objects The format object of translated name, replace custom
	 * display name here.
	 * @return Translated name.
	 */
	String translateToLocal(String str, Object...objects);

	void registerLocal(String locale, String unlocalizedName, String localizedName);
	void registerLocal(String unlocalizedName, String localizedName);
}