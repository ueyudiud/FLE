/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader;

import fle.api.util.ToolPropertiesModificater;
import nebula.common.LanguageManager;
import nebula.common.util.EnumChatFormatting;

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
		
		LanguageManager.registerLocal("info.blockable.tool.place", "Right click ground to place on the top.");
		
		LanguageManager.registerLocal("info.adobe.drying.wet", "It seems wet because of rain.");
		LanguageManager.registerLocal("info.adobe.drying.notdried", "It isn't dry yet.");
		LanguageManager.registerLocal("info.adobe.drying.quitedried", "It is quite dry now.");
		LanguageManager.registerLocal("info.adobe.drying.dried", "It is dry.");
		
		LanguageManager.registerLocal("info.mode.type", EnumChatFormatting.WHITE + "Mode: " + EnumChatFormatting.GOLD + "%s");
		
		LanguageManager.registerLocal("info.mode.chisel.carve", "Carve");
		LanguageManager.registerLocal("info.mode.chisel.polish", "Polish");
	}
}
