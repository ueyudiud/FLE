package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenDesert;

public class FLEBiomeWasteland extends FLEBiome
{
	public FLEBiomeWasteland(String name, int index)
	{
		super(name, index);
		theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 15;
        theBiomeDecorator.grassPerChunk = 1;
        theBiomeDecorator.reedsPerChunk = 20;
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