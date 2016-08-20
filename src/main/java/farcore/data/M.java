package farcore.data;

import farcore.FarCore;
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
import farcore.lib.tree.TreeVoid;
import farcore.lib.tree.instance.TreeAcacia;
import farcore.lib.tree.instance.TreeAspen;
import farcore.lib.tree.instance.TreeBirch;
import farcore.lib.tree.instance.TreeCeiba;
import farcore.lib.tree.instance.TreeMorus;
import farcore.lib.tree.instance.TreeOak;
import farcore.lib.tree.instance.TreeOakBlack;
import farcore.lib.tree.instance.TreeWillow;

public class M
{
	public static final Mat VOID = new Mat(-1, false, "farcore", "void", "Void", "Void").setCrop(ICrop.VOID);

	public static final Mat stone = new Mat(7001, "minecraft", "stone", "Stone", "Stone").setRock(4, 1.5F, 8F, 20).setToolable(5, 16, 1.2F, 0.8F, 1.0F, 8).setRGBa(0x626262FF);
	public static final Mat compact_stone = new Mat(7002, "farcore", "stone-compact", "CompactStone", "Compact Stone").setRock(5, 2.0F, 12F, 20).setToolable(6, 22, 1.8F, 0.7F, 1.0F, 6).setRGBa(0x686868FF);
	public static final Mat andesite = new Mat(7003, "farcore", "andesite", "Andesite", "Andesite").setRock(7, 4.9F, 16.6F, 20).setToolable(8, 32, 2.3F, 0.8F, 1.5F, 8).setRGBa(0x616162FF);
	public static final Mat basalt = new Mat(7004, "farcore", "basalt", "Basalt", "Basalt").setRock(7, 5.3F, 18.3F, 20).setToolable(8, 38, 2.5F, 0.8F, 1.2F, 8).setRGBa(0x3A3A3AFF);
	public static final Mat diorite = new Mat(7005, "farcore", "diorite", "Diorite", "Diorite").setRock(9, 6.5F, 23.3F, 20).setToolable(10, 42, 2.7F, 0.8F, 1.4F, 8).setRGBa(0xC9C9CDFF);
	public static final Mat gabbro = new Mat(7006, "farcore", "gabbro", "Gabbro", "Gabbro").setRock(10, 6.9F, 25.2F, 20).setToolable(11, 40, 2.6F, 0.8F, 1.5F, 6).setRGBa(0x53524EFF);
	public static final Mat granite = new Mat(7007, "farcore", "granite", "Granite", "Granite").setRock(10, 7.4F, 29.8F, 20).setToolable(11, 44, 2.8F, 0.8F, 1.8F, 4).setRGBa(0x986C5DFF);
	public static final Mat kimberlite = new Mat(7008, "farcore", "kimberlite", "Kimberlite", "Kimberlite").setRock(10, 7.8F, 31.4F, 20).setToolable(11, 46, 3.1F, 0.8F, 1.8F, 4).setRGBa(0x4D4D49FF);
	public static final Mat limestone = new Mat(7009, "farcore", "limestone", "Lime", "Limestone").setRock(4, 1.3F, 5.5F, 20).setRGBa(0xC9C9C8FF);
	public static final Mat marble = new Mat(7010, "farcore", "marble", "Marble", "Marble").setRock(6, 7.8F, 8.4F, 20).setRGBa(0xE2E6F0FF);
	public static final Mat netherrack = new Mat(7011, "minecraft", "netherrack", "Netherrack", "Netherrack").setRock(3, 1.3F, 3.8F, 180).setRGBa(0x5F3636FF);
	public static final Mat obsidian = new Mat(7012, "farcore", "obsidian", "Obsidian", "Obsidian").setRock(17, 9.8F, 4.2F, 20).setToolable(12, 8, 5.2F, 2.7F, 3F, 12).setRGBa(0x12121BFF);
	public static final Mat peridotite = new Mat(7013, "farcore", "peridotite", "Peridotite", "Peridotite").setRock(10, 7.7F, 30.5F, 20).setToolable(11, 45, 3.0F, 0.8F, 2.0F, 5).setRGBa(0x717A5CFF);
	public static final Mat rhyolite = new Mat(7014, "farcore", "rhyolite", "Rhyolite", "Rhyolite").setRock(9, 6.0F, 21.7F, 20).setToolable(10, 39, 2.6F, 0.8F, 2.0F, 8).setRGBa(0x4F535AFF);
	public static final Mat graniteP = new Mat(7015, "farcore", "graniteP", "GranitePegmatite", "Granite Pegmatite").setRock(10, 7.6F, 30.1F, 20).setToolable(11, 45, 2.8F, 0.8F, 1.8F, 6).setRGBa(0x4F535AFF);

