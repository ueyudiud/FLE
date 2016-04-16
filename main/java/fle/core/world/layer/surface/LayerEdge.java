package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerEdge extends LayerBase
{
	public LayerEdge(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int x1 = x - 1;
		int y1 = y - 1;
		int w1 = w + 2;
		int h1 = h + 2;
		int[] par = parent.getInts(x1, y1, w1, h1);
		int[] ret = IntCache.getIntCache(w * h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int biome = par[(i + 1) * w1 + (j + 1)];
				int b = biome;
				int bs[] = {par[i * w1 + (j + 1)], 
						par[(i + 2) * w1 + (j + 1)],
						par[(i + 1) * w1 + (j + 2)],
						par[(i + 1) * w1 + j]};
				
				if(biome == EnumBiome.rainforest.id() || biome == EnumBiome.rainforest_hill.id())
				{
					if(changed(bs, EnumBiome.rainforest.id(), EnumBiome.rainforest_hill.id()))
					{
						b = EnumBiome.rainforest_edge.id();
					}
				}
				else if(biome == EnumBiome.desert_hot.id())
				{
					if(changed(bs, EnumBiome.desert_hot.id(), EnumBiome.desert_subtropics.id(), EnumBiome.desert_subtropics_hill.id()))
					{
						b = EnumBiome.desert_tropic_edge.id();
					}
				}
				else if(biome == EnumBiome.desert_subtropics.id())
				{
					if(changed(bs, EnumBiome.desert_hot.id(), EnumBiome.desert_subtropics.id(), EnumBiome.desert_subtropics_hill.id()))
					{
						b = EnumBiome.desert_edge.id();
					}
				}
				else
				{
					boolean ocean1 = isOcean(bs[0]),
							ocean2 = isOcean(bs[1]),
							ocean3 = isOcean(bs[2]),
							ocean4 = isOcean(bs[3]);
					if(isWater(biome))
					{
						if(ocean1 && ocean2 && ocean3 && ocean4)
						{
							if((x + j) % 3 == 0 &&
									(y + i) % 3 == 0)
							{
								//Generate island.
								initChunkSeed(x + j, y + i);
								int rand = nextInt(2048);
								switch (rand)
								{
								case 0 :
								case 1 :
								case 2 :
									b = EnumBiome.volcanic_island.id();
									break;
								case 11 :
									b = EnumBiome.mushroom_island.id();
									break;
								default:
									break;
								}
							}
						}
						else if(ocean1 || ocean2 || ocean3 || ocean4)
						{
							if(isWater(bs[0]) && isWater(bs[1]) && isWater(bs[2]) && isWater(bs[3]))
							{
								initChunkSeed(x + j, y + i);
								if(nextInt(3) == 0)
								{
									//Added 512 to mark this is a invalid biome.
									if(biome == EnumBiome.river_freeze.id())
									{
										b = EnumBiome.beach_sand_snowy.id() | 512;
									}
									else if(biome == EnumBiome.river_rainforest.id())
									{
										b = EnumBiome.rainforest_edge.id() | 512;
									}
									else if(biome == EnumBiome.river_desert.id())
									{
										b = EnumBiome.beach_sand.id() | 512;
									}
									else
									{
										b = EnumBiome.plain.id() | 512;
									}
								}
							}
						}
					}
					else if((ocean1 || ocean2 || ocean3 || ocean4) &&
							(isRiver(bs[0]) || isRiver(bs[1]) || isRiver(bs[2]) || isRiver(bs[3])))
					{
						if(biome == EnumBiome.mountain_frigid.id() ||
								biome == EnumBiome.valley_frigid.id() ||
								biome == EnumBiome.forest_coniferous_snowy.id() ||
								biome == EnumBiome.forest_coniferous_snowy_hill.id() ||
								biome == EnumBiome.glacier.id())
						{
							b = EnumBiome.river_freeze.id();
						}
						else if(biome == EnumBiome.rainforest.id() || biome == EnumBiome.rainforest_hill.id())
						{
							b = EnumBiome.river_rainforest.id();
						}
						else if(biome == EnumBiome.desert.id() || biome == EnumBiome.desert_hot.id() ||
								biome == EnumBiome.desert_subtropics.id() || biome == EnumBiome.desert_subtropics_hill.id())
						{
							b = EnumBiome.river_desert.id();
						}
						else
						{
							b = EnumBiome.river_grass.id();
						}
					}
				}				
				ret[i * w + j] = b;
			}
		return ret;
	}
	
	private boolean changed(int[] target, int...ignore)
	{
		for(int i : target)
		{
			for(int j : ignore)
				if(i == j) continue;
			return true;
		}
		return false;
	}
	
	private boolean isWater(int id)
	{
		return isOcean(id) || isRiver(id);
	}
	
	private boolean isRiver(int id)
	{
		return id == EnumBiome.river_freeze.id() || id == EnumBiome.river_grass.id() ||
				id == EnumBiome.river_rainforest.id();
	}
	
	private boolean isOcean(int id)
	{
		return id == EnumBiome.ocean.id() || id == EnumBiome.ocean_deep.id() ||
				isDeepOcean(id);
	}
	
	private boolean isDeepOcean(int id)
	{
		return id == EnumBiome.ocean_icy.id() || id == EnumBiome.ocean_icy_deep.id();
	}
}