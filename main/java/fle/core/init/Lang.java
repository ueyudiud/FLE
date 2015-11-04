package fle.core.init;

import fle.core.util.LanguageManager;

public class Lang
{
	public static String inventory_boilingHeater;
	public static String inventory_castingPool;
	public static String inventory_ceramicFurnace;
	public static String inventory_coldForgingPlatform;
	public static String inventory_dryingTable;
	
	public static void init()
	{
		inventory_boilingHeater = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Boiling Heater");
		inventory_castingPool = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Casting Pool");
		inventory_ceramicFurnace = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Ceramic Furnace");
		inventory_coldForgingPlatform = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Cold Forging Platform");
		inventory_dryingTable = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Drying Table");
	}
}
