package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import farcore.enums.EnumTemp;
import farcore.enums.EnumTerrain;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.IntCache;

public class LayerFloor extends LayerBase
{
	private LayerTerrainBase layer1;
	private LayerTemp layer2;
	private LayerRainfall layer3;
	
	public LayerFloor(long seed, LayerBase layer, LayerTemp temp, LayerRainfall rainfall)
	{
		super(seed);
		this.parent = layer;
		this.layer1 = rainfall.getTerrain();
		this.layer2 = temp;
		this.layer3 = rainfall;
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
		int[] temps = layer2.getInts(x, y, w, h);
		int[] rainfalls = layer3.getInts(x, y, w, h);
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
						type = (temps[id] == EnumTemp.freeze.ordinal() ?
								EnumBiome.ocean_icy_deep.id() :
									EnumBiome.ocean_deep.id());
					}
					else
					{
						type = (temps[id] == EnumTemp.freeze.ordinal() ?
								EnumBiome.ocean_icy.id() :
									EnumBiome.ocean.id());
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
						if(height < 0 || height == EnumTerrain.channel.ordinal() || height == EnumTerrain.plain.ordinal())
						{
							temp = temps[id];
							rainfall = rainfalls[id];
							if(temp == EnumTemp.freeze.ordinal())
							{
								type = EnumBiome.glacier.id();
							}
							else if(temp == EnumTemp.cold.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous_snowy.id() :
											EnumBiome.tundra.id();
							}
							else if(temp == EnumTemp.cool.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous.id() :
											EnumBiome.tundra.id();
							}
							else if(temp == EnumTemp.warm.ordinal())
							{
								type = rainfall > 7 ?
										EnumBiome.forest_deciduous.id() :
											rainfall > 4 ?
												EnumBiome.plain.id() :
													rainfall > 2 ?
															EnumBiome.wasteland.id() : EnumBiome.desert.id();
							}
							else if(temp == EnumTemp.hot.ordinal())
							{
								type = rainfall > 7 ?
										EnumBiome.forest_evergreen.id() :
											rainfall > 4 ?
												EnumBiome.plain_subtropics.id() :
													rainfall > 2 ?
															EnumBiome.wasteland_subtropis.id() : EnumBiome.desert_subtropics.id();
							}
							else if(temp == EnumTemp.blazing.ordinal())
							{
								type = rainfall > 8 ?
										EnumBiome.rainforest.id() :
											rainfall > 6 ?
												EnumBiome.forest_tropic.id() :
													rainfall > 4 ?
															EnumBiome.savanna.id() :
																rainfall > 2 ? EnumBiome.bushveld.id() :
																	EnumBiome.desert_hot.id();
							}
						}
						else if(height == EnumTerrain.basin.ordinal())
						{
							temp = temps[id];
							rainfall = rainfalls[id];
							if(temp == EnumTemp.freeze.ordinal())
							{
								type = EnumBiome.glacier.id();
							}
							else if(temp == EnumTemp.cold.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous_snowy.id() :
											EnumBiome.tundra.id();
							}
							else if(temp == EnumTemp.cool.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous.id() :
											EnumBiome.tundra.id();
							}
							else if(temp == EnumTemp.warm.ordinal())
							{
								type = rainfall > 8 ?
										EnumBiome.swamp.id() :
											rainfall > 6 ?
													EnumBiome.forest_deciduous.id() :
														rainfall > 4 ?
																EnumBiome.plain.id() :
																	EnumBiome.wasteland.id();
							}
							else if(temp == EnumTemp.hot.ordinal())
							{
								type = rainfall > 7 ?
										EnumBiome.forest_evergreen.id() :
											rainfall > 4 ?
												EnumBiome.plain_subtropics.id() :
													EnumBiome.wasteland_subtropis.id();
							}
							else if(temp == EnumTemp.blazing.ordinal())
							{
								type = rainfall > 8 ?
										EnumBiome.rainforest.id() :
											rainfall > 6 ?
												EnumBiome.forest_tropic.id() :
													rainfall > 4 ?
															EnumBiome.savanna.id() : EnumBiome.bushveld.id();
							}
						}
						else if(height == EnumTerrain.hills.ordinal())
						{
							temp = temps[id];
							rainfall = rainfalls[id];
							if(temp == EnumTemp.freeze.ordinal())
							{
								type = EnumBiome.glacier.id();
							}
							else if(temp == EnumTemp.cold.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous_snowy_hill.id() :
											EnumBiome.tundra_hill.id();
							}
							else if(temp == EnumTemp.cool.ordinal())
							{
								type = rainfall > 6 ?
										EnumBiome.forest_coniferous_hill.id() :
											EnumBiome.tundra_hill.id();
							}
							else if(temp == EnumTemp.warm.ordinal())
							{
								type = rainfall > 7 ?
										EnumBiome.forest_deciduous_hill.id() :
											EnumBiome.grass_hill.id();
							}
							else if(temp == EnumTemp.hot.ordinal())
							{
								type = rainfall > 7 ?
										EnumBiome.forest_evergreen_hill.id() :
											rainfall > 3 ?
												EnumBiome.grassland_hill.id() : 
													EnumBiome.desert_subtropics_hill.id();
							}
							else if(temp == EnumTemp.blazing.ordinal())
							{
								type = rainfall > 8 ?
										EnumBiome.rainforest.id() :
											rainfall > 6 ?
												EnumBiome.forest_tropic.id() :
													rainfall > 4 ?
															EnumBiome.savanna.id() :
																rainfall > 2 ? EnumBiome.bushveld.id() :
																	EnumBiome.desert_hot.id();
							}
						}
						else if(height == EnumTerrain.mountain.ordinal())
						{
							temp = temps[id];
							if(temp == EnumTemp.freeze.ordinal())
							{
								type = EnumBiome.mountain_frigid.id();
							}
							else
							{
								rainfall = rainfalls[id];
								type = rainfall > 4 ?
										EnumBiome.mountain_deciduous_forest.id() :
											EnumBiome.mountain_meadow.id();
							}
						}
						else if(height == EnumTerrain.plateau.ordinal())
						{
							temp = temps[id];
							if(temp == EnumTemp.freeze.ordinal())
							{
								type = EnumBiome.tundra_plateau.id();
							}
							else if(temp < EnumTemp.warm.ordinal())
							{
								type = EnumBiome.grassland_plateau.id();
							}
							else if(temp < EnumTemp.blazing.ordinal())
							{
								rainfall = rainfalls[id];
								type = rainfall > 6 ?
										EnumBiome.forest_plateau.id() :
											EnumBiome.grassland_plateau.id();
							}
							else
							{
								type = EnumBiome.savanna_plateau.id();
							}
						}
						else if(height == EnumTerrain.ex_mountain.ordinal())
						{
							type = EnumBiome.mountain_snowy.id();
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
		layer2.initWorldGenSeed(seed);
		layer3.initWorldGenSeed(seed);
	}
}