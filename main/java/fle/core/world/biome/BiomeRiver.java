package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeRiver extends BiomeBase
{
	public BiomeRiver(int id, boolean onDesert)
	{
		super(id);
        spawnableCreatureList.clear();
        biomeDecorator.treesPerChunk = -999;
        if(onDesert)
        {
        	topBlock = Blocks.sand;
        	biomeDecorator.reedsPerChunk = 3;
        	biomeDecorator.cactiPerChunk = 3;
        	biomeDecorator.clayPerChunk = -999;
        }
        else
        {
        	topBlock = Blocks.dirt;
        }
	}
}