package fle.load;

import farcore.enums.EnumBiome;
import farcore.util.FleLog;
import fle.core.world.biome.BiomeBeach;
import fle.core.world.biome.BiomeBushveld;
import fle.core.world.biome.BiomeDesert;
import fle.core.world.biome.BiomeForest;
import fle.core.world.biome.BiomeGlacier;
import fle.core.world.biome.BiomeIsland;
import fle.core.world.biome.BiomeJungle;
import fle.core.world.biome.BiomeMountain;
import fle.core.world.biome.BiomeMushroomIsland;
import fle.core.world.biome.BiomeOcean;
import fle.core.world.biome.BiomePlain;
import fle.core.world.biome.BiomeRiver;
import fle.core.world.biome.BiomeSavanna;
import fle.core.world.biome.BiomeSwamp;
import fle.core.world.biome.BiomeTaiga;
import fle.core.world.biome.BiomeTundra;
import fle.core.world.biome.BiomeValley;
import fle.core.world.biome.BiomeWasteland;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase.Height;

public class Biomes
{
	static final Height height_River = new Height(-0.5F, 0.02F);
	static final Height height_Plain = new Height(0.1F, 0.05F);
	static final Height height_LowHills = new Height(0.8F, 0.5F);
    static final Height height_MidHills = new Height(1.5F, 0.8F);
    static final Height height_HighHills = new Height(2.3F, 1.1F);
    static final Height height_Plateau = new Height(1.2F, 0.1F);
    static final Height height_Valley = new Height(0.3F, 0.9F);
    static final Height height_LowIslands = new Height(0.2F, 0.3F);
    
