package fle.core.world.layer.surface;

import farcore.enums.EnumBiome;
import farcore.lib.world.biome.BiomeBase;
import fle.core.world.layer.LayerBase;
import net.minecraft.world.gen.layer.GenLayer;

public class LayerRiver extends LayerBase
{
	private GenLayer layer;
	
	public LayerRiver(long seed, GenLayer layer, GenLayer river)
	{
		super(seed);
		this.parent = layer;
		this.layer = river;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] is = parent.getInts(x, y, w, h);
		int[] river = layer.getInts(x, y, w, h);
		for(int i = 0; i < h; ++i)
			for(int j = 0; j < w; ++j)
			{
				int id = i * w + j;
				if(river[id] != 0)
				{
					if(isOcean(is[id]))
					{
						
					}
					else
					{
						if(id == EnumBiome.mountain_frigid.id() ||
								id == EnumBiome.valley_frigid.id() ||
								id == EnumBiome.forest_coniferous_snowy.id() ||
								id == EnumBiome.forest_coniferous_snowy_hill.id() ||
								id == EnumBiome.glacier.id())
						{
							is[id] = EnumBiome.river_freeze.id();
						}
						else if(id == EnumBiome.rainforest.id() || id == EnumBiome.rainforest_hill.id())
						{
							is[id] = EnumBiome.river_rainforest.id();
						}
						else if(id == EnumBiome.desert.id() || id == EnumBiome.desert_hot.id() ||
								id == EnumBiome.desert_subtropics.id() || id == EnumBiome.desert_subtropics_hill.id())
						{
							is[id] = EnumBiome.river_desert.id();
						}
						else
						{
							is[id] = EnumBiome.river_grass.id();
						}
					}
				}
			}
		return is;
	}
	
	private boolean isOcean(int id)
	{
		return id == EnumBiome.ocean.id() || id == EnumBiome.ocean_deep.id() ||
				id == EnumBiome.ocean_icy.id() || id == EnumBiome.ocean_icy_deep.id();
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		layer.initWorldGenSeed(seed);
	}
}