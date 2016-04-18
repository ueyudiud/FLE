package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeWasteland extends BiomeBase
{
	public BiomeWasteland(int id)
	{
		super(id);
		biomeDecorator.deadBushPerChunk = 3;
		biomeDecorator.grassPerChunk = 3;
		biomeDecorator.treesPerChunk = -999;
		biomeDecorator.cactiPerChunk = 1;
		spawnableCreatureList.clear();
	}
    
    @Override
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        if (layer > 0.2)
        {
            this.topBlock = Blocks.sand;
            this.field_150604_aj = 0;
            this.fillerBlock = Blocks.dirt;
        }
        else if(layer < -0.3)
        {
        	this.topBlock = Blocks.dirt;
        	this.field_150604_aj = 0;
        	this.fillerBlock = Blocks.dirt;
        }
        else
        {
            this.topBlock = Blocks.grass;
            this.field_150604_aj = 0;
            this.fillerBlock = Blocks.dirt;
        }
    	super.genTerrainBlocks(world, rand, blocks, metas, x, z, layer);
    }
}