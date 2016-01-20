package farcore.util;

/**
 * Not use minecraft language manager.
 * @author ueyudiud
 *
 */
public interface ILanguageManager
{
	/**
	 * Register local
	 * @param locale
	 * @param type
	 * @param objects
	 * @return
	 */
	String registerRecommended(String locale, String type, String localized);
	String registerRecommended(String type, String localized);
	
	String translateToLocal(String str, Object...objects);

	void registerLocal(String locale, String unlocalizedName, String localizedName);
	void registerLocal(String unlocalizedName, String localizedName);
}