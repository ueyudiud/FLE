package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class FLEBiomeOcean extends FLEBiome
{
	public FLEBiomeOcean(String name, int index, boolean isSandBase)
	{
		super(name, index);
		spawnableCreatureList.clear();
		theBiomeDecorator.generateLakes = false;
		topBlock = isSandBase ? Blocks.sand : Blocks.gravel;
		fillerBlock = Blocks.gravel;
	}
	
	@Override
	protected Block getBlock(boolean isFirstTop, boolean isNoCover,
			boolean hasFluidOnSide, boolean isBaseDecorateBlock,
			Block replaceBlock, Random rand, float temp)
	{
		return isFirstTop ? topBlock : fillerBlock;
	}
	
	@Override
	public boolean isOcean()
	{
		return true;
	}

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }
}