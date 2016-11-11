package farcore.data;

import farcore.FarCore;
import farcore.lib.collection.IPropertyMap.IProperty;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.instance.CropCabbage;
import farcore.lib.crop.instance.CropCotton;
import farcore.lib.crop.instance.CropFlax;
import farcore.lib.crop.instance.CropMillet;
import farcore.lib.crop.instance.CropPotato;
import farcore.lib.crop.instance.CropReed;
import farcore.lib.crop.instance.CropSoybean;
import farcore.lib.crop.instance.CropSweetPotato;
import farcore.lib.crop.instance.CropWheat;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.instance.TreeAcacia;
import farcore.lib.tree.instance.TreeAspen;
import farcore.lib.tree.instance.TreeBirch;
import farcore.lib.tree.instance.TreeCeiba;
import farcore.lib.tree.instance.TreeMorus;
import farcore.lib.tree.instance.TreeOak;
import farcore.lib.tree.instance.TreeOakBlack;
import farcore.lib.tree.instance.TreeWillow;
import farcore.lib.util.SubTag;
import net.minecraft.block.material.Material;

public class M
{
	/*
	 * Material properties.
	 */
	public static final String fire_encouragement = "fire_encouragement";
	public static final String flammability = "flammability";
	public static final String light_value = "light_value";
	public static final String light_opacity = "light_opacity";
	public static final String fire_spread_speed = "fire_spread_speed";
	public static final String fallen_damage_deduction = "fallen_damage_deduction";
	
	public static final IProperty<PropertyBasic> property_basic = new IProperty<PropertyBasic>() { @Override public PropertyBasic defValue() { return PropertyBasic.INSTANCE; } };
	public static final IProperty<PropertyTool> property_tool = new IProperty<PropertyTool>(){};
	public static final IProperty<PropertyOre> property_ore = new IProperty<PropertyOre>(){@Override public PropertyOre defValue() { return PropertyOre.INSTANCE; } };
	public static final IProperty<PropertyWood> property_wood = new IProperty<PropertyWood>(){};
	public static final IProperty<PropertyBlockable> property_soil = new IProperty<PropertyBlockable>(){};
	public static final IProperty<PropertyRock> property_rock = new IProperty<PropertyRock>(){};
	public static final IProperty<ICrop> property_crop = new IProperty<ICrop>(){@Override public ICrop defValue() { return ICrop.VOID; } };
	public static final IProperty<IPlant> property_plant = new IProperty<IPlant>(){};

