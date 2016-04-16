package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeGlacier extends BiomeBase
{
	public BiomeGlacier(int id)
	{
		super(id);
		topBlock = Blocks.ice;
		theBiomeDecorator.grassPerChunk = -999;
		theBiomeDecorator.treesPerChunk = -999;
		spawnableCreatureList.clear();
	}
}