package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import farcore.enums.EnumTemp;
import farcore.enums.EnumTerrain;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.IntCache;

public class LayerFloor extends LayerBase
{
	private LayerTerrainBase layer1;
	
	public LayerFloor(long seed, LayerBase layer, LayerTerrainBase layerTerrain)
	{
		super(seed);
		this.parent = layer;
		this.layer1 = layerTerrain;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] is1 = parent.getInts(x1, y1, w1, h1);
		int[] terrain = layer1.getInts(x, y, w, h);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				int id1 = (i + 1) * w1 + j + 1;
				int height = terrain[id];
				int type = 0;
				if(is1[id1] == 0)
				{
					if(is1[id1 - w1] != 0) ++height;
					if(is1[id1 - 1] != 0) ++height;
					if(is1[id1 + 1] != 0) ++height;
					if(is1[id1 + w1] != 0) ++height;
					if(height < EnumTerrain.hills.ordinal())
					{
						type = EnumBiome.ocean_deep.id();
					}
					else
					{
						type = EnumBiome.ocean.id();
					}
				}
				else
				{
//					if(is1[id1] == EnumBiome.river_grass.id())
//					{
//						type = (temps[id] == EnumTemp.freeze.ordinal() ?
//								EnumBiome.river_freeze.id() :
//									temps[id] == EnumTemp.blazing.ordinal() &&
//									rainfalls[id] > 8 ?
//											EnumBiome.river_rainforest.id() :
//												EnumBiome.river_grass.id());
//					}
//					else
					{
						if(height == EnumTerrain.basin.ordinal())
						{
							
						}
						else
						{
							if(is1[id1 - w1] == 0 ||
									is1[id1 - 1] == 0 ||
									is1[id1 + 1] == 0 ||
									is1[id1 + w1] == 0) --height;
							if(height == EnumTerrain.basin.ordinal())
								--height;
						}
						float rainfall, temp;
						if(height < 0 || height == EnumTerrain.depression.ordinal())
						{
							type = EnumBiome.swamp.id();
						}
						else if(height == EnumTerrain.plain.ordinal())
						{
							type = EnumBiome.plain.id();
						}
						else if(height == EnumTerrain.basin.ordinal())
						{
							type = EnumBiome.plain.id();
						}
						else if(height == EnumTerrain.hills.ordinal())
						{
							type = EnumBiome.low_hill.id();
						}
						else if(height == EnumTerrain.mountain.ordinal())
						{
							type = EnumBiome.mid_mountain.id();
						}
						else if(height == EnumTerrain.plateau.ordinal())
						{
							type = EnumBiome.plateau.id();
						}
						else if(height == EnumTerrain.ex_mountain.ordinal())
						{
							type = EnumBiome.high_mountain.id();
						}
					}
				}
				ret[id] = type;
			}
		return ret;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		layer1.initWorldGenSeed(seed);
	}
}