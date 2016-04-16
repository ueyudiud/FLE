package farcore.enums;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;

public enum EnumBiome
{
	rainforest,
	forest_tropic,
	savanna,
	bushveld,
	desert_hot,
	
	rainforest_hill,
	savanna_hill,
	bushveld_hill,
	
	forest_evergreen,
	plain_subtropics,
	wasteland_subtropis,
	desert_subtropics,
	
	forest_evergreen_hill,
	grassland_hill,
	desert_subtropics_hill,
	
	forest_deciduous,
	plain,
	wasteland,
	desert,
	
	forest_deciduous_hill,
	grass_hill,

	forest_coniferous,
	tundra,
	
	forest_coniferous_hill,
	tundra_hill,
	
	forest_coniferous_snowy,
	
	forest_coniferous_snowy_hill,
	
	glacier,

	volcanic_island,
	tropic_island,
	mushroom_island,
	
	swamp,
	
	mountain_frigid,
	mountain_meadow,
	mountain_deciduous_forest,
	
	savanna_plateau,
	forest_plateau,
	grassland_plateau,
	tundra_plateau,
	
	mountain_snowy,
	
	ocean,
	ocean_deep,
	ocean_icy,
	ocean_icy_deep,
	
	rainforest_edge,
	desert_tropic_edge,
	desert_edge,
	
	valley_tropic,
	valley_temperate,
	valley_frigid,

	beach_sand,
	beach_sand_snowy,
	beach_stone,
	beach_stone_snowy,

	continental_shelf,
	continental_shelf_snowy,
	
	river_rainforest,
	river_grass,
	river_freeze,
	river_desert;
	
	private BiomeBase biome;
	
	EnumBiome() {}
	
	public void setBiome(BiomeGenBase biome)
	{
		if(this.biome != null)
		{
			throw new RuntimeException("The biome " + this.biome.biomeName + " has already registered!");
		}
		this.biome = (BiomeBase) biome;
		this.biome.setBiomeName(name());
	}
	
	public BiomeBase biome()
	{
		return biome;
	}

	public int id()
	{
		return biome.biomeID;
	}
}