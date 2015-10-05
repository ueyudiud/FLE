package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class FLEBiomeOcean extends FLEBiome
{
	public FLEBiomeOcean(String name, int index)
	{
		super(name, index);
		spawnableCreatureList.clear();
		topBlock = Blocks.air;
		fillerBlock = Blocks.air;
	}

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }
    
    @Override
    protected void genTargetBlockAt(int aLayer, float aTemp, Random aRand,
    		Block[] aBlocks, byte[] aBytes, int targetID)
    {
		Block placeBlock;
		byte metadata;
		switch(aLayer)
		{
		case 0 : ;
		placeBlock = (targetID & 255) > 128 ? Blocks.air : Blocks.water;
		metadata = 0;
		break;
		case 1 : ;
		placeBlock = Blocks.sand;
		metadata = 0;
		break;
		case 2 : ;
		case 3 : ;
		placeBlock = Blocks.gravel;
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