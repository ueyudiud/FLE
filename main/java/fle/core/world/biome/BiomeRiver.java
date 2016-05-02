package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeRiver extends BiomeBase
{
	public BiomeRiver(int id, boolean onDesert)
	{
		super(id);
		sedimentaryMultiply = 3.0F;
		metamorphismMultiply = 1.1F;
        spawnableCreatureList.clear();
        biomeDecorator.treesPerChunk = -999;
        biomeDecorator.ivyPerChunk = 1;
        if(onDesert)
        {
        	waterTop = Blocks.sand;
        	biomeDecorator.reedsPerChunk = 3;
        	biomeDecorator.cactiPerChunk = 3;
        	biomeDecorator.clayPerChunk = -999;
        }
        else
        {
        	waterTop = Blocks.dirt;
        }
	}
}