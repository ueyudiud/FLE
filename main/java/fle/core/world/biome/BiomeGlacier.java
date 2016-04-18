package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeGlacier extends BiomeBase
{
	public BiomeGlacier(int id)
	{
		super(id);
		topBlock = Blocks.ice;
		biomeDecorator.grassPerChunk = -999;
		biomeDecorator.treesPerChunk = -999;
		spawnableCreatureList.clear();
	}
}