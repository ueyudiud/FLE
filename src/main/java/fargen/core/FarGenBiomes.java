package fargen.core;

import static fargen.core.biome.BiomeBase.BiomePropertiesExtended.newProperties;

import farcore.data.EnumTempCategory;
import fargen.core.biome.BiomeBase;
import fargen.core.biome.BiomeOcean;
import fargen.core.biome.BiomePlain;
import fargen.core.biome.BiomeVoid;
import fargen.core.biome.layer.BiomeLayerGenerator;
import fargen.core.biome.layer.surface.BLGSurfaceOcean;
import fargen.core.biome.layer.surface.BLGSurfaceRiver;
import fargen.core.biome.layer.surface.BLGSurfaceStandard;
import fargen.core.util.ClimaticZone;

public class FarGenBiomes
{
	// Base biomes.
	public static BiomeBase	ocean_t;
	public static BiomeBase	ocean_t_deep;
	public static BiomeBase	ocean_st;
	public static BiomeBase	ocean_st_deep;
	public static BiomeBase	ocean_te;
	public static BiomeBase	ocean_te_deep;
	public static BiomeBase	ocean_sf;
	public static BiomeBase	ocean_sf_deep;
	public static BiomeBase	ocean_f;
	public static BiomeBase	ocean_f_deep;
	public static BiomeBase	savanna;
	public static BiomeBase	shrubland;
	public static BiomeBase	rockland;
	public static BiomeBase	grassland;
	public static BiomeBase	meadow;
	public static BiomeBase	tundra;
	public static BiomeBase	frozen_tundra;
	public static BiomeBase	tropical_desert;
	public static BiomeBase	temperate_desert;
	public static BiomeBase	rocky_desert;
	public static BiomeBase	tropical_monsoon_forest;
	public static BiomeBase	tropical_thorny_forest;
	public static BiomeBase	subtropical_broadleaf_forest;
	public static BiomeBase	subtropical_coniferous_forest;
	public static BiomeBase	sclerophyll_forest;
	public static BiomeBase	temperate_broadleaf_forest;
	public static BiomeBase	temperate_mixed_forest;
	public static BiomeBase	boreal_forest;
	public static BiomeBase	mangrove;
	public static BiomeBase	swamp;
	public static BiomeBase	gigafteral_forest;
	public static BiomeBase	tropical_rainforest;
	public static BiomeBase	temperate_rainforest;
	public static BiomeBase	gigafungal_forest;
	public static BiomeBase	sequoia_forest;
	public static BiomeBase	glacispical_land;
	// Extended biomes
	public static BiomeBase mushroom_island;
	// Technical biomes
	public static BiomeBase	tropical_desert_edge;
	public static BiomeBase	temperate_desert_edge;
	public static BiomeBase	rocky_desert_edge;
	
	public static BiomeBase[]	beach;
	public static BiomeBase[]	river;
	
	// Void world biomes
	public static BiomeBase v_void;
	
