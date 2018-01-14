/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import static farcore.FarCoreRegistry.MATERIAL_REGISTERS;
import static farcore.data.MC.axe_rock;
import static farcore.data.MC.block;
import static farcore.data.MC.brick;
import static farcore.data.MC.brickBlock;
import static farcore.data.MC.chip_rock;
import static farcore.data.MC.fragment;
import static farcore.data.MC.pile_purified;

import farcore.FarCore;
import farcore.lib.block.behavior.RockBehaviorFlammable;
import farcore.lib.block.behavior.RockBehaviorNetherrack;
import farcore.lib.material.Mat;
import farcore.lib.material.behavior.metal.MatBehaviorCopper;
import farcore.lib.plant.PlantBristlegrass;
import farcore.lib.plant.PlantDandelion;
import farcore.lib.plant.PlantHalogrootBush;
import farcore.lib.plant.PlantStatic;
import nebula.Log;

/**
 * Material list.
 * 
 * @author ueyudiud
 */
//format:off
public class M
{
	// Water
	public static final Mat	water		= new Mat(401, FarCore.ID, "water", "SaltyWater", "Salty Water");
	public static final Mat	fresh_water	= new Mat(402, FarCore.ID, "fresh_water", "FreshWater", "Fresh Water");
	public static final Mat	salty_water	= new Mat(403, FarCore.ID, "salty_water", "SaltyWater", "Salty Water");
	public static final Mat	rain_water	= new Mat(404, FarCore.ID, "rain_water", "RainWater", "Rain Water");
	public static final Mat	muddy_water	= new Mat(405, FarCore.ID, "muddy_water", "MuddyWater", "Muddy Water");
	// Metal
	public static final Mat copper = new Mat(1001, FarCore.ID, "copper", "Copper", "Copper").builder().setRGBa(0xFF7656FF).setToolProp(140, 14, 4.5F, 0.0F, 10.0F, -0.8F).setGeneralProp(3.45E4F, 401, 620, 4830, Float.MAX_VALUE, 16.78E-9F, 0.8F).setMetalic(12, 4.0F, 8.0F).build();
	// Mob Resources
	public static final Mat spider_silk = new Mat(6001, FarCore.ID, "spider_silk", "SpiderSilk", "Spider's Silk").builder().setRGBa(0xFAFAFAFF).build();
	// Rocks
	public static final Mat	stone				= new Mat(7001, "minecraft", "stone", "Stone", "Stone").builder().setRGBa(0x626262FF).setToolProp(16, 5, 1.2F, 0.8F, 4.0F, -0.5F).setRock(4, 1.5F, 8.0F).setGeneralProp(6.48E4F, 2.4F, 372, 628, 5.0F, 1.8E6F, 2.5F).build();
	public static final Mat	compact_stone		= new Mat(7002, FarCore.ID, "compactstone", "CompactStone", "Compact Stone").builder().setRGBa(0x686868FF).setToolProp(22, 6, 1.8F, 0.8F, 4.0F, -0.5F).setRock(5, 2.0F, 12.0F).setGeneralProp(7.18E4F, 3.1F, 372, 674, 6.0F, 1.7E6F, 2.8F).build();
	public static final Mat	andesite			= new Mat(7003, FarCore.ID, "andesite", "Andesite", "Andesite").builder().setRGBa(0x616162FF).setToolProp(32, 8, 2.3F, 0.8F, 6.0F, -0.5F).setRock(7, 4.9F, 16.6F).build();
	public static final Mat	basalt				= new Mat(7004, FarCore.ID, "basalt", "Basalt", "Basalt").builder().setRGBa(0x3A3A3AFF).setToolProp(38, 8, 2.5F, 0.8F, 4.8F, -0.5F).setRock(7, 5.3F, 18.3F).build();
	public static final Mat	diorite				= new Mat(7005, FarCore.ID, "diorite", "Diorite", "Diorite").builder().setRGBa(0xC9C9CDFF).setToolProp(42, 10, 2.7F, 0.8F, 5.6F, -0.6F).setRock(9, 6.5F, 23.3F).build();
	public static final Mat	gabbro				= new Mat(7006, FarCore.ID, "gabbro", "Gabbro", "Gabbro").builder().setRGBa(0x53524EFF).setToolProp(40, 11, 2.6F, 0.8F, 6.0F, -0.5F).setRock(10, 6.9F, 25.2F).build();
	public static final Mat	granite				= new Mat(7007, FarCore.ID, "granite", "Granite", "Granite").builder().setRGBa(0x986C5DFF).setToolProp(44, 11, 2.8F, 0.8F, 7.2F, -0.6F).setRock(10, 7.4F, 29.8F).build();
	public static final Mat	kimberlite			= new Mat(7008, FarCore.ID, "kimberlite", "Kimberlite", "Kimberlite").builder().setRGBa(0x4D4D49FF).setToolProp(46, 11, 3.1F, 0.8F, 7.2F, -0.6F).setRock(10, 7.8F, 31.4F).build();
	public static final Mat	limestone			= new Mat(7009, FarCore.ID, "limestone", "Lime", "Limestone").builder().setRGBa(0xC9C9C8FF).setRock(4, 1.3F, 5.5F).build();
	public static final Mat	marble				= new Mat(7010, FarCore.ID, "marble", "Marble", "Marble").builder().setRGBa(0xE2E6F0FF).setRock(6, 7.8F, 8.4F).build();
	public static final Mat	netherrack			= new Mat(7011, "minecraft", "netherrack", "Netherrack", "Netherrack").builder().setRGBa(0x5F3636FF).build();
	public static final Mat	obsidian			= new Mat(7012, FarCore.ID, "obsidian", "Obsidian", "Obsidian").builder().setRGBa(0x12121BFF).setToolProp(8, 12, 5.2F, 2.7F, 12.0F, 0.2F).setRock(17, 9.8F, 4.2F).build();
	public static final Mat	peridotite			= new Mat(7013, FarCore.ID, "peridotite", "Peridotite", "Peridotite").builder().setRGBa(0x717A5CFF).setToolProp(45, 11, 3.0F, 0.8F, 8.0F, -0.6F).setRock(10, 7.7F, 30.5F).build();
	public static final Mat	rhyolite			= new Mat(7014, FarCore.ID, "rhyolite", "Rhyolite", "Rhyolite").builder().setRGBa(0x4F535AFF).setToolProp(39, 10, 2.6F, 0.8F, 8.0F, -0.5F).setRock(9, 6.0F, 21.7F).build();
	public static final Mat	graniteP			= new Mat(7015, FarCore.ID, "granite_p", "GranitePegmatite", "Granite Pegmatite").builder().setRGBa(0x4F535AFF).setToolProp(45, 11, 2.8F, 0.8F, 7.2F, -0.6F).setRock(10, 7.6F, 30.1F).build();
	public static final Mat	whitestone			= new Mat(7016, "minecraft", "whitestone", "Whitestone", "End Stone").builder().setRGBa(0xE2E2B5FF).setRock(8, 6.0F, 14.7F).build();
	public static final Mat	bituminous_coal		= new Mat(7017, FarCore.ID, "bituminous_coal", "BituminousCoal", "Bituminous Coal").builder().setRGBa(0x1C1C1CFF).build();
	public static final Mat	lignite				= new Mat(7018, FarCore.ID, "lignite", "Lignite", "Lignite").builder().setRGBa(0x211E1DFF).build();
	public static final Mat	travertine			= new Mat(7019, FarCore.ID, "travertine", "Travertine", "Travertine").builder().setRGBa(0xE0E8EFFF).setRock(5, 2.4F, 6.2F).build();
	public static final Mat	brown_sandstone		= new Mat(7020, FarCore.ID, "brown_sandstone", "BrownSandstone", "Brown Sandstone").builder().setRGBa(0xAF7D54FF).setRock(3, 2.0F, 5.7F).build();
	public static final Mat	chalk				= new Mat(7021, FarCore.ID, "chalk", "Chalk", "Chalk").builder().setRGBa(0xE9EAEAFF).setRock(4, 2.4F, 6.6F).build();
	public static final Mat	chert				= new Mat(7022, FarCore.ID, "chert", "Chert", "Chert").builder().setRGBa(0x7F7D7CFF).setRock(5, 2.4F, 6.4F).build();
	public static final Mat	conglomerate		= new Mat(7023, FarCore.ID, "conglomerate", "Conglomerate", "Conglomerate").builder().setRGBa(0xB0ADA2FF).setRock(6, 2.4F, 6.3F).build();
	public static final Mat	dolostone			= new Mat(7024, FarCore.ID, "dolostone", "Dolostone", "Dolostone").builder().setRGBa(0xA39C97FF).setRock(6, 2.4F, 6.3F).build();
	public static final Mat	ocherous_sandstone	= new Mat(7025, FarCore.ID, "ocherous_sandstone", "OcherousSandstone", "Ocherous Sandstone").builder().setRGBa(0xAD906AFF).setRock(3, 2.0F, 5.6F).build();
	public static final Mat	oil_shale			= new Mat(7026, FarCore.ID, "oil_shale", "OilShale", "Oil Shale").builder().setRGBa(0x8E9499FF).setRock(5, 2.8F, 6.9F).build();
	public static final Mat	shale				= new Mat(7027, FarCore.ID, "shale", "Shale", "Shale").builder().setRGBa(0x8E9499FF).setRock(5, 2.8F, 7.0F).build();
	public static final Mat	quartzite			= new Mat(7028, FarCore.ID, "quartzite", "Quartzite", "Quartzite").builder().setRGBa(0xF6F6F6FF).setToolProp(27, 12, 4.5F, 1.0F, 8.0F, -0.5F).setRock(11, 6.4F, 23.4F).build();
	public static final Mat	pumice				= new Mat(7029, FarCore.ID, "pumice", "Pumice", "Pumice").builder().setRGBa(0xBDBBA7FF).setRock(3, 1.9F, 2.7F).build();
	public static final Mat	radiolarite			= new Mat(7030, FarCore.ID, "radiolarite", "Radiolarite", "Radiolarite").builder().setRGBa(0x968D83FF).setToolProp(14, 10, 5.0F, 1.0F, 6.6F, -0.3F).setRock(9, 2.5F, 4.5F).build();
	public static final Mat	diorite_p			= new Mat(7031, FarCore.ID, "diorite_p", "DioritePegmatite", "Diorite Pegmatite").builder().setRGBa(0xBEC6C1FF).setToolProp(44, 10, 2.7F, 0.8F, 5.6F, -0.6F).setRock(9, 6.8F, 23.7F).build();
	public static final Mat	lapis_lazuli		= new Mat(7032, FarCore.ID, "lapis_lazuli", "LapisLazuli", "Lapis Lazuli").builder().setRGBa(0x272D82FF).setToolProp(31, 8, 2.6F, 0.8F, 6.0F, -0.5F).setRock(9, 7.0F, 19.6F).build();
	// Soils
	public static final Mat	latrosol			= new Mat(7101, FarCore.ID, "latrosol", "Latrosol", "Latrosol").builder().setRGBa(0x652A1FFF).setSoil(0.6F, 3.0F).build();
	public static final Mat	latroaluminosol		= new Mat(7102, FarCore.ID, "latroaluminosol", "Latroaluminosol", "Latroaluminosol").builder().setRGBa(0x77412FFF).setSoil(0.6F, 3.0F).build();
	public static final Mat	ruboloam			= new Mat(7103, FarCore.ID, "ruboloam", "Ruboloam", "Ruboloam").builder().setRGBa(0x773E22FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	ruboaluminoloam		= new Mat(7104, FarCore.ID, "ruboaluminoloam", "Ruboaluminoloam", "Ruboaluminoloam").builder().setRGBa(0x8E5938FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	flavoloam			= new Mat(7105, FarCore.ID, "flavoloam", "Flavoloam", "Flavoloam").builder().setRGBa(0x907451FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	peatsol				= new Mat(7106, FarCore.ID, "peatsol", "Peatsol", "Peatsol").builder().setRGBa(0x1B1715FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	aterosol			= new Mat(7107, FarCore.ID, "aterosol", "Aterosol", "Aterosol").builder().setRGBa(0x1A110EFF).setSoil(0.6F, 3.0F).build();
	public static final Mat	podzol				= new Mat(7108, FarCore.ID, "podzol", "Podzol", "Podzol").builder().setRGBa(0x281812FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	pheosol				= new Mat(7109, FarCore.ID, "pheosol", "Pheosol", "Pheosol").builder().setRGBa(0x6C4626FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	aterocalcosol		= new Mat(7110, FarCore.ID, "aterocalcosol", "Aterocalcosol", "Aterocalcosol").builder().setRGBa(0x25211EFF).setSoil(0.6F, 3.0F).build();
	public static final Mat	spaticocalcosol		= new Mat(7111, FarCore.ID, "spaticocalcosol", "Spaticocalcosol", "Spaticocalcosol").builder().setRGBa(0x3C3123FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	brunnocalcosol		= new Mat(7112, FarCore.ID, "brunnocalcosol", "Brunnocalcosol", "Brunnocalcosol").builder().setRGBa(0xAA9770FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	brunnodesertosol	= new Mat(7113, FarCore.ID, "brunnodesertosol", "Brunnodesertosol", "Brunnodesertosol").builder().setRGBa(0xACA495FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	cinerodesertosol	= new Mat(7114, FarCore.ID, "cinerodesertosol", "Cinerodesertosol", "Cinerodesertosol").builder().setRGBa(0xA3A098FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	tundrosol			= new Mat(7115, FarCore.ID, "tundrosol", "Tundrosol", "Tundrosol").builder().setRGBa(0x97948FFF).setSoil(0.6F, 3.0F).build();
	public static final Mat	moraine				= new Mat(7116, FarCore.ID, "moraine", "Moraine", "Moraine").builder().setRGBa(0x959391FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	flavobrunnoloam		= new Mat(7117, FarCore.ID, "flavobrunnoloam", "Flavobrunnoloam", "Flavobrunnoloam").builder().setRGBa(0x7C6242FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	brunnoloam			= new Mat(7118, FarCore.ID, "brunnoloam", "Brunnoloam", "Brunnoloam").builder().setRGBa(0x765F43FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	aterobrunnoloam		= new Mat(7119, FarCore.ID, "aterobrunnoloam", "Aterobrunnoloam", "Aterobrunnoloam").builder().setRGBa(0x433B30FF).setSoil(0.6F, 3.0F).build();
	public static final Mat	palosol				= new Mat(7120, FarCore.ID, "palosol", "Palosol", "Palosol").builder().setRGBa(0x948863FF).setSoil(0.6F, 3.0F).build();
	// Sand
	public static final Mat	sand	= new Mat(7201, "minecraft", "sand", "Sand", "Sand").builder().setRGBa(0xE8DCB1FF).setSand(0.8F, 1.3F).build();
	public static final Mat	redsand	= new Mat(7202, "minecraft", "redsand", "Red Sand", "Red Sand").builder().setRGBa(0xE8DCB1FF).setSand(0.9F, 1.3F).build();
	// TODO the color still needed be checked.
	// public static final Mat white_sand = new Mat(7203, FarCore.ID , "white_sand" , "WhiteSand" , "White Sand").builder().setRGBa(0xE8DCB1FF).setSand(0.8F, 1.4F).build();
	// public static final Mat salty_sand = new Mat(7204, FarCore.ID , "salty_sand" , "SaltySand" , "Salty Sand").builder().setRGBa(0xE8DCB1FF).setSand(0.8F, 1.2F).build();
	// Clay
	public static final Mat	chlorite	= new Mat(7301, FarCore.ID	, "chlorite"	, "Chlorite"	, "Chlorite"	).builder().setRGBa(0x939691FF).setClay(1.0F, 2.3F).build();
	public static final Mat	illite		= new Mat(7302, FarCore.ID	, "illite"		, "Illite"		, "Illite"		).builder().setRGBa(0x96958BFF).setClay(1.0F, 2.3F).build();
	public static final Mat	kaolinite	= new Mat(7303, FarCore.ID	, "kaolinite"	, "Kaolinite"	, "Kaolinite"	).builder().setRGBa(0xAFABA5FF).setClay(1.0F, 2.3F).build();
	public static final Mat	smectite	= new Mat(7304, FarCore.ID	, "smectite"	, "Smectite"	, "Smectite"	).builder().setRGBa(0x86898AFF).setClay(1.0F, 2.3F).build();
	// Miscellaneous terria
	public static final Mat	flint		= new Mat(7601, "minecraft"	, "flint"		, "Flint"		, "Flint"		).builder().setRGBa(0x2D2D2DFF).setToolProp(10, 8, 1.1F, 1.0F, 5.0F, -0.4F).build();
	public static final Mat	gravel		= new Mat(7602, "minecraft"	, "gravel"		, "Gravel"		, "Gravel"		).builder().setRGBa(0xAEB1BFFF).build();
	public static final Mat	quartz		= new Mat(7603, FarCore.ID	, "quartz"		, "Quartz"		, "Quartz"		).builder().setRGBa(0xDADBE5FF).setToolProp(12, 11, 1.9F, 1.0F, 4.0F, -0.5F).build();
	public static final Mat	wood		= new Mat(7604, FarCore.ID	, "wood"		, "Wood"		, "Wood"		).builder().setRGBa(0xA08155FF).setHandable(1.0F).build();
	// Trees
	public static final Mat	oak			= new Mat(8001, "minecraft"	, "oak"			, "Oak"			, "Oak"			).builder().setToolProp(7, 4, 1.2F, 0.1F, 2.0F, -0.2F).setWood(5.3F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 42, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	spruce		= new Mat(8002, "minecraft"	, "spruce"		, "Spruce"		, "Spruce"		).builder()                                           .setWood(2.3F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 28, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	birch		= new Mat(8003, "minecraft"	, "birch"		, "Birch"		, "Birch"		).builder().setToolProp(6, 3, 1.0F, 0.1F, 1.6F, -0.2F).setWood(4.0F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 35, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	ceiba		= new Mat(8004, "minecraft"	, "ceiba"		, "Ceiba"		, "Ceiba"		).builder()                                           .setWood(1.1F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 19, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	acacia		= new Mat(8005, "minecraft"	, "acacia"		, "Acacia"		, "Acacia"		).builder().setToolProp(5, 2, 0.8F, 0.1F, 1.0F, -0.2F).setWood(3.0F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 30, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	oak_black	= new Mat(8006, "minecraft"	, "oak-black"	, "DarkOak"		, "Dark Oak"	).builder().setToolProp(7, 4, 1.3F, 0.1F, 2.0F, -0.2F).setWood(5.4F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 42, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	aspen		= new Mat(8011, FarCore.ID	, "aspen"		, "Aspen"		, "Aspen"		).builder()                                           .setWood(1.6F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 23, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	morus		= new Mat(8012, FarCore.ID	, "morus"		, "Morus"		, "Morus"		).builder()                                           .setWood(3.0F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 34, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	willow		= new Mat(8013, FarCore.ID	, "willow"		, "Willow"		, "Willow"		).builder()                                           .setWood(3.0F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 34, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	lacquer		= new Mat(8014, "fle"		, "lacquer"		, "Lacquer"		, "Lacquer"		).builder()                                           .setWood(1.8F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 24, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	citrus		= new Mat(8015, "fle"		, "citrus"		, "Citrus"		, "Citrus"		).builder()                                           .setWood(1.3F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 20, 2.5F, 3.32E4F, 3.0F).build();
	public static final Mat	apple		= new Mat(8016, "fle"		, "apple"		, "Apple"		, "Apple"		).builder()                                           .setWood(2.3F, 1.0F, 20.0F).setGeneralProp(3.5E3F, 1.8E1F, 49, 19, 2.5F, 3.32E4F, 3.0F).build();
	// Plants
	public static final Mat	vine		= new Mat(9201, FarCore.ID, "vine", "Vine", "Vine").builder().setRGBa(0x867C50FF).build();
	public static final Mat	ivy			= new Mat(9202, FarCore.ID, "ivy", "Ivy", "Ivy").builder().setRGBa(0x867C50FF).build();
	public static final Mat	rattan		= new Mat(9203, FarCore.ID, "rattan", "Rattan", "Rattan").builder().setRGBa(0x867C50FF).build();
	public static final Mat	ramie_dry	= new Mat(9205, FarCore.ID, "ramie_dry", "RamieDry", "Ramie").builder().setRGBa(0xCFC898FF).build();
	
	public static final Mat	dandelion		= new Mat(9301, FarCore.ID, "dandelion"		, "Dandelion"		, "Dandelion"		);
	public static final Mat	bristlegrass	= new Mat(9302, FarCore.ID, "bristlegrass"	, "Bristlegrass"	, "Bristlegrass"	);
	public static final Mat	halogroot_bush	= new Mat(9303, FarCore.ID, "halogroot_bush", "HalogrootBush"	, "Halogroot Bush"	);
	public static final Mat	short_grass		= new Mat(9304, FarCore.ID, "short_grass"	, "ShortGrass"		, "Grass"			);
	public static final Mat	foxtail			= new Mat(9305, FarCore.ID, "foxtail"		, "Foxtail"			, "Foxtail"			);
	public static final Mat	goosefoots		= new Mat(9306, FarCore.ID, "goosefoots"	, "Goosefoots"		, "Goosefoots"		);
	public static final Mat	knotgrass		= new Mat(9307, FarCore.ID, "knotgrass"		, "Knotgrass"		, "Knotgrass"		);
	public static final Mat	pennycress		= new Mat(9308, FarCore.ID, "pennycress"	, "Pennycress"		, "Pennycress"		);
	public static final Mat	inula			= new Mat(9309, FarCore.ID, "inula"			, "Inula"			, "Inula"			);
	public static final Mat	poa_bluegrass	= new Mat(9310, FarCore.ID, "poa_bluegrass"	, "PoaBluegrass"	, "Poa Bluegrass"	);
	
	// Ores
	public static final Mat	native_copper	= new Mat(10001, FarCore.ID, "nativeCopper"	, "NativeCopper"	, "Native Copper"	).builder().setChemicalFormula("Cu"					).setRGBa(0xFF834CFF).setOreProperty( 7,  8.0F,  9.0F).build();
	public static final Mat	malachite		= new Mat(10002, FarCore.ID, "malachite"	, "Malachite"		, "Malachite"		).builder().setChemicalFormula("Cu(OH)2·CuCO3"		).setRGBa(0x30CE88FF).setOreProperty( 8,  8.8F,  9.0F).build();
	public static final Mat	cuprite			= new Mat(10003, FarCore.ID, "cuprite"		, "Cuprite"			, "Cuprite"			).builder().setChemicalFormula("Cu2O"				).setRGBa(0xD83E26FF).setOreProperty(10,  9.6F,  9.6F).build();
	public static final Mat	azurite			= new Mat(10004, FarCore.ID, "azurite"		, "Azurite"			, "Azurite"			).builder().setChemicalFormula("Cu(OH)2·2(CuCO3)"	).setRGBa(0x3039CEFF).setOreProperty(10,  9.5F,  9.5F).build();
	public static final Mat	chalcocite		= new Mat(10005, FarCore.ID, "chalcocite"	, "Chalcocite"		, "Chalcocite"		).builder().setChemicalFormula("Cu2S"				).setRGBa(0x8C96A1FF).setOreProperty(13, 10.8F, 10.2F).build();
	public static final Mat	tenorite		= new Mat(10006, FarCore.ID, "tenorite"		, "Tenorite"		, "Tenorite"		).builder().setChemicalFormula("CuO"				).setRGBa(0x4E5A63FF).setOreProperty(12, 10.2F,  9.7F).build();
	public static final Mat	chalcopyrite	= new Mat(10007, FarCore.ID, "chalcopyrite"	, "Chalcopyrite"	, "Chalcopyrite"	).builder().setChemicalFormula("CuFeS2"				).setRGBa(0xF3DA8AFF).setOreProperty(10,  9.0F,  9.6F).build();
	public static final Mat	bornite			= new Mat(10008, FarCore.ID, "bornite"		, "Bornite"			, "Bornite"			).builder().setChemicalFormula("Cu5FeS4"			).setRGBa(0xD3BA78FF).setOreProperty(14, 13.4F, 11.4F).build();
	public static final Mat	covellite		= new Mat(10009, FarCore.ID, "covellite"	, "Covellite"		, "Covellite"		).builder().setChemicalFormula("CuS"				).setRGBa(0x0B69D2FF).setOreProperty(20, 18.4F, 14.8F).build();
	public static final Mat	tetrahedrite	= new Mat(10010, FarCore.ID, "tetrahedrite"	, "Tetrahedrite"	, "Tetrahedrite"	).builder().setChemicalFormula("[Cu,Fe]12Sb4S13"	).setRGBa(0x999782FF).setOreProperty(21, 19.2F, 15.0F).build();
	public static final Mat	argentite		= new Mat(10011, FarCore.ID, "argentite"	, "Argentite"		, "Argentite"		).builder().setChemicalFormula("Ag2S"				).setRGBa(0x535455FF).setOreProperty(17, 15.4F, 12.8F).build();
	public static final Mat	pyrargyrite		= new Mat(10012, FarCore.ID, "pyrargyrite"	, "Pyrargyrite"		, "Pyrargyrite"		).builder().setChemicalFormula("Ag3SbS3"			).setRGBa(0x70483EFF).setOreProperty(18, 16.0F, 13.2F).build();
	public static final Mat	enargite		= new Mat(10013, FarCore.ID, "enargite"		, "Enargite"		, "Enargite"		).builder().setChemicalFormula("Cu3AsS4"			).setRGBa(0x705A59FF).setOreProperty(13, 12.4F, 13.9F).build();
	public static final Mat	realgar			= new Mat(10014, FarCore.ID, "realgar"		, "Realgar"			, "Realgar"			).builder().setChemicalFormula("As4S4"				).setRGBa(0xEF2B28FF).setOreProperty( 8,  8.4F,  9.1F).build();
	public static final Mat	orpiment		= new Mat(10015, FarCore.ID, "orpiment"		, "Orpiment"		, "Orpiment"		).builder().setChemicalFormula("As2S3"				).setRGBa(0xE6BB45FF).setOreProperty( 9,  8.8F,  9.4F).build();
	public static final Mat	arsenolite		= new Mat(10016, FarCore.ID, "arsenolite"	, "Arsenolite"		, "Arsenolite"		).builder().setChemicalFormula("As4O6"				).setRGBa(0x949960FF).setOreProperty(16, 14.6F, 14.0F).build();
	public static final Mat	nickeline		= new Mat(10017, FarCore.ID, "nickeline"	, "Nickeline"		, "Nickeline"		).builder().setChemicalFormula("NiAs"				).setRGBa(0xB2A76AFF).setOreProperty(31, 27.9F, 17.3F).build();
	public static final Mat	pyrite			= new Mat(10018, FarCore.ID, "pyrite"		, "Pyrite"			, "Pyrite"			).builder().setChemicalFormula("FeS2"				).setRGBa(0xDBCEA0FF).setOreProperty(16, 14.8F, 14.2F).build();
	public static final Mat	arsenopyrite	= new Mat(10019, FarCore.ID, "arsenopyrite"	, "Arsenopyrite"	, "Arsenopyrite"	).builder().setChemicalFormula("FeAsS"				).setRGBa(0xA3A29AFF).setOreProperty(17, 15.2F, 13.9F).build();
	public static final Mat	scorodite		= new Mat(10020, FarCore.ID, "scorodite"	, "Scorodite"		, "Scorodite"		).builder().setChemicalFormula("FeAsO4·2(H2O)"		).setRGBa(0x32538AFF).setOreProperty(25, 22.3F, 15.8F).build();
	public static final Mat	erythrite		= new Mat(10021, FarCore.ID, "erythrite"	, "Erythrite"		, "Erythrite"		).builder().setChemicalFormula("Co3(AsO4)2·8(H2O)"	).setRGBa(0xA9355EFF).setOreProperty(37, 32.1F, 19.3F).build();
	public static final Mat	domeykite		= new Mat(10022, FarCore.ID, "domeykite"	, "Domeykite"		, "Domeykite"		).builder().setChemicalFormula("Cu3As"				).setRGBa(0xA28261FF).setOreProperty(14, 13.5F, 11.8F).build();
	public static final Mat	cassiterite		= new Mat(10023, FarCore.ID, "cassiterite"	, "Cassiterite"		, "Cassiterite"		).builder().setChemicalFormula("SnO2"				).setRGBa(0xBCB8A5FF).setOreProperty(13, 12.6F, 10.5F).build();
	public static final Mat	gelenite		= new Mat(10024, FarCore.ID, "gelenite"		, "Gelenite"		, "Gelenite"		).builder().setChemicalFormula("PbS"				).setRGBa(0x6F7280FF).setOreProperty(11, 10.8F,  9.3F).build();
	public static final Mat	stannite		= new Mat(10025, FarCore.ID, "stannite"		, "Stannite"		, "Stannite"		).builder().setChemicalFormula("Cu2FeSnS4"			).setRGBa(0xB5B4A2FF).setOreProperty(17, 16.0F, 13.2F).build();
	public static final Mat	sphalerite		= new Mat(10026, FarCore.ID, "sphalerite"	, "Sphalerite"		, "Sphalerite"		).builder().setChemicalFormula("ZnS"				).setRGBa(0xB2A7A5FF).setOreProperty(22, 19.8F, 15.5F).build();
	public static final Mat	teallite		= new Mat(10027, FarCore.ID, "teallite"		, "Teallite"		, "Teallite"		).builder().setChemicalFormula("PbSnS2"				).setRGBa(0xCECECEFF).setOreProperty(23, 20.3F, 15.9F).build();
	public static final Mat	wolframite		= new Mat(10028, FarCore.ID, "wolframite"	, "Wolframite"		, "Wolframite"		).builder().setChemicalFormula("[Fe,Mn]WO4"			).setRGBa(0x2F2F2FFF).setOreProperty(40, 34.2F, 20.5F).build();
	public static final Mat	scheelite		= new Mat(10029, FarCore.ID, "scheelite"	, "Scheelite"		, "Scheelite"		).builder().setChemicalFormula("CaWO4"				).setRGBa(0xBD975AFF).setOreProperty(42, 35.7F, 21.8F).build();
	public static final Mat	bismuthinite	= new Mat(10030, FarCore.ID, "bismuthinite"	, "Bismuthinite"	, "Bismuthinite"	).builder().setChemicalFormula("Bi2S3"				).setRGBa(0x434A43FF).setOreProperty(29, 26.2F, 16.4F).build();
	public static final Mat	native_silver	= new Mat(10031, FarCore.ID, "nativeSilver"	, "NativeSilver"	, "Native Silver"	).builder().setChemicalFormula("Ag"					).setRGBa(0xEBE9E8FF).setOreProperty(13, 11.2F, 12.9F, SubTags.ORE_NOBLE).build();
	public static final Mat	native_gold		= new Mat(10032, FarCore.ID, "nativeGold"	, "NativeGold"		, "Native Gold"		).builder().setChemicalFormula("Au"					).setRGBa(0xF7B32AFF).setOreProperty( 5,  6.8F,  8.3F, SubTags.ORE_NOBLE).build();
	public static final Mat	electrum		= new Mat(10033, FarCore.ID, "electrum"		, "Electrum"		, "Electrum"		).builder().setChemicalFormula("?"					).setRGBa(0xE4B258FF).setOreProperty(11,  8.2F,  9.2F, SubTags.ORE_NOBLE).build();
	// Building resources
	public static final Mat	adobe			= new Mat(11001, FarCore.ID, "adobe"		, "Adobe"			, "Adobe"			).builder().setRGBa(0xABA798FF).setBrick(1, 3.0F, 4.0F).build();
	public static final Mat	argil			= new Mat(11002, FarCore.ID, "argil"		, "Argil"			, "Argil"			).builder().setRGBa(0xCBC0BCFF).setBrick(3, 4.5F, 7.0F).setGeneralProp(11.8E3F, 36.2F, 283, 36, 4.7F, 1E8F, 8.4F).build();
	public static final Mat	redbrick		= new Mat(11003, FarCore.ID, "redbrick"		, "RedBrick"		, "Red Brick"		).builder().setRGBa(0xB75A40FF).setBrick(4, 4.0F, 8.5F).build();
	public static final Mat	greybrick		= new Mat(11004, FarCore.ID, "greybrick"	, "GreyBrick"		, "Grey Brick"		).builder().setRGBa(0xB8CAC7FF).setBrick(4, 4.0F, 8.5F).build();
	
	static
	{
		block.setNames(sand, "Sand", redsand, "Red Sand");
		MC.stone.setNames(sand, "Brown Sandstone", redsand, "Ocherous Sandstone");
		chip_rock.setNames(sand, "Brown Sandstone Chip", redsand, "Ocherous Sandstone Chip");
		fragment.setNames(sand, "Brown Sandstone Fragment", redsand, "Ocherous Sandstone Fragment");
		
		copper.itemProp = new MatBehaviorCopper();
		
		SubTags.HANDLE.addTo(oak, spruce, birch, ceiba, acacia, oak_black, aspen, morus, willow, lacquer, citrus);
		SubTags.ROPE.addTo(vine, ramie_dry, rattan, ivy);
		SubTags.VINES.addTo(vine, rattan, ivy);
		SubTags.HERB.addTo(dandelion);
		SubTags.FLINT.addTo(flint, obsidian, quartz);
		SubTags.PILEABLE.addTo(gravel);
		
		fragment.addToBlackList(obsidian);
		pile_purified.addToBlackList(gravel);
		axe_rock.addToWhiteList(quartz);
		
		dandelion.builder().setPlant(new PlantDandelion());
		bristlegrass.builder().setPlant(new PlantBristlegrass());
		halogroot_bush.builder().setPlant(new PlantHalogrootBush());
		short_grass.builder().setPlant(new PlantStatic(short_grass, true));
		foxtail.builder().setPlant(new PlantStatic(foxtail, true));
		goosefoots.builder().setPlant(new PlantStatic(goosefoots, true));
		knotgrass.builder().setPlant(new PlantStatic(knotgrass, true));
		pennycress.builder().setPlant(new PlantStatic(pennycress, true));
		inula.builder().setPlant(new PlantStatic(inula, true));
		poa_bluegrass.builder().setPlant(new PlantStatic(poa_bluegrass, true));
		
		bituminous_coal.builder().setRock(new RockBehaviorFlammable<>(bituminous_coal, 4, 2.5F, 7.7F));
		lignite.builder().setRock(new RockBehaviorFlammable<>(lignite, 3, 2.2F, 6.4F));
		netherrack.builder().setRock(new RockBehaviorNetherrack(netherrack, 3, 1.3F, 3.8F));
		
		brick.setNames(redbrick, "Red Brick", greybrick, "Grey Brick");
		brickBlock.setNames(redbrick, "Red Brick", greybrick, "Grey Brick");
	}
	
	public static void init()
	{
		for (Runnable register : MATERIAL_REGISTERS)
		{
			try
			{
				register.run();
			}
			catch (Exception exception)
			{
				Log.warn("Caught an exception during {} is registering materials, " +
						"this exception might not cause by Far Core, please report " +
						"this execpion to creator.", register);
				throw exception;
			}
		}
	}
}
