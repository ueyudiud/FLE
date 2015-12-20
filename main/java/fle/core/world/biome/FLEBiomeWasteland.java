package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class FLEBiomeWasteland extends FLEBiome
{
	public FLEBiomeWasteland(String name, int index)
	{
		super(name, index);
		theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 12;
        theBiomeDecorator.grassPerChunk = 1;
        theBiomeDecorator.reedsPerChunk = 8;
        theBiomeDecorator.cactiPerChunk = 2;
        spawnableCreatureList.clear();
        topBlock = Blocks.sand;
        fillerBlock = Blocks.sandstone;
	}
    
    @Override
    protected Block getBlock(boolean isFirstTop, boolean isNoCover,
    		boolean hasFluidOnSide, boolean isBaseDecorateBlock,
    		Block replaceBlock, Random rand, float temp)
    {
    	return isBaseDecorateBlock ? Blocks.gravel : isNoCover ? (rand.nextBoolean() ? Blocks.sand : Blocks.grass) :
    		rand.nextBoolean() ? Blocks.dirt : Blocks.sand;
    }
}