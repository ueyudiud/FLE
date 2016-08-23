package fargen.core.layer.biome;

import farcore.data.EnumTempCategory;
import farcore.data.EnumTerrain;
import fargen.core.FarGenBiomes;
import fargen.core.biome.BiomeBase;
import fargen.core.layer.Layer;
import fargen.core.util.ClimaticZone;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerBiomeSurfaceMixed extends Layer
{
	private GenLayer terrain;

	public LayerBiomeSurfaceMixed(long seed, GenLayer layer, GenLayer terrain)
	{
		super(seed);
		parent = layer;
		this.terrain = terrain;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] par = parent.getInts(x, y, w, h);
		int[] ter = terrain.getInts(x1, y1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		for(int j = 0; j < h; j ++)
		{
			for(int i = 0; i < w; i ++)
			{
				int p = j * w + i;
				int q = (j + 1) * w1 + i + 1;
				int a1 = ter[q - 1];
				int a2 = ter[q + 1];
				int a3 = ter[q - w1];
				int a4 = ter[q + w1];
				ret[j * w + i] = selectBiomeForBiome(ter[q], par[p], a1, a2, a3, a4);
			}
		}
		return ret;
	}

	private int selectBiomeForBiome(int a, int b, int a1, int a2, int a3, int a4)
	{
		int biome = selectBiomeForStandardBiome(a, b);
		if(EnumTerrain.values()[a].isOcean)
		{
			if(!EnumTerrain.values()[a1].isWater ||
					!EnumTerrain.values()[a2].isWater ||
					!EnumTerrain.values()[a3].isWater ||
					!EnumTerrain.values()[a4].isWater)
				return FarGenBiomes.beach[a].biomeID;
		}
		return biome;
	}

	private int selectBiomeForStandardBiome(int a, int b)
	{
		int b1 = b & 0xFF;
		switch (EnumTerrain.values()[a])
		{
		case tectogene :
		case deep_ocean :
			return selectOceanBiome(b1).biomeID;
		case ocean :
		case channel :
		case ridge :
		case basin :
		case ocean_valley :
			return selectDeepOceanBiome(b1).biomeID;
		case river :
			ClimaticZone zone = BiomeBase.getBiomeFromID(b1).zone;
			return zone.category1 == EnumTempCategory.OCEAN ?
					selectOceanBiome(b1).biomeID : FarGenBiomes.river[zone.ordinal()].biomeID;
		default: return b;
		}
	}
	
	private BiomeBase selectOceanBiome(int b)
	{
		switch(BiomeBase.getBiomeFromID(b).zone.category1)
		{
		case TROPICAL : return FarGenBiomes.ocean_t;
		case SUBTROPICAL : return FarGenBiomes.ocean_st;
		case TEMPERATE : return FarGenBiomes.ocean_te;
		case SUBFRIGID : return FarGenBiomes.ocean_sf;
		case FRIGID : return FarGenBiomes.ocean_f;
		default : return BiomeBase.DEBUG;
		}
	}
	
	private BiomeBase selectDeepOceanBiome(int b)
	{
		switch(BiomeBase.getBiomeFromID(b).zone.category1)
		{
		case TROPICAL : return FarGenBiomes.ocean_t_deep;
		case SUBTROPICAL : return FarGenBiomes.ocean_st_deep;
		case TEMPERATE : return FarGenBiomes.ocean_te_deep;
		case SUBFRIGID : return FarGenBiomes.ocean_sf_deep;
		case FRIGID : return FarGenBiomes.ocean_f_deep;
		default : return BiomeBase.DEBUG;
		}
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		terrain.initWorldGenSeed(seed);
	}
	
	@Override
	public void markZoom(int zoom)
	{
		super.markZoom(zoom);
		if(terrain instanceof Layer)
		{
			((Layer) terrain).markZoom(zoom);
		}
	}
}