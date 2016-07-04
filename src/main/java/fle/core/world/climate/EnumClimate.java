package fle.core.world.climate;

import java.util.ArrayList;
import java.util.List;

public enum EnumClimate
{
	ocean,
	ocean_frozen,
	deep_ocean,
	continental_shelf,
	river,
	river_frozen,
	tropical_desert,
	savanna,
	tropical_thorny_forest,
	tropical_monsoon_forest,
	tropical_rainforest,
	gigafteral_forest,
	subtropical_coniferous_forest,
	subtropical_broadleaf_forest,
	mangrove,
	temperate_rainforest,
	temperate_desert,
	shrubland,
	sclerophyll_forest,
	deciduous_broad_leaved_forest,
	swamp,
	gigafungal_forest,
	rockland,
	grassland,
	temperate_mixed_forest,
	rocky_desert,
	boreal_forest,
	sequoia_forest,
	meadow,
	tundra,
	frozen_turdra,
	ice_spikes_land;
	
	public static final EnumClimate[][] climateGraphEarth = {
			{tropical_desert,	savanna,			savanna,	savanna,	tropical_thorny_forest,			tropical_thorny_forest,			tropical_thorny_forest,			tropical_monsoon_forest,		tropical_monsoon_forest,tropical_rainforest,	tropical_rainforest},
			{tropical_desert,	tropical_desert,	savanna,	savanna,	subtropical_coniferous_forest,	subtropical_coniferous_forest,	subtropical_broadleaf_forest,	subtropical_broadleaf_forest,	mangrove,				tropical_rainforest,	tropical_rainforest},
			{temperate_desert,	temperate_desert,	shrubland,	shrubland,	subtropical_coniferous_forest,	subtropical_coniferous_forest,	subtropical_broadleaf_forest,	subtropical_broadleaf_forest,	mangrove,				temperate_rainforest,	temperate_rainforest},
			{temperate_desert,	temperate_desert,	shrubland,	shrubland,	sclerophyll_forest,				deciduous_broad_leaved_forest,	deciduous_broad_leaved_forest,	swamp,							swamp, 					gigafungal_forest,		gigafungal_forest},
			{temperate_desert,	temperate_desert,	rockland,	grassland,	grassland,						deciduous_broad_leaved_forest,	temperate_mixed_forest,			swamp,							swamp, 					gigafungal_forest,		gigafungal_forest},
			{rocky_desert,		rocky_desert,		grassland,	grassland,	grassland,						boreal_forest,					boreal_forest,					boreal_forest,					sequoia_forest,			sequoia_forest,			sequoia_forest},
			{rocky_desert,		rocky_desert,		meadow,		meadow,		meadow,							boreal_forest,					boreal_forest,					boreal_forest,					sequoia_forest,			sequoia_forest,			ice_spikes_land},
			{rocky_desert,		rocky_desert,		tundra,		tundra,		tundra,							frozen_turdra,					frozen_turdra,					frozen_turdra,					ice_spikes_land,		ice_spikes_land,		ice_spikes_land}};

	static
	{
		tropical_rainforest.setAssociated(gigafteral_forest);
	}
	
	List<EnumClimate> associated = new ArrayList();
	
	Climate climate;
	
	EnumClimate()
	{
		associated.add(this);
	}
	
	public void setClimate(Climate climate)
	{
		this.climate = climate;
	}
	
	public Climate climate()
	{
		return climate;
	}
	
	public List<EnumClimate> getAssociated()
	{
		return associated;
	}
	
	void setAssociated(EnumClimate climate)
	{
		associated.add(climate);
		climate.associated.add(this);
	}

	
	public int id()
	{
		return climate.climateID;
	}
}