	public static void init()
	{
		FleLog.getLogger().info("Start register biomes.");
		EnumBiome.plain.setBiome(						new BiomePlain(1).setColor(0x8DB360).setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Plain));
		EnumBiome.plain_subtropics.setBiome(			new BiomePlain(2).setColor(0x9EBC65).setTemperatureRainfall(1.2F, 0.4F).setHeight(height_Plain));
		EnumBiome.desert.setBiome(						new BiomeDesert(3, true, true).setColor(0xFA9418).setDisableRain().setTemperatureRainfall(1.5F, 0F).setHeight(height_Plain));
		EnumBiome.desert_subtropics.setBiome(			new BiomeDesert(4, false, true).setColor(0xEF8017).setDisableRain().setTemperatureRainfall(1.8F, 0F).setHeight(height_Plain));
		EnumBiome.desert_hot.setBiome(					new BiomeDesert(5, false, false).setColor(0xD85015).setDisableRain().setTemperatureRainfall(2.0F, 0F).setHeight(height_Plain));
		EnumBiome.forest_evergreen.setBiome(			new BiomeForest(6, 0).setColor(0x068200).setTemperatureRainfall(0.9F, 0.8F).setHeight(height_Plain));
		EnumBiome.forest_deciduous.setBiome(			new BiomeForest(7, 0).setColor(0x056621).setTemperatureRainfall(0.7F, 0.8F).setHeight(height_Plain));
		EnumBiome.forest_coniferous.setBiome(			new BiomeTaiga(8, 0).setColor(0x0B6659).setTemperatureRainfall(0.4F, 0.5F).setHeight(height_Plain));
		EnumBiome.forest_coniferous_snowy.setBiome(		new BiomeTaiga(9, 0).setColor(0x31554A).setTemperatureRainfall(0.2F, 0.3F).setEnableSnow().setHeight(height_Plain));
		EnumBiome.forest_tropic.setBiome(				new BiomeForest(10, 0).setColor(0x356605).setTemperatureRainfall(1.4F, 0.7F).setHeight(height_Plain));
		EnumBiome.swamp.setBiome(						new BiomeSwamp(11).setColor(0x07F9B2).func_76733_a(9154376).setHeight(new Height(-0.2F, 0.1F)).setTemperatureRainfall(1.0F, 0.9F));
		EnumBiome.rainforest.setBiome(					new BiomeJungle(12, true).setColor(0x537B09).func_76733_a(5470985).setTemperatureRainfall(1.3F, 0.9F));
		EnumBiome.savanna.setBiome(						new BiomeSavanna(13).setColor(0xBDB25F).setDisableRain().setHeight(height_Plain).setTemperatureRainfall(1.4F, 0.2F));
		EnumBiome.bushveld.setBiome(					new BiomeBushveld(14).setColor(0xD3C252).setDisableRain().setHeight(height_Plain).setTemperatureRainfall(1.4F, 0.1F));
		EnumBiome.wasteland_subtropis.setBiome(			new BiomeWasteland(15).setColor(0xC6AB23).setDisableRain().setHeight(height_Plain).setTemperatureRainfall(1.0F, 0.02F));
		EnumBiome.wasteland.setBiome(					new BiomeWasteland(16).setColor(0xD8BB27).setDisableRain().setHeight(height_Plain).setTemperatureRainfall(0.8F, 0.02F));
		EnumBiome.tundra.setBiome(						new BiomeTundra(17).setColor(0xD3BFA9).setDisableRain().setHeight(height_Plain).setTemperatureRainfall(0.05F, 0.02F));
		EnumBiome.glacier.setBiome(						new BiomeGlacier(18).setColor(0xFAFFEF).setEnableSnow().setHeight(height_Plain).setTemperatureRainfall(-0.2F, 0.02F));
		EnumBiome.ocean.setBiome(						new BiomeOcean(0, false, false).setColor(0x6368B7).setTemperatureRainfall(0.6F, 0.5F));
		EnumBiome.ocean_deep.setBiome(					new BiomeOcean(19, false, true).setColor(0x0002B2).setTemperatureRainfall(0.6F, 0.6F));
		EnumBiome.ocean_icy.setBiome(					new BiomeOcean(20, false, false).setColor(0x69DACB5).setTemperatureRainfall(0.0F, 0.3F));
		EnumBiome.ocean_icy_deep.setBiome(				new BiomeOcean(21, false, true).setColor(0x5D7AAF).setTemperatureRainfall(0.0F, 0.3F));
		EnumBiome.continental_shelf.setBiome(			new BiomeOcean(22, true, false).setColor(0xD3EAED).setTemperatureRainfall(0.6F, 0.6F));
		EnumBiome.continental_shelf_snowy.setBiome(		new BiomeOcean(23, true, false).setColor(0xE8EAE5).setTemperatureRainfall(0.0F, 0.4F).setEnableSnow());
		EnumBiome.river_desert.setBiome(				new BiomeRiver(24, true).setColor(0x1700E8).setTemperatureRainfall(1.8F, 0.1F).setDisableRain().setHeight(height_River));
		EnumBiome.river_rainforest.setBiome(			new BiomeRiver(25, false).setColor(0x1700E8).setTemperatureRainfall(1.2F, 0.7F).setHeight(height_River));
		EnumBiome.river_grass.setBiome(					new BiomeRiver(26, false).setColor(0x1700E8).setTemperatureRainfall(0.8F, 0.5F).setHeight(height_River));
		EnumBiome.river_freeze.setBiome(				new BiomeRiver(27, false).setColor(0x1700E8).setTemperatureRainfall(0.0F, 0.2F).setEnableSnow().setHeight(height_River));
		EnumBiome.beach_sand.setBiome(					new BiomeBeach(28, false).setColor(0xFADE55).setTemperatureRainfall(0.8F, 0.4F));
		EnumBiome.beach_sand_snowy.setBiome(			new BiomeBeach(29, false).setColor(0xFADE55).setTemperatureRainfall(0.0F, 0.2F).setEnableSnow());
		EnumBiome.beach_stone.setBiome(					new BiomeBeach(30, true).setColor(0xA3A3A3).setTemperatureRainfall(0.8F, 0.4F));
		EnumBiome.beach_stone_snowy.setBiome(			new BiomeBeach(31, true).setColor(0xA3A3A3).setTemperatureRainfall(0.0F, 0.2F).setEnableSnow());
		