	public static final Mat oak = new Mat(8001, "minecraft", "oak", "Oak", "Oak").setWood(5.3F, 1.0F, 20.0F);
	public static final Mat spruce = new Mat(8002, "minecraft", "spruce", "Spruce", "Spruce").setWood(2.3F, 1.0F, 20.0F);
	public static final Mat birch = new Mat(8003, "minecraft", "birch", "Birch", "Birch").setWood(4.0F, 1.0F, 20.0F);
	public static final Mat ceiba = new Mat(8004, "minecraft", "ceiba", "Ceiba", "Ceiba").setWood(1.1F, 1.0F, 20.0F);
	public static final Mat acacia = new Mat(8005, "minecraft", "acacia", "Acacia", "Acacia").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat oak_black = new Mat(8006, "minecraft", "oak-black", "DarkOak", "Dark Oak").setWood(5.4F, 1.0F, 20.0F);

	public static final Mat aspen = new Mat(8011, FarCore.ID, "aspen", "Aspen", "Aspen").setWood(1.6F, 1.0F, 20.0F);
	public static final Mat morus = new Mat(8012, FarCore.ID, "morus", "Morus", "Morus").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat willow = new Mat(8013, FarCore.ID, "willow", "Willow", "Willow").setWood(3.0F, 1.0F, 20.0F);

