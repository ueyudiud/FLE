package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;

public class BiomeValley extends BiomeBase
{
	public BiomeValley(int id)
	{
		super(id);
		biomeDecorator.treesPerChunk = 1;
		biomeDecorator.brownMushroomsPerChunk = 2;
		biomeDecorator.flowersPerChunk = -999;
		biomeDecorator.ivyPerChunk = 1;
	}
}