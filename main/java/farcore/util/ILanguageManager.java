package farcore.util;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

/**
 * Not use minecraft language manager.
 * @author ueyudiud
 *
 */
public interface ILanguageManager
{
	/**
	 * Register local with recommended name(Only for en_US).
	 * @param locale
	 * @param type
	 * @param objects
	 * @return
	 */
	String registerRecommended(String type, String localized);
	
	/**
	 * Translate unlocalized name to localized name,
	 * use minecraft configure locale.
	 * @param str Translate name.
	 * @param objects The format object of translated name, replace custom
	 * display name here.
	 * @return Translated name.
	 */
	String translateToLocal(String str, Object...objects);
	default String translateToLocal(IUnlocalized unlocalized, Object...objects)
	{
		return translateToLocal(unlocalized.getUnlocalized() + ".name", objects);
	}

	void registerLocal(String locale, String unlocalizedName, String localizedName);
	void registerLocal(String unlocalizedName, String localizedName);
	default void registerLocal(String locale, IUnlocalized unlocalized, String localizedName)
	{
		registerLocal(locale, unlocalized.getUnlocalized() + ".name", localizedName);
	}
	default void registerLocal(IUnlocalized unlocalized, String localizedName)
	{
		registerLocal(unlocalized.getUnlocalized() + ".name", localizedName);
	}
}