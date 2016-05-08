package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeBeach extends BiomeBase
{
	public BiomeBeach(int id)
	{
		super(id);
		sedimentaryMultiply = 0.3F;
		metamorphismMultiply = 1.2F;
		setHeight(height_Shores);
        spawnableCreatureList.clear();
        biomeDecorator.treesPerChunk = -999;
        biomeDecorator.deadBushPerChunk = 0;
        biomeDecorator.reedsPerChunk = 0;
        biomeDecorator.cactiPerChunk = 0;
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer,
			float temp, float rainfall)
	{
		if(layer > -0.7F)
		{
        	topBlock = Blocks.sand;
        	secondBlock = Blocks.sand;
        	fillerBlock = Blocks.sandstone;
		}
		else
		{
        	topBlock = Blocks.stone;
        	secondBlock = Blocks.stone;
        	fillerBlock = Blocks.stone;
		}
    	super.genTerrainBlocks(world, rand, blocks, metas, x, z, layer, temp, rainfall);
	}
}