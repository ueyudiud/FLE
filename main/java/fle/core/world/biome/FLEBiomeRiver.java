package fle.core.world.biome;

import java.util.Random;

import fle.api.enums.EnumFLERock;
import fle.api.material.MaterialRock;
import fle.core.block.BlockFleRock;
import fle.core.block.BlockRock;
import fle.core.init.IB;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class FLEBiomeRiver extends FLEBiome
{
	public FLEBiomeRiver(String name, int index)
	{
		super(name, index);
		spawnableCreatureList.clear();
		topBlock = fillerBlock = Blocks.dirt;
		theBiomeDecorator.sandPerChunk = 2;
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