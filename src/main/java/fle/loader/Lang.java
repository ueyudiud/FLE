/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import fle.api.util.ToolPropertiesModificater;
import nebula.common.LanguageManager;

/**
 * @author ueyudiud
 */
public class Lang
{
	public static void init()
	{
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.ATTACK_DAMAGE.getTranslateName(), "AD");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.ATTACK_SPEED.getTranslateName(), "AS");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.DURABILITY.getTranslateName(), "MD");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.BRITTLENESS.getTranslateName(), "B");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.THROUGHNESS.getTranslateName(), "T");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.ENCHANTABILITY.getTranslateName(), "E");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.HARVEST_LEVEL.getTranslateName(), "HL");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.MINING_SPEED.getTranslateName(), "MS");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.SPECIAL.getTranslateName(), "");
		LanguageManager.registerLocal(ToolPropertiesModificater.Property.UNKNOWN.getTranslateName(), "???");
	}
}