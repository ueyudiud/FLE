package fargen.core.layer.biome;

import static fargen.core.FarGenBiomes.boreal_forest;
import static fargen.core.FarGenBiomes.frozen_tundra;
import static fargen.core.FarGenBiomes.gigafungal_forest;
import static fargen.core.FarGenBiomes.glacispical_land;
import static fargen.core.FarGenBiomes.grassland;
import static fargen.core.FarGenBiomes.mangrove;
import static fargen.core.FarGenBiomes.meadow;
import static fargen.core.FarGenBiomes.rockland;
import static fargen.core.FarGenBiomes.rocky_desert;
import static fargen.core.FarGenBiomes.savanna;
import static fargen.core.FarGenBiomes.sclerophyll_forest;
import static fargen.core.FarGenBiomes.sequoia_forest;
import static fargen.core.FarGenBiomes.shrubland;
import static fargen.core.FarGenBiomes.subtropical_broadleaf_forest;
import static fargen.core.FarGenBiomes.subtropical_coniferous_forest;
import static fargen.core.FarGenBiomes.swamp;
import static fargen.core.FarGenBiomes.temperate_broadleaf_forest;
import static fargen.core.FarGenBiomes.temperate_desert;
import static fargen.core.FarGenBiomes.temperate_mixed_forest;
import static fargen.core.FarGenBiomes.temperate_rainforest;
import static fargen.core.FarGenBiomes.tropical_desert;
import static fargen.core.FarGenBiomes.tropical_monsoon_forest;
import static fargen.core.FarGenBiomes.tropical_rainforest;
import static fargen.core.FarGenBiomes.tropical_thorny_forest;
import static fargen.core.FarGenBiomes.tundra;

import fargen.core.biome.BiomeBase;
import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerBaseBiome extends Layer
{
	private final BiomeBase[][] biomes = {
			{rocky_desert,     rocky_desert,     tundra,    tundra,    tundra,                        frozen_tundra,                 frozen_tundra,                frozen_tundra,                glacispical_land,        glacispical_land,     glacispical_land},
			{rocky_desert,     rocky_desert,     meadow,    meadow,    meadow,                        boreal_forest,                 boreal_forest,                boreal_forest,                sequoia_forest,          sequoia_forest,       glacispical_land},
			{rocky_desert,     rocky_desert,     grassland, grassland, grassland,                     boreal_forest,                 boreal_forest,                boreal_forest,                sequoia_forest,          sequoia_forest,       sequoia_forest},
			{temperate_desert, temperate_desert, rockland,  grassland, grassland,                     temperate_broadleaf_forest,    temperate_mixed_forest,       swamp,                        swamp,                   gigafungal_forest,    gigafungal_forest},
			{temperate_desert, temperate_desert, shrubland, shrubland, sclerophyll_forest,            temperate_broadleaf_forest,    temperate_broadleaf_forest,   swamp,                        swamp,                   gigafungal_forest,    gigafungal_forest},
			{temperate_desert, temperate_desert, shrubland, shrubland, subtropical_coniferous_forest, subtropical_coniferous_forest, subtropical_broadleaf_forest, subtropical_broadleaf_forest, mangrove,                temperate_rainforest, temperate_rainforest},
			{tropical_desert,  tropical_desert,  savanna,   savanna,   subtropical_coniferous_forest, subtropical_coniferous_forest, subtropical_broadleaf_forest, subtropical_broadleaf_forest, mangrove,                tropical_rainforest,  tropical_rainforest},
			{tropical_desert,  tropical_desert,  savanna,   savanna,   tropical_thorny_forest,        tropical_thorny_forest,        tropical_thorny_forest,       tropical_monsoon_forest,      tropical_monsoon_forest, tropical_rainforest,  tropical_rainforest},
			{tropical_desert,  savanna,          savanna,   savanna,   tropical_thorny_forest,        tropical_thorny_forest,        tropical_thorny_forest,       tropical_monsoon_forest,      tropical_monsoon_forest, tropical_rainforest,  tropical_rainforest}};
	
	private GenLayer temperature;
	private GenLayer humidity;
	
	public LayerBaseBiome(long seed, GenLayer temperature, GenLayer humidity)
	{
		super(seed);
		this.temperature = temperature;
		this.humidity = humidity;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] tem = temperature.getInts(x, y, w, h);
		int[] hum = humidity.getInts(x, y, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < w * h; ++i)
		{
			ret[i] = selectBaseBiome(tem[i], hum[i]);
		}
		return ret;
	}
	
	protected int selectBaseBiome(int temperature, int humidity)
	{
		temperature = farcore.util.L.range(0, 8, temperature);
		humidity = farcore.util.L.range(0, 10, humidity);
		return biomes[temperature][humidity].biomeID;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		temperature.initWorldGenSeed(seed);
		humidity.initWorldGenSeed(seed);
	}
	
	@Override
	public void markZoom(int zoom)
	{
		super.markZoom(zoom);
		if(temperature instanceof Layer)
		{
			((Layer) temperature).markZoom(zoom * zoomLevel);
		}
		if(humidity instanceof Layer)
		{
			((Layer) humidity).markZoom(zoom * zoomLevel);
		}
	}
}