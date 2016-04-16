package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;

public class BiomeValley extends BiomeBase
{
	public BiomeValley(int id)
	{
		super(id);
		theBiomeDecorator.treesPerChunk = 1;
		theBiomeDecorator.mushroomsPerChunk = 2;
		theBiomeDecorator.flowersPerChunk = -999;
	}
}