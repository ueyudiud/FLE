/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.layer.biome;

import static fargen.core.FarGenBiomes.boreal_forest;
import static fargen.core.FarGenBiomes.frozen_tundra;
import static fargen.core.FarGenBiomes.gigafteral_forest;
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

import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiome extends Layer
{
	public LayerSurfaceBiome(long seed, GenLayer biomes)
	{
		super(seed);
		this.parent = biomes;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = this.parent.getInts(x, y, w, h);
		for (int y1 = 0; y1 < h; ++y1)
			for (int x1 = 0; x1 < w; ++x1)
			{
				int id = y1 * w + x1;
				ret[id] = getBiome(ret[id] & 0xF, ret[id] >> 4 & 0xF, ret[id] >> 8 & 0xF);
			}
		return ret;
	}
	
	protected int getBiome(int temp, int rain, int rare)
	{
		switch (temp)
		{
		case 0:
			return (rain < 4 ? rocky_desert : rain < 8 ? tundra : rain < 12 ? frozen_tundra : glacispical_land).biomeID;
		case 1:
			return (rain < 4 ? rocky_desert : rain < 8 ? meadow : rain < 12 ? boreal_forest : rain < 15 ? sequoia_forest : glacispical_land).biomeID;
		case 2:
			return (rain < 4 ? rocky_desert : rain < 8 ? grassland : rain < 12 ? boreal_forest : sequoia_forest).biomeID;
		case 3:
			return (rain < 3 ? temperate_desert : rain < 4 ? rockland : rain < 7 ? grassland : rain < 10 ? temperate_broadleaf_forest : rain < 11 ? temperate_mixed_forest : rain < 13 ? swamp : gigafungal_forest).biomeID;
		case 4:
			return (rain < 3 ? temperate_desert : rain < 5 ? shrubland : rain < 8 ? sclerophyll_forest : rain < 11 ? temperate_broadleaf_forest : rain < 14 ? swamp : gigafungal_forest).biomeID;
		case 5:
			return (rain < 3 ? temperate_desert : rain < 6 ? shrubland : rain < 9 ? subtropical_broadleaf_forest : rain < 12 ? subtropical_coniferous_forest : rain < 14 ? mangrove : temperate_rainforest).biomeID;
		case 6:
			return (rain < 2 ? tropical_desert : rain < 6 ? savanna : rain < 9 ? subtropical_broadleaf_forest : rain < 12 ? subtropical_coniferous_forest : rain < 14 ? mangrove : (rare > 9 ? gigafteral_forest : tropical_rainforest)).biomeID;
		case 7:
			return (rain < 3 ? tropical_desert : rain < 7 ? savanna : rain < 10 ? tropical_thorny_forest : rain < 13 ? tropical_monsoon_forest : (rare > 8 ? gigafteral_forest : tropical_rainforest)).biomeID;
		default:
			return -1;
		}
	}
}