		EnumBiome.rainforest_hill.setBiome(				new BiomeJungle(51, false).setColor(0x2C4205).func_76733_a(5470985).setTemperatureRainfall(1.3F, 0.9F).setHeight(height_LowHills));
		EnumBiome.savanna_hill.setBiome(				new BiomeSavanna(52).setColor(0xB9BC60).setDisableRain().setTemperatureRainfall(1.3F, 0.2F).setHeight(height_LowHills));
		EnumBiome.bushveld_hill.setBiome(				new BiomeBushveld(53).setColor(0xD3C252).setDisableRain().setTemperatureRainfall(1.3F, 0.1F).setHeight(height_LowHills));
		EnumBiome.forest_evergreen_hill.setBiome(		new BiomeForest(54, 1).setColor(0x078802).setTemperatureRainfall(0.9F, 0.75F).setHeight(height_LowHills));
		EnumBiome.grassland_hill.setBiome(				new BiomePlain(55).setColor(0x9EBC65).setTemperatureRainfall(1.2F, 0.4F).setHeight(height_LowHills));
		EnumBiome.forest_deciduous_hill.setBiome(		new BiomeForest(56, 1).setColor(0x056621).setTemperatureRainfall(0.7F, 0.8F).setHeight(height_LowHills));
		EnumBiome.grass_hill.setBiome(					new BiomePlain(57).setColor(0x8DB360).setTemperatureRainfall(0.8F, 0.4F).setHeight(height_LowHills));
		EnumBiome.forest_coniferous_hill.setBiome(		new BiomeTaiga(58, 0).setColor(0x596651).setTemperatureRainfall(0.4F, 0.5F).setHeight(height_LowHills));
		EnumBiome.tundra_hill.setBiome(					new BiomeTundra(59).setColor(0xD5D6D1).setDisableRain().setTemperatureRainfall(0.05F, 0.02F).setHeight(height_LowHills));
		EnumBiome.forest_coniferous_snowy_hill.setBiome(new BiomeTaiga(60, 0).setColor(0x243F36).setEnableSnow().setTemperatureRainfall(0.2F, 0.3F).setHeight(height_LowHills));
		EnumBiome.mountain_snowy.setBiome(				new BiomeMountain(61, false, false).setColor(0x606060).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_HighHills));
		EnumBiome.mountain_deciduous_forest.setBiome(	new BiomeMountain(62, true, true).setColor(0x609860).setTemperatureRainfall(0.3F, 0.4F).setHeight(height_MidHills));
		EnumBiome.mountain_meadow.setBiome(				new BiomeMountain(63, false, true).setColor(0x607260).setTemperatureRainfall(0.2F, 0.4F).setHeight(height_MidHills));
		EnumBiome.mountain_frigid.setBiome(				new BiomeMountain(64, false, false).setColor(0x707070).setEnableSnow().setTemperatureRainfall(-0.2F, 0.4F).setHeight(height_MidHills));
		EnumBiome.volcanic_island.setBiome(				new BiomeIsland(65, Blocks.obsidian, true).setColor(0x34343F).setTemperatureRainfall(0.9F, 0.6F).setHeight(height_LowIslands));
		EnumBiome.tropic_island.setBiome(				new BiomeIsland(66, Blocks.grass, true).setColor(0x11950C).setTemperatureRainfall(1.2F, 0.9F).setHeight(height_LowIslands));
		EnumBiome.savanna_plateau.setBiome(				new BiomeSavanna(67).setColor(0xBDB25F).setDisableRain().setTemperatureRainfall(1.3F, 0.2F).setHeight(height_Plateau));
		EnumBiome.forest_plateau.setBiome(				new BiomeTaiga(68, 1).setColor(0x596651).setTemperatureRainfall(0.4F, 0.3F).setHeight(height_Plateau));
		EnumBiome.grassland_plateau.setBiome(			new BiomePlain(69).setColor(0x9EB287).setTemperatureRainfall(0.7F, 0.3F).setHeight(height_Plateau));
		EnumBiome.tundra_plateau.setBiome(				new BiomeTundra(70).setColor(0xD3D1AD).setDisableRain().setTemperatureRainfall(0.0F, 0.02F).setHeight(height_Plateau));
		EnumBiome.valley_tropic.setBiome(				new BiomeValley(71).setColor(0x5DD140).setTemperatureRainfall(0.9F, 0.7F).setHeight(height_Valley));
		EnumBiome.valley_temperate.setBiome(			new BiomeValley(72).setColor(0x99CE8C).setTemperatureRainfall(0.5F, 0.5F).setHeight(height_Valley));
		EnumBiome.valley_frigid.setBiome(				new BiomeValley(73).setColor(0xBBCCB7).setTemperatureRainfall(0.2F, 0.3F).setHeight(height_Valley));
		EnumBiome.desert_subtropics_hill.setBiome(		new BiomeDesert(74, false, true).setColor(0xDF7017).setDisableRain().setTemperatureRainfall(1.8F, 0F).setHeight(height_LowHills));
		EnumBiome.mushroom_island.setBiome(				new BiomeMushroomIsland(75).setColor(0x8F719B).setTemperatureRainfall(0.9F, 0.9F).setHeight(height_LowIslands));
		
		EnumBiome.rainforest_edge.setBiome(				new BiomeJungle(91, false).setColor(0x197525).setTemperatureRainfall(1.2F, 0.86F).setHeight(height_Plain));
		EnumBiome.desert_tropic_edge.setBiome(			new BiomeDesert(92, false, false).setColor(0xCF9418).setTemperatureRainfall(1.9F, 0.04F).setDisableRain().setHeight(height_Plain));
		EnumBiome.desert_edge.setBiome(					new BiomeDesert(93, false, false).setColor(0xD3A780).setTemperatureRainfall(1.4F, 0.04F).setDisableRain().setHeight(height_Plain));
	}
}