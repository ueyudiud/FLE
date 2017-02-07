/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import static farcore.FarCoreRegistry.MATERIAL_REGISTERS;

import farcore.FarCore;
import farcore.lib.crop.CropTemplate;
import farcore.lib.crop.instance.CropCabbage;
import farcore.lib.crop.instance.CropMillet;
import farcore.lib.material.IMaterialRegister;
import farcore.lib.material.Mat;
import farcore.lib.material.MaterialBuilder;
import farcore.lib.material.prop.PropertyEdible;
import farcore.lib.tree.instance.TreeAcacia;
import farcore.lib.tree.instance.TreeAspen;
import farcore.lib.tree.instance.TreeBirch;
import farcore.lib.tree.instance.TreeCeiba;
import farcore.lib.tree.instance.TreeMorus;
import farcore.lib.tree.instance.TreeOak;
import farcore.lib.tree.instance.TreeOakBlack;
import farcore.lib.tree.instance.TreeWillow;
import nebula.Log;
import net.minecraft.block.material.Material;

/**
 * Material set.
 * @author ueyudiud
 *
 */
public class M
{
	//Metal
	public static final Mat Copper			= new MaterialBuilder(1001, FarCore.ID , "copper"			, "Copper"			, "Copper"				).setRGBa(0xFF7656FF).setToolProp( 140, 14, 4.5F, 0.0F, 10.0F, -0.8F).setGeneralProp(3.45E6F, 401, 620, 4830, Float.MAX_VALUE, 16.78E-9F, 0.8F).build();
	//Rocks
	public static final Mat stone 			= new MaterialBuilder(7001, "minecraft", "stone"			, "Stone"			, "Stone"				).setRGBa(0x626262FF).setToolProp(  16,  5, 1.2F, 0.8F,  4.0F, -0.5F).build().setRock( 4, 1.5F,  8.0F);
	public static final Mat compact_stone	= new MaterialBuilder(7002, FarCore.ID , "compactstone"		, "CompactStone"	, "Compact Stone"		).setRGBa(0x686868FF).setToolProp(  22,  6, 1.8F, 0.8F,  4.0F, -0.5F).build().setRock( 5, 2.0F, 12.0F);
	public static final Mat andesite		= new MaterialBuilder(7003, FarCore.ID , "andesite"			, "Andesite"		, "Andesite"			).setRGBa(0x616162FF).setToolProp(  32,  8, 2.3F, 0.8F,  6.0F, -0.5F).build().setRock( 7, 4.9F, 16.6F);
	public static final Mat basalt			= new MaterialBuilder(7004, FarCore.ID , "basalt"			, "Basalt"			, "Basalt"				).setRGBa(0x3A3A3AFF).setToolProp(  38,  8, 2.5F, 0.8F,  4.8F, -0.5F).build().setRock( 7, 5.3F, 18.3F);
	public static final Mat diorite			= new MaterialBuilder(7005, FarCore.ID , "diorite"			, "Diorite"			, "Diorite"				).setRGBa(0xC9C9CDFF).setToolProp(  42, 10, 2.7F, 0.8F,  5.6F, -0.6F).build().setRock( 9, 6.5F, 23.3F);
	public static final Mat gabbro			= new MaterialBuilder(7006, FarCore.ID , "gabbro"			, "Gabbro"			, "Gabbro"				).setRGBa(0x53524EFF).setToolProp(  40, 11, 2.6F, 0.8F,  6.0F, -0.5F).build().setRock(10, 6.9F, 25.2F);
	public static final Mat granite			= new MaterialBuilder(7007, FarCore.ID , "granite"			, "Granite"			, "Granite"				).setRGBa(0x986C5DFF).setToolProp(  44, 11, 2.8F, 0.8F,  7.2F, -0.6F).build().setRock(10, 7.4F, 29.8F);
	public static final Mat kimberlite		= new MaterialBuilder(7008, FarCore.ID , "kimberlite"		, "Kimberlite"		, "Kimberlite"			).setRGBa(0x4D4D49FF).setToolProp(  46, 11, 3.1F, 0.8F,  7.2F, -0.6F).build().setRock(10, 7.8F, 31.4F);
	public static final Mat limestone		= new MaterialBuilder(7009, FarCore.ID , "limestone"		, "Lime"			, "Limestone"			).setRGBa(0xC9C9C8FF)                                                .build().setRock( 4, 1.3F,  5.5F);
	public static final Mat marble			= new MaterialBuilder(7010, FarCore.ID , "marble"			, "Marble"			, "Marble"				).setRGBa(0xE2E6F0FF)                                                .build().setRock( 6, 7.8F,  8.4F);
	public static final Mat netherrack		= new MaterialBuilder(7011, "minecraft", "netherrack"		, "Netherrack"		, "Netherrack"			).setRGBa(0x5F3636FF)                                                .build().setRock( 3, 1.3F,  3.8F);
	public static final Mat obsidian		= new MaterialBuilder(7012, FarCore.ID , "obsidian"			, "Obsidian"		, "Obsidian"			).setRGBa(0x12121BFF).setToolProp(   8, 12, 5.2F, 2.7F, 12.0F,  0.2F).build().setRock(17, 9.8F,  4.2F);
	public static final Mat peridotite		= new MaterialBuilder(7013, FarCore.ID , "peridotite"		, "Peridotite"		, "Peridotite"			).setRGBa(0x717A5CFF).setToolProp(  45, 11, 3.0F, 0.8F,  8.0F, -0.6F).build().setRock(10, 7.7F, 30.5F);
	public static final Mat rhyolite		= new MaterialBuilder(7014, FarCore.ID , "rhyolite"			, "Rhyolite"		, "Rhyolite"			).setRGBa(0x4F535AFF).setToolProp(  39, 10, 2.6F, 0.8F,  8.0F, -0.5F).build().setRock( 9, 6.0F, 21.7F);
	public static final Mat graniteP		= new MaterialBuilder(7015, FarCore.ID , "granite_p"		, "GranitePegmatite", "Granite Pegmatite"	).setRGBa(0x4F535AFF).setToolProp(  45, 11, 2.8F, 0.8F,  7.2F, -0.6F).build().setRock(10, 7.6F, 30.1F);
	public static final Mat whitestone		= new MaterialBuilder(7016, "minecraft", "whitestone"		, "Whitestone"		, "End Stone"			).setRGBa(0xE2E2B5FF)                                                .build().setRock( 8, 6.0F, 14.7F);
	//Soils
	public static final Mat latosol			= new MaterialBuilder(7101, FarCore.ID , "latosol"			, "Latosol"			, "Latosol"				).setRGBa(0x652A1FFF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat latoaluminosol	= new MaterialBuilder(7102, FarCore.ID , "latoaluminosol"	, "Latoaluminosol"	, "Latoaluminosol"		).setRGBa(0x77412FFF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat ruboloam		= new MaterialBuilder(7103, FarCore.ID , "ruboloam"			, "Ruboloam"		, "Ruboloam"			).setRGBa(0x773E22FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat ruboaluminoloam	= new MaterialBuilder(7104, FarCore.ID , "ruboaluminoloam"	, "Ruboaluminoloam"	, "Ruboaluminoloam"		).setRGBa(0x8E5938FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat flavoloam		= new MaterialBuilder(7105, FarCore.ID , "flavoloam"		, "Flavoloam"		, "Flavoloam"			).setRGBa(0x907451FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat peatsol			= new MaterialBuilder(7106, FarCore.ID , "peatsol"			, "Peatsol"			, "Peatsol"				).setRGBa(0x1B1715FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat aterosol		= new MaterialBuilder(7107, FarCore.ID , "aterosol"			, "Aterosol"		, "Aterosol"			).setRGBa(0x1A110EFF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat podzol			= new MaterialBuilder(7108, FarCore.ID , "podzol"			, "Podzol"			, "Podzol"				).setRGBa(0x281812FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat pheosol			= new MaterialBuilder(7109, FarCore.ID , "pheosol"			, "Pheosol"			, "Pheosol"				).setRGBa(0x6C4626FF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	public static final Mat aterocalcosol	= new MaterialBuilder(7110, FarCore.ID , "aterocalcosol"	, "Aterocalcosol"	, "Aterocalcosol"		).setRGBa(0x25211EFF).build().setSoil(0.6F, 3.0F, Material.GROUND);
	//Trees
	public static final Mat oak				= new Mat(8001, "minecraft", "oak", "Oak", "Oak").setWood(5.3F, 1.0F, 20.0F);
	public static final Mat spruce			= new Mat(8002, "minecraft", "spruce", "Spruce", "Spruce").setWood(2.3F, 1.0F, 20.0F);
	public static final Mat birch			= new Mat(8003, "minecraft", "birch", "Birch", "Birch").setWood(4.0F, 1.0F, 20.0F);
	public static final Mat ceiba			= new Mat(8004, "minecraft", "ceiba", "Ceiba", "Ceiba").setWood(1.1F, 1.0F, 20.0F);
	public static final Mat acacia			= new Mat(8005, "minecraft", "acacia", "Acacia", "Acacia").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat oak_black		= new Mat(8006, "minecraft", "oak-black", "DarkOak", "Dark Oak").setWood(5.4F, 1.0F, 20.0F);
	public static final Mat aspen			= new Mat(8011, FarCore.ID, "aspen", "Aspen", "Aspen").setWood(1.6F, 1.0F, 20.0F);
	public static final Mat morus			= new Mat(8012, FarCore.ID, "morus", "Morus", "Morus").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat willow			= new Mat(8013, FarCore.ID, "willow", "Willow", "Willow").setWood(3.0F, 1.0F, 20.0F);
	//Crops
	public static final Mat wheat			= new Mat(9001, FarCore.ID, "wheat", "Wheat", "Wheat");
	public static final Mat millet			= new Mat(9002, FarCore.ID, "millet", "Millet", "Millet");
	public static final Mat soybean			= new Mat(9003, FarCore.ID, "soybean", "Soybean", "Soybean");
	public static final Mat potato			= new Mat(9004, FarCore.ID, "potato", "Potato", "Potato");
	public static final Mat sweet_potato	= new Mat(9005, FarCore.ID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat cabbage			= new Mat(9006, FarCore.ID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat reed			= new Mat(9007, FarCore.ID, "reed", "Reed", "Reed");
	public static final Mat flax			= new Mat(9008, FarCore.ID, "flax", "Flax", "Flax");
	public static final Mat cotton			= new Mat(9009, FarCore.ID, "cotton", "Cotton", "Cotton");
	public static final Mat ramie			= new Mat(9010, FarCore.ID, "ramie", "Ramie", "Ramie");
	//Plants
	public static final Mat vine			= new Mat(9201, FarCore.ID, "vine", "Vine", "Vine").setRGBa(0x867C50FF).setTag(SubTags.ROPE);
	public static final Mat ivy				= new Mat(9202, FarCore.ID, "ivy", "Ivy", "Ivy").setRGBa(0x867C50FF).setTag(SubTags.ROPE);
	public static final Mat rattan			= new Mat(9203, FarCore.ID, "rattan", "Rattan", "Rattan").setRGBa(0x867C50FF).setTag(SubTags.ROPE);
	public static final Mat spider_silk		= new Mat(9204, FarCore.ID, "spider_silk", "SpiderSilk", "Spider's Silk").setRGBa(0xFAFAFAFF).setTag(SubTags.ROPE);
	public static final Mat ramie_dry		= new Mat(9205, FarCore.ID, "ramie_dry", "RamieDry", "Ramie").setRGBa(0xCFC898FF).setTag(SubTags.ROPE);
	//Ores
	public static final Mat native_copper	= new Mat(10001, FarCore.ID, "nativeCopper", "NativeCopper", "Native Copper").setChemicalFormula("Cu").setRGBa(0xFF834CFF).setOreProperty(7, 8.0F, 9.0F);
	public static final Mat malachite		= new Mat(10002, FarCore.ID, "malachite", "Malachite", "Malachite").setChemicalFormula("Cu(OH)2·CuCO3").setRGBa(0x30CE88FF).setOreProperty(8, 8.8F, 9.0F);
	public static final Mat cuprite			= new Mat(10003, FarCore.ID, "cuprite", "Cuprite", "Cuprite").setChemicalFormula("Cu2O").setRGBa(0xD83E26FF).setOreProperty(10, 9.6F, 9.6F);
	public static final Mat azurite			= new Mat(10004, FarCore.ID, "azurite", "Azurite", "Azurite").setChemicalFormula("Cu(OH)2·2(CuCO3)").setRGBa(0x3039CEFF).setOreProperty(10, 9.5F, 9.5F);
	public static final Mat chalcocite		= new Mat(10005, FarCore.ID, "chalcocite", "Chalcocite", "Chalcocite").setChemicalFormula("Cu2S").setRGBa(0x8C96A1FF).setOreProperty(13, 10.8F, 10.2F);
	public static final Mat tenorite		= new Mat(10006, FarCore.ID, "tenorite", "Tenorite", "Tenorite").setChemicalFormula("CuO").setRGBa(0x4E5A63FF).setOreProperty(12, 10.2F, 9.7F);
	public static final Mat chalcopyrite	= new Mat(10007, FarCore.ID, "chalcopyrite", "Chalcopyrite", "Chalcopyrite").setChemicalFormula("CuFeS2").setRGBa(0xF3DA8AFF).setOreProperty(10, 9.0F, 9.6F);
	public static final Mat bornite			= new Mat(10008, FarCore.ID, "bornite", "Bornite", "Bornite").setChemicalFormula("Cu5FeS4").setRGBa(0xD3BA78FF).setOreProperty(14, 13.4F, 11.4F);
	public static final Mat covellite		= new Mat(10009, FarCore.ID, "covellite", "Covellite", "Covellite").setChemicalFormula("CuS").setRGBa(0x0B69D2FF).setOreProperty(20, 18.4F, 14.8F);
	public static final Mat tetrahedrite	= new Mat(10010, FarCore.ID, "tetrahedrite", "Tetrahedrite", "Tetrahedrite").setChemicalFormula("[Cu,Fe]12Sb4S13").setRGBa(0x999782FF).setOreProperty(21, 19.2F, 15.0F);
	public static final Mat argentite		= new Mat(10011, FarCore.ID, "argentite", "Argentite", "Argentite").setChemicalFormula("Ag2S").setRGBa(0x535455FF).setOreProperty(17, 15.4F, 12.8F);
	public static final Mat pyrargyrite		= new Mat(10012, FarCore.ID, "pyrargyrite", "Pyrargyrite", "Pyrargyrite").setChemicalFormula("Ag3SbS3").setRGBa(0x70483EFF).setOreProperty(18, 16.0F, 13.2F);
	public static final Mat enargite		= new Mat(10013, FarCore.ID, "enargite", "Enargite", "Enargite").setChemicalFormula("Cu3AsS4").setRGBa(0x705A59FF).setOreProperty(13, 12.4F, 13.9F);
	public static final Mat realgar			= new Mat(10014, FarCore.ID, "realgar", "Realgar", "Realgar").setChemicalFormula("As4S4").setRGBa(0xEF2B28FF).setOreProperty(8, 8.4F, 9.1F);
	public static final Mat orpiment		= new Mat(10015, FarCore.ID, "orpiment", "Orpiment", "Orpiment").setChemicalFormula("As2S3").setRGBa(0xE6BB45FF).setOreProperty(9, 8.8F, 9.4F);
	public static final Mat arsenolite		= new Mat(10016, FarCore.ID, "arsenolite", "Arsenolite", "Arsenolite").setChemicalFormula("As4O6").setRGBa(0x949960FF).setOreProperty(16, 14.6F, 14.0F);
	public static final Mat nickeline		= new Mat(10017, FarCore.ID, "nickeline", "Nickeline", "Nickeline").setChemicalFormula("NiAs").setRGBa(0xB2A76AFF).setOreProperty(31, 27.9F, 17.3F);
	public static final Mat pyrite			= new Mat(10018, FarCore.ID, "pyrite", "Pyrite", "Pyrite").setChemicalFormula("FeS2").setRGBa(0xDBCEA0FF).setOreProperty(16, 14.8F, 14.2F);
	public static final Mat arsenopyrite	= new Mat(10019, FarCore.ID, "arsenopyrite", "Arsenopyrite", "Arsenopyrite").setChemicalFormula("FeAsS").setRGBa(0xA3A29AFF).setOreProperty(17, 15.2F, 13.9F);
	public static final Mat scorodite		= new Mat(10020, FarCore.ID, "scorodite", "Scorodite", "Scorodite").setChemicalFormula("FeAsO4·2(H2O)").setRGBa(0x32538AFF).setOreProperty(25, 22.3F, 15.8F);
	public static final Mat erythrite		= new Mat(10021, FarCore.ID, "erythrite", "Erythrite", "Erythrite").setChemicalFormula("Co3(AsO4)2·8(H2O)").setRGBa(0xA9355EFF).setOreProperty(37, 32.1F, 19.3F);
	public static final Mat domeykite		= new Mat(10022, FarCore.ID, "domeykite", "Domeykite", "Domeykite").setChemicalFormula("Cu3As").setRGBa(0xA28261FF).setOreProperty(14, 13.5F, 11.8F);
	public static final Mat cassiterite		= new Mat(10023, FarCore.ID, "cassiterite", "Cassiterite", "Cassiterite").setChemicalFormula("SnO2").setRGBa(0xBCB8A5FF).setOreProperty(13, 12.6F, 10.5F);
	public static final Mat gelenite		= new Mat(10024, FarCore.ID, "gelenite", "Gelenite", "Gelenite").setChemicalFormula("PbS").setRGBa(0x6F7280FF).setOreProperty(11, 10.8F, 9.3F);
	public static final Mat stannite		= new Mat(10025, FarCore.ID, "stannite", "Stannite", "Stannite").setChemicalFormula("Cu2FeSnS4").setRGBa(0xB5B4A2FF).setOreProperty(17, 16.0F, 13.2F);
	public static final Mat sphalerite		= new Mat(10026, FarCore.ID, "sphalerite", "Sphalerite", "Sphalerite").setChemicalFormula("ZnS").setRGBa(0xB2A7A5FF).setOreProperty(22, 19.8F, 15.5F);
	public static final Mat teallite		= new Mat(10027, FarCore.ID, "teallite", "Teallite", "Teallite").setChemicalFormula("PbSnS2").setRGBa(0xCECECEFF).setOreProperty(23, 20.3F, 15.9F);
	public static final Mat wolframite		= new Mat(10028, FarCore.ID, "wolframite", "Wolframite", "Wolframite").setChemicalFormula("[Fe,Mn]WO4").setRGBa(0x2F2F2FFF).setOreProperty(40, 34.2F, 20.5F);
	public static final Mat scheelite		= new Mat(10029, FarCore.ID, "scheelite", "Scheelite", "Scheelite").setChemicalFormula("CaWO4").setRGBa(0xBD975AFF).setOreProperty(42, 35.7F, 21.8F);
	public static final Mat bismuthinite	= new Mat(10030, FarCore.ID, "bismuthinite", "Bismuthinite", "Bismuthinite").setChemicalFormula("Bi2S3").setRGBa(0x434A43FF).setOreProperty(29, 26.2F, 16.4F);
	public static final Mat native_silver	= new Mat(10031, FarCore.ID, "nativeSilver", "NativeSilver", "Native Silver").setChemicalFormula("Ag").setRGBa(0xEBE9E8FF).setOreProperty(13, 11.2F, 12.9F, SubTags.ORE_NOBLE);
	public static final Mat native_gold		= new Mat(10032, FarCore.ID, "nativeGold", "NativeGold", "Native Gold").setChemicalFormula("Au").setRGBa(0xF7B32AFF).setOreProperty(5, 6.8F, 8.3F, SubTags.ORE_NOBLE);
	public static final Mat electrum		= new Mat(10033, FarCore.ID, "electrum", "Electrum", "Electrum").setChemicalFormula("?").setRGBa(0xE4B258FF).setOreProperty(11, 8.2F, 9.2F, SubTags.ORE_NOBLE);
	
	static
	{
		SubTags.HANDLE.addTo(oak, spruce, birch, ceiba, acacia, oak_black, aspen, morus, willow);
		
		oak			.setTree(new TreeOak());
		spruce		.setTree(new TreeBirch());
		birch		.setTree(new TreeBirch());
		ceiba		.setTree(new TreeCeiba());
		acacia		.setTree(new TreeAcacia());
		oak_black	.setTree(new TreeOakBlack());
		aspen		.setTree(new TreeAspen());
		morus		.setTree(new TreeMorus());
		willow		.setTree(new TreeWillow());
		
		wheat		.setCrop(new CropTemplate(wheat, 8, 800).setMultiplicationProp(6, 5, 2).setDNAHelper(DNAs.get('w'), DNAs.GRAIN_III, DNAs.GROWTH_II, DNAs.GRAIN_II, DNAs.get('w'), DNAs.GROWTH_IV));
		millet		.setCrop(new CropMillet(millet));
		soybean		.setCrop(new CropTemplate(soybean, 7, 1600).setMultiplicationProp(5, 5, 2).setDNAHelper(DNAs.get('p')));
		potato		.setCrop(new CropTemplate(potato, 7, 1300).setDNAHelper());
		sweet_potato.setCrop(new CropTemplate(sweet_potato, 6, 1500).setDNAHelper());
		cabbage		.setCrop(new CropCabbage(cabbage));
		reed		.setCrop(new CropTemplate(reed, 6, 1400));
		flax		.setCrop(new CropTemplate(flax, 6, 1500));
		cotton		.setCrop(new CropTemplate(cotton, 6, 1400));
		
		ramie_dry	.setUnificationMaterial(ramie);
		
		potato      .addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[]{3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F}));
		sweet_potato.addProperty(MP.property_edible, new PropertyEdible(2.0F, 0.5F, 0.0F, new float[]{3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F}));
		reed        .addProperty(MP.property_edible, new PropertyEdible(0.0F, 2.0F, 3.0F, new float[]{0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F}));
	}
	
	public static void init()
	{
		for(IMaterialRegister register : MATERIAL_REGISTERS)
		{
			try
			{
				register.registerMaterials();
			}
			catch (Exception exception)
			{
				Log.warn("Fail to register materials.", exception);
			}
		}
	}
}