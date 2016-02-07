package fle.core.init;

import static fle.init.Substance1.*;

import farcore.block.EnumDirtState;
import farcore.block.EnumRockState;
import farcore.util.IUnlocalized;
import farcore.util.Langs;
import flapi.FleAPI;
import flapi.enums.EnumRockSize;

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
		rt(EnumRockState.smooth, "Smooth %s");
		rt(EnumDirtState.dirt, "%s");
		rt(EnumDirtState.farmland, "%s Farmland");
		rt(EnumDirtState.grass, "%s With Grass");
		rt(EnumDirtState.mycelium, "%s With Mycelium");
		rt(EnumDirtState.moss, "Mossy %s");
		rt(EnumRockState.resource.getUnlocalized() + ".ore.name", "%s Ore");
		rt(EnumRockState.cobble.getUnlocalized() + ".ore.name",
				"Cobble %s Ore");
		rt(EnumRockState.crush.getUnlocalized() + ".ore.name", "Crush %s Ore");
		rt(EnumRockState.smooth.getUnlocalized() + ".ore.name",
				"Smooth %s Ore");
		rt(EnumRockSize.small, "Small %s");
		rt(EnumRockSize.medium, "Medium %s");
		rt(EnumRockSize.large, "Large %s");
		rt(Langs.chinese, EnumRockState.resource, "%s岩");
		rt(Langs.chinese, EnumRockState.cobble, "%s原石");
		rt(Langs.chinese, EnumRockState.crush, "粉碎%s岩");
		rt(Langs.chinese, EnumRockState.smooth, "平滑%s岩");
		rt(Langs.chinese, EnumRockState.resource.getUnlocalized() + ".ore.name",
				"%s矿");
		rt(Langs.chinese, EnumRockState.cobble.getUnlocalized() + ".ore.name",
				"碎%s矿");
		rt(Langs.chinese, EnumRockState.crush.getUnlocalized() + ".ore.name",
				"粉碎%s矿");
		rt(Langs.chinese, EnumRockState.smooth.getUnlocalized() + ".ore.name",
				"平滑%s矿");
		rt(Langs.chinese, EnumDirtState.dirt, "%s");
		rt(Langs.chinese, EnumDirtState.farmland, "%s耕地");
		rt(Langs.chinese, EnumDirtState.grass, "草方块");
		rt(Langs.chinese, EnumDirtState.mycelium, "菌丝");
		rt(Langs.chinese, EnumDirtState.moss, "苔藓方块");
		rt(Langs.chinese, EnumRockSize.small, "小型%s石块");
		rt(Langs.chinese, EnumRockSize.medium, "中型%s石块");
		rt(Langs.chinese, EnumRockSize.large, "大型%s石块");
	}
	
	private static void rt(String unlocalized, String localized)
	{
		FleAPI.lang.registerLocal(unlocalized, localized);
	}
	
	private static void rt(IUnlocalized unlocalized, String localized)
	{
		rt(unlocalized.getUnlocalized() + ".name", localized);
	}
	
	private static void rt(String locale, String unlocalized, String localized)
	{
		FleAPI.lang.registerLocal(locale, unlocalized, localized);
	}
	
	private static void rt(String locale, IUnlocalized unlocalized,
			String localized)
	{
		rt(locale, unlocalized.getUnlocalized() + ".name", localized);
	}
}