package fargen.core.layer.biome;

import farcore.data.EnumTerrain;
import fargen.core.FarGenBiomes;
import fargen.core.biome.BiomeBase;
import fargen.core.layer.Layer;
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
				int a = ter[q];
				int b = par[p];
				int a1 = ter[q - 1];
				int a2 = ter[q + 1];
				int a3 = ter[q - w1];
				int a4 = ter[q + w1];
				ret[j * w + i] = selectBiomeForBiome(a, b, a1, a2, a3, a4);
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
			return BiomeBase.getBiomeFromID(b1).zone.temperatureAverage > BiomeBase.minSnowTemperature ?
					FarGenBiomes.ocean_deep.biomeID : FarGenBiomes.icy_ocean_deep.biomeID;
		case ocean :
		case channel :
		case ridge :
		case basin :
		case ocean_valley :
			return BiomeBase.getBiomeFromID(b1).zone.temperatureAverage > BiomeBase.minSnowTemperature ?
					FarGenBiomes.ocean.biomeID : FarGenBiomes.icy_ocean.biomeID;
		case river :
			return FarGenBiomes.river[BiomeBase.getBiomeFromID(b1).zone.ordinal()].biomeID;
		default: return b;
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