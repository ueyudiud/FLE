package fle.core.init;

import fle.core.util.LanguageManager;

public class Lang
{
	public static String cg_casting;
	public static String cg_clay;
	public static String cg_coldForging;
	public static String cg_drying;
	public static String cg_oilMill;
	public static String cg_polish;
	public static String cg_crafting_shaped;
	public static String cg_crafting_shapeless;
	public static String cg_sifter;
	public static String cg_soak;
	public static String cg_stoneMill;
	public static String cg_washing;
	
	public static String inventory_boilingHeater;
	public static String inventory_castingPool;
	public static String inventory_ceramicFurnace;
	public static String inventory_ceramicFireBox;
	public static String inventory_coldForgingPlatform;
	public static String inventory_dryingTable;
	public static String inventory_leverOilMill;
	public static String inventory_polishTable;
	public static String inventory_sifter;
	public static String inventory_stoneMill;
	public static String inventory_terrine;
	public static String inventory_bag;
	
	public static String info_length;
	
	public static void init()
	{
		cg_casting = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Casting");
		cg_clay = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Clay Model");
		cg_coldForging = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Cold Forging");
		cg_drying = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Drying");
		cg_oilMill = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Oil Mill");
		cg_polish = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Polish");
		cg_crafting_shaped = LanguageManager.regWithRecommendedUnlocalizedName("cg", "FLE Shaped Crafting");
		cg_crafting_shapeless = LanguageManager.regWithRecommendedUnlocalizedName("cg", "FLE Shaped Crafting");
		cg_sifter = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Sifter");
		cg_soak = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Soak");
		cg_stoneMill = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Stone Mill");
		cg_washing = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Washing");
		
		inventory_bag = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Bag");
		inventory_boilingHeater = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Boiling Heater");
		inventory_castingPool = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Casting Pool");
		inventory_ceramicFurnace = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Ceramic Furnace");
		inventory_ceramicFireBox = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Ceramic Fire Box");
		inventory_coldForgingPlatform = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Cold Forging Platform");
		inventory_dryingTable = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Drying Table");
		inventory_leverOilMill = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Lever Oil Mill");
		inventory_polishTable = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Polish Table");
		inventory_sifter = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Sifter");
		inventory_stoneMill = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Stone Mill");
		inventory_terrine = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Terrine");
		
		info_length = LanguageManager.regWithRecommendedUnlocalizedName("info", "Length");
	}
}