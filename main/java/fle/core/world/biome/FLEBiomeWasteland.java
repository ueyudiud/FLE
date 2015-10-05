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
	}
	
	@Override
	protected void genTargetBlockAt(int aLayer, float aTemp, Random aRand,
			Block[] aBlocks, byte[] aBytes, int targetID)
	{
		boolean flag = aRand.nextBoolean();
		Block placeBlock;
		byte metadata;
		switch(aLayer)
		{
		case 0 : ;
		placeBlock = flag ? Blocks.dirt : Blocks.sand;
		metadata = 0;
		break;
		case 1 : ;
		case 2 : ;
		placeBlock = flag ? Blocks.dirt : Blocks.sand;
		metadata = 0;
		break;
		case 3 : ;
		placeBlock = flag ? Blocks.gravel : Blocks.sandstone;
		metadata = 0;
		break;
		default: ;
		placeBlock = Blocks.stone;
		metadata = 0;
		}
		
		aBlocks[targetID] = placeBlock;
		aBytes[targetID] = metadata;
	}
}