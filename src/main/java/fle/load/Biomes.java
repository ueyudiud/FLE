package fle.load;

import farcore.enums.EnumBiome;
import farcore.util.FleLog;
import fle.core.world.biome.BiomeBeach;
import fle.core.world.biome.BiomeIsland;
import fle.core.world.biome.BiomeMountain;
import fle.core.world.biome.BiomeOcean;
import fle.core.world.biome.BiomePlain;
import fle.core.world.biome.BiomeRiver;
import fle.core.world.biome.BiomeSwamp;
import fle.core.world.biome.BiomeValley;
import fle.core.world.climate.EnumClimate;
import fle.core.world.climate.surface.ClimateDesertTemperate;
import fle.core.world.climate.surface.ClimateDesertTropical;
import fle.core.world.climate.surface.ClimateForestBoreal;
import fle.core.world.climate.surface.ClimateForestDeciduousBroadleaf;
import fle.core.world.climate.surface.ClimateForestGigafteral;
import fle.core.world.climate.surface.ClimateForestGigafungal;
import fle.core.world.climate.surface.ClimateForestSclerophyll;
import fle.core.world.climate.surface.ClimateForestSequoia;
import fle.core.world.climate.surface.ClimateForestSubtropicalBroadleaf;
import fle.core.world.climate.surface.ClimateForestSubtropicalConiferous;
import fle.core.world.climate.surface.ClimateForestTemperateMixed;
import fle.core.world.climate.surface.ClimateForestTropicalMonsoon;
import fle.core.world.climate.surface.ClimateFrozenTundra;
import fle.core.world.climate.surface.ClimateGrassland;
import fle.core.world.climate.surface.ClimateIceSpikesLand;
import fle.core.world.climate.surface.ClimateMangrove;
import fle.core.world.climate.surface.ClimateMeadow;
import fle.core.world.climate.surface.ClimateOcean;
import fle.core.world.climate.surface.ClimateRainforestTemperate;
import fle.core.world.climate.surface.ClimateRainforestTropical;
import fle.core.world.climate.surface.ClimateRiver;
import fle.core.world.climate.surface.ClimateRockland;
import fle.core.world.climate.surface.ClimateRockyDesert;
import fle.core.world.climate.surface.ClimateSavanna;
import fle.core.world.climate.surface.ClimateShrubland;
import fle.core.world.climate.surface.ClimateSwamp;
import fle.core.world.climate.surface.ClimateTundra;
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
		EnumBiome.ocean.setBiome(				new BiomeOcean(0, false, false)			.setColor(0x6368B7));
		EnumBiome.plain.setBiome(				new BiomePlain(1)						.setColor(0x8DB360).setHeight(height_Plain));
		EnumBiome.ocean_deep.setBiome(			new BiomeOcean(2, false, true)			.setColor(0x0002B2));
		EnumBiome.continental_shelf.setBiome(	new BiomeOcean(3, true, false)			.setColor(0x269DCC));
		EnumBiome.low_hill.setBiome(			new BiomePlain(4)						.setColor(0x9EBC65).setHeight(height_LowHills));
		EnumBiome.mid_hill.setBiome(			new BiomePlain(5)						.setColor(0xA2C269).setHeight(height_MidHills));
		EnumBiome.mid_mountain.setBiome(		new BiomeMountain(6, true, true)		.setColor(0x609860).setHeight(height_MidHills));
		EnumBiome.plateau.setBiome(				new BiomePlain(7)						.setColor(0x9EB287).setHeight(height_Plateau));
		EnumBiome.high_mountain.setBiome(		new BiomeMountain(8, false, false)		.setColor(0x606060).setHeight(height_HighHills).setEnableSnow());
		EnumBiome.island.setBiome(				new BiomeIsland(9, Blocks.grass, false)	.setColor(0x11950C).setHeight(height_LowIslands));
		EnumBiome.beach.setBiome(				new BiomeBeach(10)						.setColor(0xFADE55));
		EnumBiome.river.setBiome(				new BiomeRiver(11, false)				.setColor(0x1700E8).setHeight(height_River));
		EnumBiome.valley.setBiome(				new BiomeValley(12)						.setColor(0x5DD140));
		EnumBiome.swamp.setBiome(				new BiomeSwamp(13)						.setColor(0x07F9B2).func_76733_a(9154376));
		
		EnumClimate.ocean.setClimate(							new ClimateOcean(0, "ocean"));
		EnumClimate.ocean_frozen.setClimate(					new ClimateOcean(1, "frozen ocean"));
		EnumClimate.deep_ocean.setClimate(						new ClimateOcean(2, "deep ocean"));
		EnumClimate.continental_shelf.setClimate(				new ClimateOcean(3, "continental shelf"));
		EnumClimate.river.setClimate(							new ClimateRiver(4, "river"));
		EnumClimate.river_frozen.setClimate(					new ClimateRiver(5, "frozen river").setEnableSnow());
		EnumClimate.tropical_desert.setClimate(					new ClimateDesertTropical(6, "tropical desert").setDisableRain());
		EnumClimate.savanna.setClimate(							new ClimateSavanna(7, "savanna").setDisableRain());
		EnumClimate.tropical_monsoon_forest.setClimate(			new ClimateForestTropicalMonsoon(8, "tropical monsoon forest"));
		EnumClimate.tropical_rainforest.setClimate(				new ClimateRainforestTropical(9, "tropical rainforest"));
		EnumClimate.gigafteral_forest.setClimate(				new ClimateForestGigafteral(10, "gigafteral forest"));
		EnumClimate.subtropical_coniferous_forest.setClimate(	new ClimateForestSubtropicalConiferous(11, "subtropical coniferous forest"));
		EnumClimate.subtropical_broadleaf_forest.setClimate(	new ClimateForestSubtropicalBroadleaf(12, "subtropical broadleaf forest"));
		EnumClimate.mangrove.setClimate(						new ClimateMangrove(13, "mangrove"));
		EnumClimate.temperate_rainforest.setClimate(			new ClimateRainforestTemperate(14, "temperate rainforest"));
		EnumClimate.temperate_desert.setClimate(				new ClimateDesertTemperate(15, "temperate desert").setDisableRain());
		EnumClimate.shrubland.setClimate(						new ClimateShrubland(16, "shrubland").setDisableRain());
		EnumClimate.sclerophyll_forest.setClimate(				new ClimateForestSclerophyll(17, "sclerophyll forest"));
		EnumClimate.deciduous_broad_leaved_forest.setClimate(	new ClimateForestDeciduousBroadleaf(18, "deciduous broadleaf forest"));
		EnumClimate.swamp.setClimate(							new ClimateSwamp(19, "swamp"));
		EnumClimate.gigafungal_forest.setClimate(				new ClimateForestGigafungal(20, "gigafungal forest"));
		EnumClimate.rockland.setClimate(						new ClimateRockland(21, "rockland").setDisableRain());
		EnumClimate.grassland.setClimate(						new ClimateGrassland(22, "grassland"));
		EnumClimate.temperate_mixed_forest.setClimate(			new ClimateForestTemperateMixed(23, "temperate mixed forest"));
		EnumClimate.rocky_desert.setClimate(					new ClimateRockyDesert(24, "rocky desert").setDisableRain());
		EnumClimate.boreal_forest.setClimate(					new ClimateForestBoreal(25, "boreal forest"));
		EnumClimate.sequoia_forest.setClimate(					new ClimateForestSequoia(26, "sequoia forest"));
		EnumClimate.meadow.setClimate(							new ClimateMeadow(27, "meadow").setDisableRain());
		EnumClimate.tundra.setClimate(							new ClimateTundra(28, "tundra").setDisableRain());
		EnumClimate.frozen_turdra.setClimate(					new ClimateFrozenTundra(29, "frozen turdra").setEnableSnow());
		EnumClimate.ice_spikes_land.setClimate(					new ClimateIceSpikesLand(30, "ice spikes land"));
	}
}