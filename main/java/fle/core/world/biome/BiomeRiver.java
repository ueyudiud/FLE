package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenRiver;

public class BiomeRiver extends BiomeBase
{
	public BiomeRiver(int id, boolean onDesert)
	{
		super(id);
        spawnableCreatureList.clear();
        if(onDesert)
        {
        	topBlock = Blocks.sand;
        	theBiomeDecorator.reedsPerChunk = 3;
        	theBiomeDecorator.cactiPerChunk = 3;
        	theBiomeDecorator.clayPerChunk = -999;
        }
	}
}