	public static final Mat VOID = new Mat(-1, false, FarCore.ID, "void", "Void", "Void").setToolable(0, 1, 1.0F, 0.0F, 1.0F, 1.0F, 0).setHandable(1.0F).setCrop(ICrop.VOID);
	//Rocks
	public static final Mat stone = new Mat(7001, "minecraft", "stone", "Stone", "Stone").setRock(4, 1.5F, 8F, 370).setToolable(5, 16, 1.2F, 0.8F, 0.8F, 1.0F, 8).setRGBa(0x626262FF);
	public static final Mat compact_stone = new Mat(7002, FarCore.ID, "stone-compact", "CompactStone", "Compact Stone").setRock(5, 2.0F, 12F, 370).setToolable(6, 22, 1.8F, 0.7F, 0.8F, 1.0F, 6).setRGBa(0x686868FF);
	public static final Mat andesite = new Mat(7003, FarCore.ID, "andesite", "Andesite", "Andesite").setRock(7, 4.9F, 16.6F, 370).setToolable(8, 32, 2.3F, 0.8F, 0.8F, 1.5F, 8).setRGBa(0x616162FF);
	public static final Mat basalt = new Mat(7004, FarCore.ID, "basalt", "Basalt", "Basalt").setRock(7, 5.3F, 18.3F, 370).setToolable(8, 38, 2.5F, 0.8F, 0.8F, 1.2F, 8).setRGBa(0x3A3A3AFF);
	public static final Mat diorite = new Mat(7005, FarCore.ID, "diorite", "Diorite", "Diorite").setRock(9, 6.5F, 23.3F, 370).setToolable(10, 42, 2.7F, 0.8F, 0.8F, 1.4F, 8).setRGBa(0xC9C9CDFF);
	public static final Mat gabbro = new Mat(7006, FarCore.ID, "gabbro", "Gabbro", "Gabbro").setRock(10, 6.9F, 25.2F, 370).setToolable(11, 40, 2.6F, 0.8F, 0.8F, 1.5F, 6).setRGBa(0x53524EFF);
	public static final Mat granite = new Mat(7007, FarCore.ID, "granite", "Granite", "Granite").setRock(10, 7.4F, 29.8F, 370).setToolable(11, 44, 2.8F, 0.8F, 0.8F, 1.8F, 4).setRGBa(0x986C5DFF);
	public static final Mat kimberlite = new Mat(7008, FarCore.ID, "kimberlite", "Kimberlite", "Kimberlite").setRock(10, 7.8F, 31.4F, 370).setToolable(11, 46, 3.1F, 0.8F, 0.8F, 1.8F, 4).setRGBa(0x4D4D49FF);
	public static final Mat limestone = new Mat(7009, FarCore.ID, "limestone", "Lime", "Limestone").setRock(4, 1.3F, 5.5F, 370).setRGBa(0xC9C9C8FF);
	public static final Mat marble = new Mat(7010, FarCore.ID, "marble", "Marble", "Marble").setRock(6, 7.8F, 8.4F, 370).setRGBa(0xE2E6F0FF);
	public static final Mat netherrack = new Mat(7011, "minecraft", "netherrack", "Netherrack", "Netherrack").setRock(3, 1.3F, 3.8F, 180).setRGBa(0x5F3636FF);
	public static final Mat obsidian = new Mat(7012, FarCore.ID, "obsidian", "Obsidian", "Obsidian").setRock(17, 9.8F, 4.2F, 370).setToolable(12, 8, 5.2F, 2.7F, 0.8F, 3F, 12).setRGBa(0x12121BFF);
	public static final Mat peridotite = new Mat(7013, FarCore.ID, "peridotite", "Peridotite", "Peridotite").setRock(10, 7.7F, 30.5F, 370).setToolable(11, 45, 3.0F, 0.8F, 0.8F, 2.0F, 5).setRGBa(0x717A5CFF);
	public static final Mat rhyolite = new Mat(7014, FarCore.ID, "rhyolite", "Rhyolite", "Rhyolite").setRock(9, 6.0F, 21.7F, 370).setToolable(10, 39, 2.6F, 0.8F, 0.8F, 2.0F, 8).setRGBa(0x4F535AFF);
	public static final Mat graniteP = new Mat(7015, FarCore.ID, "graniteP", "GranitePegmatite", "Granite Pegmatite").setRock(10, 7.6F, 30.1F, 370).setToolable(11, 45, 2.8F, 0.8F, 0.8F, 1.8F, 6).setRGBa(0x4F535AFF);
	//Soils
	public static final Mat latosol = new Mat(7101, FarCore.ID, "latosol", "Latosol", "Latosol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x652A1FFF);
	public static final Mat latoaluminosol = new Mat(7102, FarCore.ID, "latoaluminosol", "Latoaluminosol", "Latoaluminosol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x77412FFF);
	public static final Mat ruboloam = new Mat(7103, FarCore.ID, "ruboloam", "Ruboloam", "Ruboloam").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x773E22FF);
	public static final Mat ruboaluminoloam = new Mat(7104, FarCore.ID, "ruboaluminoloam", "Ruboaluminoloam", "Ruboaluminoloam").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x8E5938FF);
	public static final Mat flavoloam = new Mat(7105, FarCore.ID, "flavoloam", "Flavoloam", "Flavoloam").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x907451FF);
	public static final Mat peatsol = new Mat(7106, FarCore.ID, "peatsol", "Peatsol", "Peatsol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x1B1715FF);
	public static final Mat aterosol = new Mat(7107, FarCore.ID, "aterosol", "Aterosol", "Aterosol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x1A110EFF);
	public static final Mat podzol = new Mat(7108, FarCore.ID, "podzol", "Podzol", "Podzol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x281812FF);
	public static final Mat pheosol = new Mat(7109, FarCore.ID, "pheosol", "Pheosol", "Pheosol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x6C4626FF);
	public static final Mat aterocalcosol = new Mat(7110, FarCore.ID, "aterocalcosol", "Aterocalcosol", "Aterocalcosol").setSoil(0.6F, 3.0F, Material.GROUND).setRGBa(0x25211EFF);
	//Trees
	public static final Mat oak = new Mat(8001, "minecraft", "oak", "Oak", "Oak").setWood(5.3F, 1.0F, 20.0F);
	public static final Mat spruce = new Mat(8002, "minecraft", "spruce", "Spruce", "Spruce").setWood(2.3F, 1.0F, 20.0F);
	public static final Mat birch = new Mat(8003, "minecraft", "birch", "Birch", "Birch").setWood(4.0F, 1.0F, 20.0F);
	public static final Mat ceiba = new Mat(8004, "minecraft", "ceiba", "Ceiba", "Ceiba").setWood(1.1F, 1.0F, 20.0F);
	public static final Mat acacia = new Mat(8005, "minecraft", "acacia", "Acacia", "Acacia").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat oak_black = new Mat(8006, "minecraft", "oak-black", "DarkOak", "Dark Oak").setWood(5.4F, 1.0F, 20.0F);
	public static final Mat aspen = new Mat(8011, FarCore.ID, "aspen", "Aspen", "Aspen").setWood(1.6F, 1.0F, 20.0F);
	public static final Mat morus = new Mat(8012, FarCore.ID, "morus", "Morus", "Morus").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat willow = new Mat(8013, FarCore.ID, "willow", "Willow", "Willow").setWood(3.0F, 1.0F, 20.0F);
	//Crops
	public static final Mat wheat = new Mat(9001, FarCore.ID, "wheat", "Wheat", "Wheat");
	public static final Mat millet = new Mat(9002, FarCore.ID, "millet", "Millet", "Millet");
	public static final Mat soybean = new Mat(9003, FarCore.ID, "soybean", "Soybean", "Soybean");
	public static final Mat potato = new Mat(9004, FarCore.ID, "potato", "Potato", "Potato");
	public static final Mat sweet_potato = new Mat(9005, FarCore.ID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat cabbage = new Mat(9006, FarCore.ID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat reed = new Mat(9007, FarCore.ID, "reed", "Reed", "Reed");
	public static final Mat flax = new Mat(9008, FarCore.ID, "flax", "Flax", "Flax");
	public static final Mat cotton = new Mat(9009, FarCore.ID, "cotton", "Cotton", "Cotton");
	public static final Mat ramie = new Mat(9010, FarCore.ID, "ramie", "Ramie", "Ramie");
	//Plants
	public static final Mat vine = new Mat(9201, FarCore.ID, "vine", "Vine", "Vine").setRGBa(0x867C50FF).setTag(SubTag.ROPE);
	public static final Mat ivy = new Mat(9202, FarCore.ID, "ivy", "Ivy", "Ivy").setRGBa(0x867C50FF).setTag(SubTag.ROPE);
	public static final Mat rattan = new Mat(9203, FarCore.ID, "rattan", "Rattan", "Rattan").setRGBa(0x867C50FF).setTag(SubTag.ROPE);
	public static final Mat spider_silk = new Mat(9204, FarCore.ID, "spider_silk", "SpiderSilk", "Spider's Silk").setRGBa(0xFAFAFAFF).setTag(SubTag.ROPE);
	public static final Mat ramie_dry = new Mat(9205, FarCore.ID, "ramie_dry", "RamieDry", "Ramie").setRGBa(0xCFC898FF).setTag(SubTag.ROPE);
	//Ores
	public static final Mat native_copper = new Mat(10001, FarCore.ID, "nativeCopper", "NativeCopper", "Native Copper").setChemicalFormula("Cu").setRGBa(0xFF834CFF).setOreProperty(7, 8.0F, 9.0F);
	public static final Mat malachite = new Mat(10002, FarCore.ID, "malachite", "Malachite", "Malachite").setChemicalFormula("Cu(OH)2·CuCO3").setRGBa(0x30CE88FF).setOreProperty(8, 8.8F, 9.0F);
	public static final Mat cuprite = new Mat(10003, FarCore.ID, "cuprite", "Cuprite", "Cuprite").setChemicalFormula("Cu2O").setRGBa(0xD83E26FF).setOreProperty(10, 9.6F, 9.6F);
	public static final Mat azurite = new Mat(10004, FarCore.ID, "azurite", "Azurite", "Azurite").setChemicalFormula("Cu(OH)2·2(CuCO3)").setRGBa(0x3039CEFF).setOreProperty(10, 9.5F, 9.5F);
	public static final Mat chalcocite = new Mat(10005, FarCore.ID, "chalcocite", "Chalcocite", "Chalcocite").setChemicalFormula("Cu2S").setRGBa(0x8C96A1FF).setOreProperty(13, 10.8F, 10.2F);
	public static final Mat tenorite = new Mat(10006, FarCore.ID, "tenorite", "Tenorite", "Tenorite").setChemicalFormula("CuO").setRGBa(0x4E5A63FF).setOreProperty(12, 10.2F, 9.7F);
	public static final Mat chalcopyrite = new Mat(10007, FarCore.ID, "chalcopyrite", "Chalcopyrite", "Chalcopyrite").setChemicalFormula("CuFeS2").setRGBa(0xF3DA8AFF).setOreProperty(10, 9.0F, 9.6F);
	public static final Mat bornite = new Mat(10008, FarCore.ID, "bornite", "Bornite", "Bornite").setChemicalFormula("Cu5FeS4").setRGBa(0xD3BA78FF).setOreProperty(14, 13.4F, 11.4F);
	public static final Mat covellite = new Mat(10009, FarCore.ID, "covellite", "Covellite", "Covellite").setChemicalFormula("CuS").setRGBa(0x0B69D2FF).setOreProperty(20, 18.4F, 14.8F);
	public static final Mat tetrahedrite = new Mat(10010, FarCore.ID, "tetrahedrite", "Tetrahedrite", "Tetrahedrite").setChemicalFormula("[Cu,Fe]12Sb4S13").setRGBa(0x999782FF).setOreProperty(21, 19.2F, 15.0F);
	public static final Mat argentite = new Mat(10011, FarCore.ID, "argentite", "Argentite", "Argentite").setChemicalFormula("Ag2S").setRGBa(0x535455FF).setOreProperty(17, 15.4F, 12.8F);
	public static final Mat pyrargyrite = new Mat(10012, FarCore.ID, "pyrargyrite", "Pyrargyrite", "Pyrargyrite").setChemicalFormula("Ag3SbS3").setRGBa(0x70483EFF).setOreProperty(18, 16.0F, 13.2F);
	public static final Mat enargite = new Mat(10013, FarCore.ID, "enargite", "Enargite", "Enargite").setChemicalFormula("Cu3AsS4").setRGBa(0x705A59FF).setOreProperty(13, 12.4F, 13.9F);
	public static final Mat realgar = new Mat(10014, FarCore.ID, "realgar", "Realgar", "Realgar").setChemicalFormula("As4S4").setRGBa(0xEF2B28FF).setOreProperty(8, 8.4F, 9.1F);
	public static final Mat orpiment = new Mat(10015, FarCore.ID, "orpiment", "Orpiment", "Orpiment").setChemicalFormula("As2S3").setRGBa(0xE6BB45FF).setOreProperty(9, 8.8F, 9.4F);
	public static final Mat arsenolite = new Mat(10016, FarCore.ID, "arsenolite", "Arsenolite", "Arsenolite").setChemicalFormula("As4O6").setRGBa(0x949960FF).setOreProperty(16, 14.6F, 14.0F);
	public static final Mat nickeline = new Mat(10017, FarCore.ID, "nickeline", "Nickeline", "Nickeline").setChemicalFormula("NiAs").setRGBa(0xB2A76AFF).setOreProperty(31, 27.9F, 17.3F);
	public static final Mat pyrite = new Mat(10018, FarCore.ID, "pyrite", "Pyrite", "Pyrite").setChemicalFormula("FeS2").setRGBa(0xDBCEA0FF).setOreProperty(16, 14.8F, 14.2F);
	public static final Mat arsenopyrite = new Mat(10019, FarCore.ID, "arsenopyrite", "Arsenopyrite", "Arsenopyrite").setChemicalFormula("FeAsS").setRGBa(0xA3A29AFF).setOreProperty(17, 15.2F, 13.9F);
	public static final Mat scorodite = new Mat(10020, FarCore.ID, "scorodite", "Scorodite", "Scorodite").setChemicalFormula("FeAsO4·2(H2O)").setRGBa(0x32538AFF).setOreProperty(25, 22.3F, 15.8F);
	public static final Mat erythrite = new Mat(10021, FarCore.ID, "erythrite", "Erythrite", "Erythrite").setChemicalFormula("Co3(AsO4)2·8(H2O)").setRGBa(0xA9355EFF).setOreProperty(37, 32.1F, 19.3F);
	public static final Mat domeykite = new Mat(10022, FarCore.ID, "domeykite", "Domeykite", "Domeykite").setChemicalFormula("Cu3As").setRGBa(0xA28261FF).setOreProperty(14, 13.5F, 11.8F);
	public static final Mat cassiterite = new Mat(10023, FarCore.ID, "cassiterite", "Cassiterite", "Cassiterite").setChemicalFormula("SnO2").setRGBa(0xBCB8A5FF).setOreProperty(13, 12.6F, 10.5F);
	public static final Mat gelenite = new Mat(10024, FarCore.ID, "gelenite", "Gelenite", "Gelenite").setChemicalFormula("PbS").setRGBa(0x6F7280FF).setOreProperty(11, 10.8F, 9.3F);
	public static final Mat stannite = new Mat(10025, FarCore.ID, "stannite", "Stannite", "Stannite").setChemicalFormula("Cu2FeSnS4").setRGBa(0xB5B4A2FF).setOreProperty(17, 16.0F, 13.2F);
	public static final Mat sphalerite = new Mat(10026, FarCore.ID, "sphalerite", "Sphalerite", "Sphalerite").setChemicalFormula("ZnS").setRGBa(0xB2A7A5FF).setOreProperty(22, 19.8F, 15.5F);
	public static final Mat teallite = new Mat(10027, FarCore.ID, "teallite", "Teallite", "Teallite").setChemicalFormula("PbSnS2").setRGBa(0xCECECEFF).setOreProperty(23, 20.3F, 15.9F);
	public static final Mat wolframite = new Mat(10028, FarCore.ID, "wolframite", "Wolframite", "Wolframite").setChemicalFormula("[Fe,Mn]WO4").setRGBa(0x2F2F2FFF).setOreProperty(40, 34.2F, 20.5F);
	public static final Mat scheelite = new Mat(10029, FarCore.ID, "scheelite", "Scheelite", "Scheelite").setChemicalFormula("CaWO4").setRGBa(0xBD975AFF).setOreProperty(42, 35.7F, 21.8F);
	public static final Mat bismuthinite = new Mat(10030, FarCore.ID, "bismuthinite", "Bismuthinite", "Bismuthinite").setChemicalFormula("Bi2S3").setRGBa(0x434A43FF).setOreProperty(29, 26.2F, 16.4F);
	public static final Mat native_silver = new Mat(10031, FarCore.ID, "nativeSilver", "NativeSilver", "Native Silver").setChemicalFormula("Ag").setRGBa(0xEBE9E8FF).setOreProperty(13, 11.2F, 12.9F, SubTag.ORE_NOBLE);
	public static final Mat native_gold = new Mat(10032, FarCore.ID, "nativeGold", "NativeGold", "Native Gold").setChemicalFormula("Au").setRGBa(0xF7B32AFF).setOreProperty(5, 6.8F, 8.3F, SubTag.ORE_NOBLE);
	public static final Mat electrum = new Mat(10033, FarCore.ID, "electrum", "Electrum", "Electrum").setChemicalFormula("?").setRGBa(0xE4B258FF).setOreProperty(11, 8.2F, 9.2F, SubTag.ORE_NOBLE);

	static
	{
		VOID.addProperty(property_wood, PropertyTree.VOID);
		
		oak.setTree(new TreeOak());
		spruce.setTree(new TreeBirch());
		birch.setTree(new TreeBirch());
		ceiba.setTree(new TreeCeiba());
		acacia.setTree(new TreeAcacia());
		oak_black.setTree(new TreeOakBlack());
		aspen.setTree(new TreeAspen());
		morus.setTree(new TreeMorus());
		willow.setTree(new TreeWillow());
		
		wheat.setCrop(new CropWheat(wheat));
		millet.setCrop(new CropMillet(millet));
		soybean.setCrop(new CropSoybean(soybean));
		potato.setCrop(new CropPotato(potato));
		sweet_potato.setCrop(new CropSweetPotato(sweet_potato));
		cabbage.setCrop(new CropCabbage(cabbage));
		reed.setCrop(new CropReed(reed));
		flax.setCrop(new CropFlax(flax));
		cotton.setCrop(new CropCotton(cotton));

		ramie_dry.setUnificationMaterial(ramie);
	}
	
	public static void init(){}
}