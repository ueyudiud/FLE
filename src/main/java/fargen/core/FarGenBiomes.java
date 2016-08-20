package fargen.core;

import fargen.core.biome.BiomeBase;
import fargen.core.biome.BiomeBase.BiomePropertiesExtended;
import fargen.core.biome.layer.BiomeLayerGenerator;
import fargen.core.biome.layer.surface.BLGSDesert;
import fargen.core.biome.layer.surface.BLGSSimple;
import fargen.core.util.ClimaticZone;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class FarGenBiomes
{
	//Base biomes.
	public static BiomeBase icy_ocean;
	public static BiomeBase icy_ocean_deep;
	public static BiomeBase ocean;
	public static BiomeBase ocean_deep;
	public static BiomeBase savanna;
	public static BiomeBase shrubland;
	public static BiomeBase rockland;
	public static BiomeBase grassland;
	public static BiomeBase meadow;
	public static BiomeBase tundra;
	public static BiomeBase frozen_tundra;
	public static BiomeBase tropical_desert;
	public static BiomeBase temperate_desert;
	public static BiomeBase rocky_desert;
	public static BiomeBase tropical_monsoon_forest;
	public static BiomeBase tropical_thorny_forest;
	public static BiomeBase subtropical_broadleaf_forest;
	public static BiomeBase subtropical_coniferous_forest;
	public static BiomeBase sclerophyll_forest;
	public static BiomeBase temperate_broadleaf_forest;
	public static BiomeBase temperate_mixed_forest;
	public static BiomeBase boreal_forest;
	public static BiomeBase mangrove;
	public static BiomeBase swamp;
	public static BiomeBase gigafteral_forest;
	public static BiomeBase tropical_rainforest;
	public static BiomeBase temperate_rainforest;
	public static BiomeBase gigafungal_forest;
	public static BiomeBase sequoia_forest;
	public static BiomeBase glacispical_land;
	//Extended biomes
	public static BiomeBase mushroom_island;
	//Technical biomes
	public static BiomeBase[] beach;
	public static BiomeBase[] river;
	
	public static void init()
	{
		IBlockState PODZEL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
		BiomeLayerGenerator baseGenerator = new BLGSSimple(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState(), true);
		ocean = new BiomeBase(0, BiomePropertiesExtended.newProperties("ocean").setClimaticZone(ClimaticZone.ocean).setLayerGenerator(new BLGSSimple(Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false)));
		ocean_deep = new BiomeBase(1, BiomePropertiesExtended.newProperties("ocean_deep").setClimaticZone(ClimaticZone.ocean).setLayerGenerator(new BLGSSimple(Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false)));
		icy_ocean = new BiomeBase(2, BiomePropertiesExtended.newProperties("icy_ocean").setClimaticZone(ClimaticZone.ocean_icy).setLayerGenerator(new BLGSSimple(Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false)));
		icy_ocean_deep = new BiomeBase(3, BiomePropertiesExtended.newProperties("icy_ocean_deep").setClimaticZone(ClimaticZone.ocean_icy).setLayerGenerator(new BLGSSimple(Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false)));
		savanna = new BiomeBase(11, BiomePropertiesExtended.newProperties("savanna").setClimaticZone(ClimaticZone.tropical_plain).setLayerGenerator(baseGenerator));
		shrubland = new BiomeBase(12, BiomePropertiesExtended.newProperties("shrubland").setClimaticZone(ClimaticZone.subtropical_plain).setLayerGenerator(baseGenerator));
		rockland = new BiomeBase(13, BiomePropertiesExtended.newProperties("rockland").setClimaticZone(ClimaticZone.temperate_rockland).setLayerGenerator(baseGenerator));
		grassland = new BiomeBase(14, BiomePropertiesExtended.newProperties("grassland").setClimaticZone(ClimaticZone.temperate_plain).setLayerGenerator(baseGenerator));
		meadow = new BiomeBase(15, BiomePropertiesExtended.newProperties("meadow").setClimaticZone(ClimaticZone.subfrigid_plain).setLayerGenerator(baseGenerator));
		tundra = new BiomeBase(16, BiomePropertiesExtended.newProperties("tundra").setClimaticZone(ClimaticZone.frigid_plain).setLayerGenerator(new BLGSSimple(PODZEL, Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState(), true)));
		frozen_tundra = new BiomeBase(17, BiomePropertiesExtended.newProperties("frozen_tundra").setClimaticZone(ClimaticZone.iceland).setLayerGenerator(new BLGSSimple(PODZEL, Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState(), true)));
		tropical_desert = new BiomeBase(21, BiomePropertiesExtended.newProperties("tropical_desert").setClimaticZone(ClimaticZone.tropical_desert).setLayerGenerator(new BLGSDesert()));
		temperate_desert = new BiomeBase(22, BiomePropertiesExtended.newProperties("temperate_desert").setClimaticZone(ClimaticZone.temperate_desert).setLayerGenerator(new BLGSDesert()));
		rocky_desert = new BiomeBase(23, BiomePropertiesExtended.newProperties("rocky_desert").setClimaticZone(ClimaticZone.frigid_desert).setLayerGenerator(new BLGSDesert()));
		tropical_monsoon_forest = new BiomeBase(31, BiomePropertiesExtended.newProperties("tropical_monsoon_forest").setClimaticZone(ClimaticZone.tropical_forest).setLayerGenerator(baseGenerator));
		tropical_thorny_forest = new BiomeBase(32, BiomePropertiesExtended.newProperties("tropical_thorny_forest").setClimaticZone(ClimaticZone.tropical_monsoon).setLayerGenerator(baseGenerator));
		subtropical_broadleaf_forest = new BiomeBase(33, BiomePropertiesExtended.newProperties("subtropical_broadleaf_forest").setClimaticZone(ClimaticZone.subtropical_monson).setLayerGenerator(baseGenerator));
		subtropical_coniferous_forest = new BiomeBase(34, BiomePropertiesExtended.newProperties("subtropical_coniferous_forest").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(baseGenerator));
		sclerophyll_forest = new BiomeBase(35, BiomePropertiesExtended.newProperties("sclerophyll_forest").setClimaticZone(ClimaticZone.temperate_monsoon).setLayerGenerator(baseGenerator));
		temperate_broadleaf_forest = new BiomeBase(36, BiomePropertiesExtended.newProperties("temperate_broadleaf_forest").setClimaticZone(ClimaticZone.temperate_forest).setLayerGenerator(baseGenerator));
		temperate_mixed_forest = new BiomeBase(37, BiomePropertiesExtended.newProperties("temperate_mixed_forest").setClimaticZone(ClimaticZone.temperate_forest).setLayerGenerator(baseGenerator));
		boreal_forest = new BiomeBase(38, BiomePropertiesExtended.newProperties("boreal_forest").setClimaticZone(ClimaticZone.subfrigid_forest).setLayerGenerator(baseGenerator));
		mangrove = new BiomeBase(41, BiomePropertiesExtended.newProperties("mangrove").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(baseGenerator));
		swamp = new BiomeBase(42, BiomePropertiesExtended.newProperties("swamp").setClimaticZone(ClimaticZone.temperate_forest).setLayerGenerator(baseGenerator));
		gigafteral_forest = new BiomeBase(51, BiomePropertiesExtended.newProperties("gigafteral_forest").setClimaticZone(ClimaticZone.tropical_rainforest).setLayerGenerator(baseGenerator));
		tropical_rainforest = new BiomeBase(52, BiomePropertiesExtended.newProperties("tropical_rainforest").setClimaticZone(ClimaticZone.tropical_rainforest).setLayerGenerator(baseGenerator));
		temperate_rainforest = new BiomeBase(53, BiomePropertiesExtended.newProperties("temperate_rainforest").setClimaticZone(ClimaticZone.temperate_rainforest).setLayerGenerator(baseGenerator));
		gigafungal_forest = new BiomeBase(54, BiomePropertiesExtended.newProperties("gigafungal_forest").setClimaticZone(ClimaticZone.subfrigid_forest).setLayerGenerator(baseGenerator));
		sequoia_forest = new BiomeBase(55, BiomePropertiesExtended.newProperties("sequoia_forest").setClimaticZone(ClimaticZone.frigid_plain).setLayerGenerator(baseGenerator));
		glacispical_land = new BiomeBase(56, BiomePropertiesExtended.newProperties("glacispical_land").setClimaticZone(ClimaticZone.iceland).setLayerGenerator(baseGenerator));
		
		mushroom_island = new BiomeBase(101, BiomePropertiesExtended.newProperties("mushroom_island").setClimaticZone(ClimaticZone.subtropical_wet).setLayerGenerator(new BLGSSimple(Blocks.MYCELIUM.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false)));
		river = new BiomeBase[ClimaticZone.values().length];
		beach = new BiomeBase[ClimaticZone.values().length];
		for(ClimaticZone zone : ClimaticZone.values())
		{
			river[zone.ordinal()] = new BiomeBase(121 + zone.ordinal(), BiomePropertiesExtended.newProperties("river_" + zone.name()).setClimaticZone(zone).setLayerGenerator(baseGenerator));
			beach[zone.ordinal()] = new BiomeBase(151 + zone.ordinal(), BiomePropertiesExtended.newProperties("beach_" + zone.name()).setClimaticZone(zone).setLayerGenerator(baseGenerator));
		}
	}
}