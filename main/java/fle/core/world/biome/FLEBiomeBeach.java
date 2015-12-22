package fle.core.world.biome;

import net.minecraft.init.Blocks;

public class FLEBiomeBeach extends FLEBiome
{
	public FLEBiomeBeach(String name, int index)
	{
		super(name, index);
        this.spawnableCreatureList.clear();
        topBlock = Blocks.sand;
        fillerBlock = Blocks.sand;
        theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 0;
        theBiomeDecorator.reedsPerChunk = 0;
        theBiomeDecorator.cactiPerChunk = 0;
        theBiomeDecorator.generateLakes = false;
	}
	
	@Override
	public boolean isBeach()
	{
		return true;
	}
}