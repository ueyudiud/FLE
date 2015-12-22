package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class FLEBiomeRiver extends FLEBiome
{
	public FLEBiomeRiver(String name, int index)
	{
		super(name, index);
		spawnableCreatureList.clear();
		topBlock = fillerBlock = Blocks.dirt;
		theBiomeDecorator.sandPerChunk = 2;
	}
	
	@Override
	public boolean isRiver()
	{
		return true;
	}
	
	int height;
	
	public FLEBiomeRiver setWaterHeight(int aHeight)
	{
		height = aHeight;
		return this;
	}
    
    @Override
    protected Block getBlock(boolean isFirstTop, boolean isNoCover,
    		boolean hasFluidOnSide, boolean isBaseDecorateBlock,
    		Block replaceBlock, Random rand, float temp)
    {
    	return isBaseDecorateBlock ? Blocks.gravel : isNoCover || hasFluidOnSide ? Blocks.dirt : Blocks.grass;
    }
}