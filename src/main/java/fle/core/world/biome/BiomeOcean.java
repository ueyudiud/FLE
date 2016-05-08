package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeOcean extends BiomeBase
{
	private static final Height HEIGHT = new Height(-.3F, 0.0F);
	
	public BiomeOcean(int id, boolean isShelf, boolean deep)
	{
		super(id);
		if(!isShelf)
		{
			setHeight(deep ? height_DeepOceans : height_Oceans);
		}
		else
		{
			setHeight(HEIGHT);
		}
		sedimentaryMultiply = 1.5F;
		metamorphismMultiply = 0.8F;
		topBlock = Blocks.gravel;
		secondBlock = Blocks.gravel;
		fillerBlock = Blocks.gravel;
		biomeDecorator.clayPerChunk = -1;
		spawnableCreatureList.clear();
	}

    public TempCategory getTempCategory()
    {
        return TempCategory.OCEAN;
    }
    
    @Override
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer,
    		float temp, float rainfall)
    {
    	if(layer > 0.8)
    	{
    		topBlock = Blocks.sand;
    	}
    	else
    	{
    		topBlock = Blocks.gravel;
    	}
    	super.genTerrainBlocks(world, rand, blocks, metas, x, z, layer, temp, rainfall);
    }
}