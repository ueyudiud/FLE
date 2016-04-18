package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenAcacia;
import farcore.lib.world.gen.tree.TreeGenCanopy;
import farcore.lib.world.gen.tree.TreeGenSimple;
import fle.api.world.gen.TreeGenStraight;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenSavanna;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends BiomeBase
{
//    private static final WorldGenSavannaTree savannaTree = new WorldGenSavannaTree(false);

	private static boolean init = false;
	
	protected static final ITreeGenerator genAcacia = new TreeGenAcacia();
	
	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("acacia");
			genAcacia.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
	public BiomeSavanna(int id)
	{
		super(id);
		init();
        spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 1, 2, 6));
        biomeDecorator.treesPerChunk = 1;
        biomeDecorator.flowersPerChunk = 4;
        biomeDecorator.grassPerChunk = 20;
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		return genAcacia;
	}

//    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
//    {
//        return (WorldGenAbstractTree)(p_150567_1_.nextInt(5) > 0 ? savannaTree : this.worldGeneratorTrees);
//    }

    public void decorate(World world, Random random, int x, int z)
    {
        genTallFlowers.func_150548_a(2);

        for (int k = 0; k < 7; ++k)
        {
            int l = x + random.nextInt(16) + 8;
            int i1 = z + random.nextInt(16) + 8;
            int j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
            genTallFlowers.generate(world, random, l, j1, i1);
        }

        super.decorate(world, random, x, z);
    }
}