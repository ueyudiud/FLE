package fle.core.init;

import static fle.init.Substances.*;

import farcore.FarCore;
import farcore.util.IUnlocalized;
import farcore.util.Langs;
import flapi.util.Values;
import fle.core.enums.EnumDirtState;
import fle.core.enums.EnumRockSize;
import fle.core.enums.EnumRockState;

public class Lang
{	
	public static void init()
	{
		$void.setTranslate("Void").setTranslate(Langs.chinese, "默认");
		
		stone.setTranslate("Stone").setTranslate(Langs.chinese, "普通");
		compactStone.setTranslate("Compact Stone").setTranslate(Langs.chinese,
				"压缩");
		limestone.setTranslate("Stone").setTranslate(Langs.chinese, "石灰");
		rhyolite.setTranslate("Rhyolite").setTranslate(Langs.chinese, "流纹");
		andesite.setTranslate("Andesite").setTranslate(Langs.chinese, "安山");
		basalt.setTranslate("Basalt").setTranslate(Langs.chinese, "玄武");
		peridotite.setTranslate("Peridotite").setTranslate(Langs.chinese, "橄榄");
		
		nativeCopper.setTranslate("Native Copper").setTranslate(Langs.chinese,
				"原生铜");
		tetrahedrite.setTranslate("Tetrahedrite").setTranslate(Langs.chinese,
				"黝铜");
		enargite.setTranslate("Enargite").setTranslate(Langs.chinese, "硫砷铜");
		cuprite.setTranslate("Cuprite").setTranslate(Langs.chinese, "赤铜");
		tenorite.setTranslate("Tenorite").setTranslate(Langs.chinese, "黑铜");
		covellite.setTranslate("Covellite").setTranslate(Langs.chinese, "靛铜");
		chalcocite.setTranslate("Chalcocite").setTranslate(Langs.chinese, "辉铜");
		malachite.setTranslate("Malachite").setTranslate(Langs.chinese, "孔雀石");
		azurite.setTranslate("Azurite").setTranslate(Langs.chinese, "蓝铜");
		bornite.setTranslate("Bornite").setTranslate(Langs.chinese, "斑铜");
		chalcopyrite.setTranslate("Chalcopyrite").setTranslate(Langs.chinese,
				"黄铜");
		orpiment.setTranslate("Orpiment").setTranslate(Langs.chinese, "雌黄");
		realgar.setTranslate("Realgar").setTranslate(Langs.chinese, "雄黄");
		arsenolite.setTranslate("Arsenolite").setTranslate(Langs.chinese, "砷华");
		nickeline.setTranslate("Nickeline").setTranslate(Langs.chinese, "红砷镍");
		arsenopyrite.setTranslate("Arsenopyrite").setTranslate(Langs.chinese,
				"砷黄铁");
		scorodite.setTranslate("Scorodite").setTranslate(Langs.chinese, "臭葱石");
		erythrite.setTranslate("Erythrite").setTranslate(Langs.chinese, "钴华");
		gelenite.setTranslate("Gelenite").setTranslate(Langs.chinese, "方铅");
		sphalerite.setTranslate("Sphalerite").setTranslate(Langs.chinese, "闪锌");
		cassiterite.setTranslate("Cassiterite").setTranslate(Langs.chinese,
				"锡石");
		stannite.setTranslate("Stannite").setTranslate(Langs.chinese, "黝锡");
		
		sand.setTranslate("Sand").setTranslate(Langs.chinese, "沙子");
		saltSand.setTranslate("Salt Sand").setTranslate(Langs.chinese, "盐沙");
		beachSand.setTranslate("Beach Sand").setTranslate(Langs.chinese, "白沙");
		soulSand.setTranslate("Soul Sand").setTranslate(Langs.chinese, "灵魂沙");
		
		dirt.setTranslate("Dirt").setTranslate(Langs.chinese, "泥土");
		
		rt(EnumRockState.resource, "%s");
		rt(EnumRockState.cobble, "Cobble %s");
		rt(EnumRockState.crush, "Crush %s");
		rt(EnumDirtState.dirt, "%s");
		rt(EnumDirtState.farmland, "%s Farmland");
		rt(EnumDirtState.grass, "%s With Grass");
		rt(EnumDirtState.mycelium, "%s With Mycelium");
		rt(EnumDirtState.moss, "Mossy %s");
		rt(EnumRockState.resource.getUnlocalized() + ".ore.name", "%s Ore");
		rt(EnumRockState.cobble.getUnlocalized() + ".ore.name",
				"Cobble %s Ore");
		rt(EnumRockState.crush.getUnlocalized() + ".ore.name", "Crush %s Ore");
		
		rt(EnumRockSize.small, "Small %s");
		rt(EnumRockSize.medium, "Medium %s");
		rt(EnumRockSize.large, "Large %s");
		rt(Langs.chinese, EnumRockState.resource, "%s岩");
		rt(Langs.chinese, EnumRockState.cobble, "%s原石");
		rt(Langs.chinese, EnumRockState.crush, "粉碎%s岩");
//		rt(Langs.chinese, EnumRockState.smooth, "平滑%s岩");
		rt(Langs.chinese, EnumRockState.resource.getUnlocalized() + ".ore.name",
				"%s矿");
		rt(Langs.chinese, EnumRockState.cobble.getUnlocalized() + ".ore.name",
				"碎%s矿");
		rt(Langs.chinese, EnumRockState.crush.getUnlocalized() + ".ore.name",
				"粉碎%s矿");
//		rt(Langs.chinese, EnumRockState.smooth.getUnlocalized() + ".ore.name",
//				"平滑%s矿");
		rt(Langs.chinese, EnumDirtState.dirt, "%s");
		rt(Langs.chinese, EnumDirtState.farmland, "%s耕地");
		rt(Langs.chinese, EnumDirtState.grass, "草方块");
		rt(Langs.chinese, EnumDirtState.mycelium, "菌丝");
		rt(Langs.chinese, EnumDirtState.moss, "苔藓方块");
		rt(Langs.chinese, EnumRockSize.small, "小型%s石块");
		rt(Langs.chinese, EnumRockSize.medium, "中型%s石块");
		rt(Langs.chinese, EnumRockSize.large, "大型%s石块");
		if(Values.tabFLE != null)
			Values.tabFLE.registerLocal(Langs.chinese, "远陆|核心");
		if(Values.tabFLEResource != null)
			Values.tabFLEResource.registerLocal(Langs.chinese, "远陆|资源");
		if(Values.tabFLEMachine != null)
			Values.tabFLEMachine.registerLocal(Langs.chinese, "远陆|机器");
		if(Values.tabFLETools != null)
			Values.tabFLETools.registerLocal(Langs.chinese, "远陆|工具");
//		cg_casting = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Casting");
//		cg_clay = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Clay Model");
//		cg_coldForging = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Cold Forging");
//		cg_drying = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Drying");
//		cg_oilMill = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Oil Mill");
//		cg_polish = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Polish");
//		cg_crafting_shaped = LanguageManager.regWithRecommendedUnlocalizedName("cg", "FLE Shaped Crafting");
//		cg_crafting_shapeless = LanguageManager.regWithRecommendedUnlocalizedName("cg", "FLE Shaped Crafting");
//		cg_sifter = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Sifter");
//		cg_soak = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Soak");
//		cg_stoneMill = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Stone Mill");
//		cg_washing = LanguageManager.regWithRecommendedUnlocalizedName("cg", "Washing");
//		
//		inventory_bag = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Bag");
//		inventory_boilingHeater = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Boiling Heater");
//		inventory_castingPool = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Casting Pool");
//		inventory_ceramicFurnace = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Ceramic Furnace");
//		inventory_ceramicFireBox = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Ceramic Fire Box");
//		inventory_coldForgingPlatform = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Cold Forging Platform");
//		inventory_dryingTable = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Drying Table");
//		inventory_leverOilMill = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Lever Oil Mill");
//		inventory_polishTable = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Polish Table");
//		inventory_sifter = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Sifter");
//		inventory_stoneMill = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Stone Mill");
//		inventory_terrine = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Terrine");
//		inventory_washing = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Washing");
//		inventory_cermaics = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Cermaics");
//		inventory_barrel = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Barrel");
//		inventory_chest_small = LanguageManager.regWithRecommendedUnlocalizedName("inventory", "Small Chest");
//		
//		info_length = LanguageManager.regWithRecommendedUnlocalizedName("info", "Length");
	}
	
	private static void rt(String unlocalized, String localized)
	{
		FarCore.lang.registerLocal(unlocalized, localized);
	}
	
	private static void rt(IUnlocalized unlocalized, String localized)
	{
		FarCore.lang.registerLocal(unlocalized, localized);
	}
	
	private static void rt(String locale, String unlocalized, String localized)
	{
		FarCore.lang.registerLocal(locale, unlocalized, localized);
	}
	
	private static void rt(String locale, IUnlocalized unlocalized,
			String localized)
	{
		FarCore.lang.registerLocal(locale, unlocalized, localized);
	}
}