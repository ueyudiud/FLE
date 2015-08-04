package fle.core.init;

import fle.api.material.MaterialAbstract;
import fle.api.material.MaterialOre;
import fle.api.material.Matter;
import fle.api.material.PropertyInfo;
import fle.api.util.SubTag;

public class Materials
{
	public static MaterialOre NativeCopper;
	public static MaterialOre Tetrahedrite;
	public static MaterialOre Enargite;
	public static MaterialOre Cuprite;
	public static MaterialOre Tenorite;
	public static MaterialOre Covellite;
	public static MaterialOre Chalcocite;
	public static MaterialOre Malachite;
	public static MaterialOre Azurite;
	public static MaterialOre Chalcopyrite;
	public static MaterialOre Bornite;

	public static MaterialAbstract HardWood;
	public static MaterialAbstract Flint;
	public static MaterialAbstract Obsidian;
	
	public static void init()
	{
		NativeCopper = new MaterialOre("NativeCopper", Matter.mCu, new PropertyInfo(0xFF834C, 18, 1.2F, 0.8F, 1.0F, 0.2F, 12000), SubTag.ORE_native);
		Tetrahedrite = new MaterialOre("Tetrahedrite", Matter.mCu10Fe2Sb4S13, new PropertyInfo(0xDCBC74, 9, 2.1F, 0.3F, 1.0F, 0.4F, 6800), SubTag.ORE_sulfide);
		Enargite = new MaterialOre("Enargite", Matter.mCu3AsS4, new PropertyInfo(0x7F6A68, 10, 1.3F, 1.0F, 0.4F, 0.5F, 6000), SubTag.ORE_sulfide);
		Cuprite = new MaterialOre("Cuprite", Matter.mCu2O, new PropertyInfo(0xD83E26, 8, 1.0F, 0.4F, 1.0F, 0.9F, 8000), SubTag.ORE_oxide);
		Tenorite = new MaterialOre("Tenorite", Matter.mCuO, new PropertyInfo(0x5F6A73, 9, 1.2F, 0.6F, 1.0F, 1.0F, 9000), SubTag.ORE_oxide);
		Covellite = new MaterialOre("Covellite", Matter.mCuS, new PropertyInfo(new int[]{0x3FC2F8, 0x643EB7}, 11, 2.0F, 0.5F, 1.0F, 1.1F, 7500), SubTag.ORE_sulfide);
		Chalcocite = new MaterialOre("Chalcocite", Matter.mCu2S, new PropertyInfo(0xA5B2C0, 11, 1.3F, 0.2F, 1.0F, 0.6F, 4800), SubTag.ORE_sulfide);
		Malachite = new MaterialOre("Malachite", Matter.mCu_OH2_CO3, new PropertyInfo(0x43D393, 6, 0.8F, 0.3F, 0.9F, 0.7F, 7200), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
		Azurite = new MaterialOre("Azurite", Matter.mCu_OH2_2CO3, new PropertyInfo(0x434BD3, 7, 1.0F, 0.3F, 0.9F, 0.8F, 8000), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
		Chalcopyrite = new MaterialOre("Chalcopyrite", Matter.mCuFeS2, new PropertyInfo(0xF8E3A2, 8, 1.0F, 0.4F, 1.0F, 0.7F, 8400), SubTag.ORE_sulfide);
		Bornite = new MaterialOre("Bornite", Matter.mCu5FeS4, new PropertyInfo(new int[]{0xDCBC74, 0x9D7797}, 9, 1.4F, 0.4F, 1.0F, 0.8F, 9200), SubTag.ORE_sulfide);
		Flint = new MaterialAbstract("Flint", Matter.mSiO2, new PropertyInfo(0x5A5A5A, 12, 0.6F, 0.0F, 1.1F, 1.5F, 13500), SubTag.TOOL_stone, SubTag.TOOL_flint);
		Obsidian = new MaterialAbstract("Obsidian", Matter.mSiO2, new PropertyInfo(0x33344F, 16, 2.1F, 0.0F, 1.0F, 3.0F, 7500), SubTag.TOOL_stone, SubTag.TOOL_flint);
		HardWood = new MaterialAbstract("HardWood", new PropertyInfo(0x7F643D, 20, 0.4F, 1.0F, 0.1F, 0.1F, 4600), SubTag.TOOL_wood);
	}
}