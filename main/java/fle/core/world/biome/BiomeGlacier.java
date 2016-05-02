package fle.core.world.biome;

import farcore.enums.EnumBlock;
import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeGlacier extends BiomeBase
{
	public BiomeGlacier(int id)
	{
		super(id);
		topBlock = EnumBlock.ice.block();
		secondBlock = Blocks.dirt;
		fillerBlock = Blocks.dirt;
		biomeDecorator.grassPerChunk = -999;
		biomeDecorator.treesPerChunk = -999;
		spawnableCreatureList.clear();
	}
}