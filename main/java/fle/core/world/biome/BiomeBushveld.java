package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenShrub;
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
//    private static final WorldGenTrees shrub = new WorldGenShrub(0, 0);

	private static boolean init = false;
	
	protected static final ITreeGenerator genShrub = new TreeGenShrub();

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("oak");
			genShrub.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	public BiomeBushveld(int id)
	{
		super(id);
		init();
        biomeDecorator.treesPerChunk = 3;
        biomeDecorator.flowersPerChunk = 1;
        biomeDecorator.grassPerChunk = 8;
        biomeDecorator.ivyPerChunk = 1;
        biomeDecorator.rattanPerChunk = 1;
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise) 
	{
		return genShrub;
	}

//    public WorldGenAbstractTree func_150567_a(Random random)
//    {
//        return shrub;
//    }
    
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