/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import java.util.Map;

/**
 * @author ueyudiud
 */
public interface INetworkLocalizationEntry
{
	boolean loadLocalization(LanguageManager manager, Map<String, String> properties,
			String locale, Map<String, String> localization);
}
