package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BiomeBushveld extends BiomeBase
{
    private static final WorldGenTrees shrub = new WorldGenShrub(0, 0);
    
	public BiomeBushveld(int id)
	{
		super(id);
        theBiomeDecorator.treesPerChunk = 3;
        theBiomeDecorator.flowersPerChunk = 1;
        theBiomeDecorator.grassPerChunk = 8;
	}

    public WorldGenAbstractTree func_150567_a(Random random)
    {
        return shrub;
    }
    
    @Override
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        if (layer > 1.75D)
        {
            this.topBlock = Blocks.sand;
            this.field_150604_aj = 0;
            this.fillerBlock = Blocks.dirt;
        }
        else if(layer < -0.8D)
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