	public static void init()
	{
		BiomeLayerGenerator generator1 = new BLGSurfaceStandard();
		BiomeLayerGenerator generator2 = new BLGSurfaceOcean();
		BiomeLayerGenerator generator3 = new BLGSurfaceRiver();
		ocean_t = new BiomeOcean(0, newProperties("ocean_tropical").setClimaticZone(ClimaticZone.ocean_tropical).setLayerGenerator(generator2));
		ocean_st = new BiomeOcean(1, newProperties("ocean_subtropical").setClimaticZone(ClimaticZone.ocean_subtropical).setLayerGenerator(generator2));
		ocean_te = new BiomeOcean(2, newProperties("ocean_temperate").setClimaticZone(ClimaticZone.ocean_temperate).setLayerGenerator(generator2));
		ocean_sf = new BiomeOcean(3, newProperties("ocean_subfrigid").setClimaticZone(ClimaticZone.ocean_subfrigid).setLayerGenerator(generator2));
		ocean_f = new BiomeOcean(4, newProperties("ocean_frigid").setClimaticZone(ClimaticZone.ocean_frigid).setLayerGenerator(generator2));
		ocean_t_deep = new BiomeOcean(5, newProperties("ocean_tropical_deep").setClimaticZone(ClimaticZone.ocean_tropical).setLayerGenerator(generator2));
		ocean_st_deep = new BiomeOcean(6, newProperties("ocean_subtropical_deep").setClimaticZone(ClimaticZone.ocean_subtropical).setLayerGenerator(generator2));
		ocean_te_deep = new BiomeOcean(7, newProperties("ocean_temperate_deep").setClimaticZone(ClimaticZone.ocean_temperate).setLayerGenerator(generator2));
		ocean_sf_deep = new BiomeOcean(8, newProperties("ocean_subfrigid_deep").setClimaticZone(ClimaticZone.ocean_subfrigid).setLayerGenerator(generator2));
		ocean_f_deep = new BiomeOcean(9, newProperties("ocean_frigid_deep").setClimaticZone(ClimaticZone.ocean_frigid).setLayerGenerator(generator2));
		
		savanna = new BiomePlain(11, newProperties("savanna").setClimaticZone(ClimaticZone.tropical_plain).setLayerGenerator(generator1).setTreePerChunk(3, 4).setCropPerChunk(3, 2));
		shrubland = new BiomePlain(12, newProperties("shrubland").setClimaticZone(ClimaticZone.subtropical_plain).setLayerGenerator(generator1).setTreePerChunk(5, 6).setCropPerChunk(2, 1));
		rockland = new BiomePlain(13, newProperties("rockland").setClimaticZone(ClimaticZone.temperate_rockland).setLayerGenerator(generator1));
		grassland = new BiomePlain(14, newProperties("grassland").setClimaticZone(ClimaticZone.temperate_plain).setLayerGenerator(generator1).setTreePerChunk(1, 7).setCropPerChunk(1, 0));
		meadow = new BiomePlain(15, newProperties("meadow").setClimaticZone(ClimaticZone.subfrigid_plain).setLayerGenerator(generator1).setTreePerChunk(1, 10));
		tundra = new BiomeBase(16, newProperties("tundra").setClimaticZone(ClimaticZone.frigid_plain).setLayerGenerator(generator1));
		frozen_tundra = new BiomeBase(17, newProperties("frozen_tundra").setClimaticZone(ClimaticZone.iceland).setLayerGenerator(generator1));
		
		tropical_desert = new BiomeBase(21, newProperties("tropical_desert").setClimaticZone(ClimaticZone.tropical_desert).setLayerGenerator(generator1));
		temperate_desert = new BiomeBase(22, newProperties("temperate_desert").setClimaticZone(ClimaticZone.temperate_desert).setLayerGenerator(generator1));
		rocky_desert = new BiomeBase(23, newProperties("rocky_desert").setClimaticZone(ClimaticZone.frigid_desert).setLayerGenerator(generator1));
		tropical_desert_edge = new BiomeBase(tropical_desert.biomeID, false, newProperties("tropical_desert_edge").setClimaticZone(ClimaticZone.tropical_desert));
		temperate_desert_edge = new BiomeBase(temperate_desert.biomeID, false, newProperties("temperate_desert_edge").setClimaticZone(ClimaticZone.temperate_desert));
		rocky_desert_edge = new BiomeBase(rocky_desert.biomeID, false, newProperties("rocky_desert_edge").setClimaticZone(ClimaticZone.frigid_desert));
		
		tropical_monsoon_forest = new BiomeBase(31, newProperties("tropical_monsoon_forest").setClimaticZone(ClimaticZone.tropical_forest).setLayerGenerator(generator1).setTreePerChunk(7, 2).setCropPerChunk(2, 5));
		tropical_thorny_forest = new BiomeBase(32, newProperties("tropical_thorny_forest").setClimaticZone(ClimaticZone.tropical_monsoon).setLayerGenerator(generator1).setTreePerChunk(2, 1).setCropPerChunk(4, 2));
		subtropical_broadleaf_forest = new BiomeBase(33, newProperties("subtropical_broadleaf_forest").setClimaticZone(ClimaticZone.subtropical_monson).setLayerGenerator(generator1).setTreePerChunk(3, 1).setCropPerChunk(3, 2));
		subtropical_coniferous_forest = new BiomeBase(34, newProperties("subtropical_coniferous_forest").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(generator1).setTreePerChunk(3, 1).setCropPerChunk(3, 2));
		sclerophyll_forest = new BiomeBase(35, newProperties("sclerophyll_forest").setClimaticZone(ClimaticZone.temperate_monsoon).setLayerGenerator(generator1).setTreePerChunk(6, 5).setCropPerChunk(2, 1));
		temperate_broadleaf_forest = new BiomeBase(36, newProperties("temperate_broadleaf_forest").setClimaticZone(ClimaticZone.temperate_forest).setLayerGenerator(generator1).setTreePerChunk(5, 2).setCropPerChunk(3, 2));
		temperate_mixed_forest = new BiomeBase(37, newProperties("temperate_mixed_forest").setClimaticZone(ClimaticZone.temperate_forest).setLayerGenerator(generator1).setTreePerChunk(2, 1).setCropPerChunk(2, 2));
		boreal_forest = new BiomeBase(38, newProperties("boreal_forest").setClimaticZone(ClimaticZone.subfrigid_forest).setLayerGenerator(generator1).setTreePerChunk(3, 2).setCropPerChunk(1, 0));
		mangrove = new BiomeBase(41, newProperties("mangrove").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(generator1).setTreePerChunk(8, 3).setCropPerChunk(2, 2));
		swamp = new BiomeBase(42, newProperties("swamp").setClimaticZone(ClimaticZone.temperate_wet).setLayerGenerator(generator1).setTreePerChunk(5, 3).setCropPerChunk(1, 3));
		
		gigafteral_forest = new BiomeBase(51, newProperties("gigafteral_forest").setClimaticZone(ClimaticZone.tropical_rainforest).setLayerGenerator(generator1).setTreePerChunk(5, 1).setCropPerChunk(5, 3));
		tropical_rainforest = new BiomeBase(52, newProperties("tropical_rainforest").setClimaticZone(ClimaticZone.tropical_rainforest).setLayerGenerator(generator1).setTreePerChunk(5, 1).setCropPerChunk(5, 3));
		temperate_rainforest = new BiomeBase(53, newProperties("temperate_rainforest").setClimaticZone(ClimaticZone.temperate_rainforest).setLayerGenerator(generator1).setTreePerChunk(9, 2).setCropPerChunk(5, 2));
		gigafungal_forest = new BiomeBase(54, newProperties("gigafungal_forest").setClimaticZone(ClimaticZone.subfrigid_forest).setLayerGenerator(generator1).setTreePerChunk(5, 2).setCropPerChunk(2, 3));
		sequoia_forest = new BiomeBase(55, newProperties("sequoia_forest").setClimaticZone(ClimaticZone.frigid_plain).setLayerGenerator(generator1).setTreePerChunk(2, 1).setCropPerChunk(1, 1));
		glacispical_land = new BiomeBase(56, newProperties("glacispical_land").setClimaticZone(ClimaticZone.iceland).setLayerGenerator(generator1));
		
		mushroom_island = new BiomeBase(101, newProperties("mushroom_island").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(generator1));
		river = new BiomeBase[ClimaticZone.values().length];
		beach = new BiomeBase[ClimaticZone.values().length];
		for (ClimaticZone zone : ClimaticZone.values())
		{
			if (zone.category1 == EnumTempCategory.OCEAN)
			{
				continue;
			}
			river[zone.ordinal()] = new BiomeBase(121 + zone.ordinal(), newProperties("river_" + zone.name().toLowerCase()).setClimaticZone(zone).setLayerGenerator(generator3).setTreePerChunk(zone.temperatureAverage > 0.7F ? 4 : 1, 2));
			beach[zone.ordinal()] = new BiomeBase(151 + zone.ordinal(), newProperties("beach_" + zone.name().toLowerCase()).setClimaticZone(zone).setLayerGenerator(generator1));
		}
		river[ClimaticZone.ocean_frigid.ordinal()] = ocean_f;
		river[ClimaticZone.ocean_subfrigid.ordinal()] = ocean_sf;
		river[ClimaticZone.ocean_temperate.ordinal()] = ocean_te;
		river[ClimaticZone.ocean_subtropical.ordinal()] = ocean_st;
		river[ClimaticZone.ocean_tropical.ordinal()] = ocean_t;
		beach[ClimaticZone.ocean_frigid.ordinal()] = ocean_f;
		beach[ClimaticZone.ocean_subfrigid.ordinal()] = ocean_sf;
		beach[ClimaticZone.ocean_temperate.ordinal()] = ocean_te;
		beach[ClimaticZone.ocean_subtropical.ordinal()] = ocean_st;
		beach[ClimaticZone.ocean_tropical.ordinal()] = ocean_t;
		
		v_void = new BiomeVoid();
	}
}