	public static final Mat wheat = new Mat(9001, FarCore.ID, "wheat", "Wheat", "Wheat");
	public static final Mat millet = new Mat(9002, FarCore.ID, "millet", "Millet", "Millet");
	public static final Mat soybean = new Mat(9003, FarCore.ID, "soybean", "Soybean", "Soybean");
	public static final Mat potato = new Mat(9004, FarCore.ID, "potato", "Potato", "Potato");
	public static final Mat sweet_potato = new Mat(9005, FarCore.ID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat cabbage = new Mat(9006, FarCore.ID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat reed = new Mat(9007, FarCore.ID, "reed", "Reed", "Reed");
	public static final Mat flax = new Mat(9008, FarCore.ID, "flax", "Flax", "Flax");
	public static final Mat cotton = new Mat(9009, FarCore.ID, "cotton", "Cotton", "Cotton");
	
	public static final Mat native_copper = new Mat(10001, "farcore", "nativeCopper", "NativeCopper", "Native Copper").setChemicalFormula("Cu").setOreProperty(7, 8.0F, 9.0F);
	public static final Mat malachite = new Mat(10002, "farcore", "malachite", "Malachite", "Malachite").setChemicalFormula("Cu(OH)2·CuCO3").setOreProperty(8, 8.8F, 9.0F);
	public static final Mat cuprite = new Mat(10003, "farcore", "cuprite", "Cuprite", "Cuprite").setChemicalFormula("Cu2O").setOreProperty(10, 9.6F, 9.6F);
	public static final Mat azurite = new Mat(10004, "farcore", "azurite", "Azurite", "Azurite").setChemicalFormula("Cu(OH)2·2(CuCO3)").setOreProperty(10, 9.5F, 9.5F);
	public static final Mat chalcocite = new Mat(10005, "farcore", "chalcocite", "Chalcocite", "Chalcocite").setChemicalFormula("Cu2S").setOreProperty(13, 10.8F, 10.2F);
	public static final Mat tenorite = new Mat(10006, "farcore", "tenorite", "Tenorite", "Tenorite").setChemicalFormula("CuO").setOreProperty(12, 10.2F, 9.7F);
	public static final Mat chalcopyrite = new Mat(10007, "farcore", "chalcopyrite", "Chalcopyrite", "Chalcopyrite").setChemicalFormula("CuFeS2").setOreProperty(10, 9.0F, 9.6F);
	public static final Mat bornite = new Mat(10008, "farcore", "bornite", "Bornite", "Bornite").setChemicalFormula("Cu5FeS4").setOreProperty(14, 13.4F, 11.4F);
	public static final Mat covellite = new Mat(10009, "farcore", "covellite", "Covellite", "Covellite").setChemicalFormula("CuS").setOreProperty(20, 18.4F, 14.8F);
	public static final Mat tetrahedrite = new Mat(10010, "farcore", "tetrahedrite", "Tetrahedrite", "Tetrahedrite").setChemicalFormula("[Cu,Fe]12Sb4S13").setOreProperty(21, 19.2F, 15.0F);
	public static final Mat argentite = new Mat(10011, "farcore", "argentite", "Argentite", "Argentite").setChemicalFormula("Ag2S").setOreProperty(17, 15.4F, 12.8F);
	public static final Mat pyrargyrite = new Mat(10012, "farcore", "pyrargyrite", "Pyrargyrite", "Pyrargyrite").setChemicalFormula("Ag3SbS3").setOreProperty(18, 16.0F, 13.2F);
	public static final Mat enargite = new Mat(10013, "farcore", "enargite", "Enargite", "Enargite").setChemicalFormula("Cu3AsS4").setOreProperty(13, 12.4F, 13.9F);
	public static final Mat realgar = new Mat(10014, "farcore", "realgar", "Realgar", "Realgar").setChemicalFormula("As4S4").setOreProperty(8, 8.4F, 9.1F);
	public static final Mat orpiment = new Mat(10015, "farcore", "orpiment", "Orpiment", "Orpiment").setChemicalFormula("As2S3").setOreProperty(9, 8.8F, 9.4F);
	public static final Mat arsenolite = new Mat(10016, "farcore", "arsenolite", "Arsenolite", "Arsenolite").setChemicalFormula("As4O6").setOreProperty(16, 14.6F, 14.0F);
	public static final Mat nickeline = new Mat(10017, "farcore", "nickeline", "Nickeline", "Nickeline").setChemicalFormula("NiAs").setOreProperty(31, 27.9F, 17.3F);
	public static final Mat pyrite = new Mat(10018, "farcore", "pyrite", "Pyrite", "Pyrite").setChemicalFormula("FeS2").setOreProperty(16, 14.8F, 14.2F);
	public static final Mat arsenopyrite = new Mat(10019, "farcore", "arsenopyrite", "Arsenopyrite", "Arsenopyrite").setChemicalFormula("FeAsS").setOreProperty(17, 15.2F, 13.9F);
	public static final Mat scorodite = new Mat(10020, "farcore", "scorodite", "Scorodite", "Scorodite").setChemicalFormula("FeAsO4·2(H2O)").setOreProperty(25, 22.3F, 15.8F);
	public static final Mat erythrite = new Mat(10021, "farcore", "erythrite", "Erythrite", "Erythrite").setChemicalFormula("Co3(AsO4)2·8(H2O)").setOreProperty(37, 32.1F, 19.3F);
	public static final Mat domeykite = new Mat(10022, "farcore", "domeykite", "Domeykite", "Domeykite").setChemicalFormula("Cu3As").setOreProperty(14, 13.5F, 11.8F);
	public static final Mat cassiterite = new Mat(10023, "farcore", "cassiterite", "Cassiterite", "Cassiterite").setChemicalFormula("SnO2").setOreProperty(13, 12.6F, 10.5F);
	public static final Mat gelenite = new Mat(10024, "farcore", "gelenite", "Gelenite", "Gelenite").setChemicalFormula("PbS").setOreProperty(11, 10.8F, 9.3F);
	public static final Mat stannite = new Mat(10025, "farcore", "stannite", "Stannite", "Stannite").setChemicalFormula("Cu2FeSnS4").setOreProperty(17, 16.0F, 13.2F);
	public static final Mat sphalerite = new Mat(10026, "farcore", "sphalerite", "Sphalerite", "Sphalerite").setChemicalFormula("ZnS").setOreProperty(22, 19.8F, 15.5F);
	public static final Mat teallite = new Mat(10027, "farcore", "teallite", "Teallite", "Teallite").setChemicalFormula("PbSnS2").setOreProperty(23, 20.3F, 15.9F);
	public static final Mat wolframite = new Mat(10028, "farcore", "wolframite", "Wolframite", "Wolframite").setChemicalFormula("[Fe,Mn]WO4").setOreProperty(40, 34.2F, 20.5F);
	public static final Mat scheelite = new Mat(10029, "farcore", "scheelite", "Scheelite", "Scheelite").setChemicalFormula("CaWO4").setOreProperty(42, 35.7F, 21.8F);
	public static final Mat bismuthinite = new Mat(10030, "farcore", "bismuthinite", "Bismuthinite", "Bismuthinite").setChemicalFormula("Bi2S3").setOreProperty(29, 26.2F, 16.4F);
	
	static
	{
		VOID.setTree(new TreeVoid(), false);

		oak.setTree(new TreeOak(oak));
		spruce.setTree(new TreeBirch(spruce));
		birch.setTree(new TreeBirch(birch));
		ceiba.setTree(new TreeCeiba(ceiba));
		acacia.setTree(new TreeAcacia(acacia));
		oak_black.setTree(new TreeOakBlack(oak_black));
		aspen.setTree(new TreeAspen(aspen));
		morus.setTree(new TreeMorus(morus));
		willow.setTree(new TreeWillow(willow));

		wheat.setCrop(new CropWheat(wheat));
		millet.setCrop(new CropMillet(millet));
		soybean.setCrop(new CropSoybean(soybean));
		potato.setCrop(new CropPotato(potato));
		sweet_potato.setCrop(new CropSweetPotato(sweet_potato));
		cabbage.setCrop(new CropCabbage(cabbage));
		reed.setCrop(new CropReed(reed));
		flax.setCrop(new CropFlax(flax));
		cotton.setCrop(new CropCotton(cotton));
	}

	public static void init(){}
}